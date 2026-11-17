package com.example.data.repository

import android.util.Log
import com.example.core.AppLanguage
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.InlineDataPart
import com.google.firebase.ai.type.LatLng
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.RetrievalConfig
import com.google.firebase.ai.type.SpeechConfig
import com.google.firebase.ai.type.Tool
import com.google.firebase.ai.type.ToolConfig
import com.google.firebase.ai.type.Voice
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

private const val TAG = "AiMarketplaceRepo"

data class GroundingSource(
    val title: String,
    val uri: String,
    val isMapsSource: Boolean = false,
    val placeId: String? = null
)

data class GroundedAiAnswer(
    val text: String,
    val sources: List<GroundingSource>,
    val searchSuggestionsHtml: String? = null,
    val usedMapsGrounding: Boolean = false
)

class AiMarketplaceRepository {

    private val searchGroundedModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = "gemini-4-hydrogen",
            tools = listOf(Tool.googleSearch())
        )
    }

    private fun createMapsGroundedModel(latitude: Double, longitude: Double, language: AppLanguage) =
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = "gemini-4-hydrogen",
            tools = listOf(Tool.googleMaps()),
            toolConfig = ToolConfig(
                retrievalConfig = RetrievalConfig.builder()
                    .setLatLng(LatLng(latitude = latitude, longitude = longitude))
                    .setLanguageCode(language.ttsLocale)
                    .build()
            )
        )

    @OptIn(PublicPreviewAPI::class)
    private fun createTtsModel(modelName: String, language: AppLanguage) =
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = modelName,
            generationConfig = generationConfig {
                responseModalities = listOf(ResponseModality.AUDIO)
                speechConfig = SpeechConfig(
                    voice = Voice("Kore"),
                    languageCode = language.ttsLocale
                )
            }
        )

    suspend fun askWithSearchGrounding(
        query: String,
        language: AppLanguage,
        cityContext: String? = null
    ): Result<GroundedAiAnswer> {
        return try {
            val langInstruction = when (language) {
                AppLanguage.AR -> "Respond clearly and concisely in Arabic."
                AppLanguage.FR -> "Réponds clairement et de manière concise en français."
                AppLanguage.ES -> "Responde de forma clara y concisa en español."
                AppLanguage.EN -> "Respond clearly and concisely in English."
            }
            val locationLine = cityContext?.takeIf { it.isNotBlank() }?.let {
                "Location context: $it."
            } ?: ""

            val prompt = """
                You are the TrustCraft global home-services and artisan marketplace advisor.
                $langInstruction
                $locationLine
                Use live Google Search results to provide accurate, up-to-date local pricing ranges, safety/licensing tips, or material guidance.
                User question: $query
            """.trimIndent()

            val response = withTimeout(60_000L) {
                searchGroundedModel.generateContent(prompt)
            }

            val text = response.text?.trim().orEmpty()
            if (text.isBlank()) {
                return Result.failure(IllegalStateException("Model returned an empty response."))
            }

            val metadata = response.candidates.firstOrNull()?.groundingMetadata
            val renderedHtml = metadata?.searchEntryPoint?.renderedContent
            val sources = metadata?.groundingChunks.orEmpty().mapNotNull { chunk ->
                val web = chunk.web
                if (web != null && !web.uri.isNullOrBlank()) {
                    GroundingSource(
                        title = web.title?.ifBlank { web.uri } ?: "Web Source",
                        uri = web.uri ?: "",
                        isMapsSource = false
                    )
                } else null
            }.distinctBy { it.uri }

            Result.success(
                GroundedAiAnswer(
                    text = text,
                    sources = sources,
                    searchSuggestionsHtml = renderedHtml,
                    usedMapsGrounding = false
                )
            )
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Search grounding timed out", e)
            Result.failure(Exception("Request timed out after 60 seconds. Please try again.", e))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Search grounding failed", e)
            Result.failure(e)
        }
    }

    suspend fun findNearbyInsightsWithMapsGrounding(
        latitude: Double,
        longitude: Double,
        city: String,
        tradeLabel: String,
        language: AppLanguage
    ): Result<GroundedAiAnswer> {
        return try {
            val langInstruction = when (language) {
                AppLanguage.AR -> "Respond in Arabic."
                AppLanguage.FR -> "Réponds en français."
                AppLanguage.ES -> "Responde en español."
                AppLanguage.EN -> "Respond in English."
            }
            val model = createMapsGroundedModel(latitude, longitude, language)
            val prompt = """
                $langInstruction
                Near $city ($latitude, $longitude), list 3-4 helpful real hardware/supply stores, artisan workshops, or trade centers relevant to $tradeLabel, with brief practical notes.
            """.trimIndent()

            val response = withTimeout(60_000L) {
                model.generateContent(prompt)
            }

            val text = response.text?.trim().orEmpty()
            if (text.isBlank()) {
                return Result.failure(IllegalStateException("Model returned an empty Maps response."))
            }

            val metadata = response.candidates.firstOrNull()?.groundingMetadata
            val sources = metadata?.groundingChunks.orEmpty().mapNotNull { chunk ->
                val maps = chunk.maps
                if (maps != null && !maps.uri.isNullOrBlank()) {
                    GroundingSource(
                        title = maps.title?.ifBlank { "Google Maps Place" } ?: "Google Maps Place",
                        uri = maps.uri ?: "",
                        isMapsSource = true,
                        placeId = maps.placeId
                    )
                } else null
            }.distinctBy { it.uri }

            Result.success(
                GroundedAiAnswer(
                    text = text,
                    sources = sources,
                    searchSuggestionsHtml = null,
                    usedMapsGrounding = true
                )
            )
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Maps grounding timed out", e)
            Result.failure(Exception("Maps discovery timed out after 60 seconds.", e))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Maps grounding failed", e)
            Result.failure(e)
        }
    }

    suspend fun synthesizeSpeech(text: String, language: AppLanguage): Result<ByteArray> {
        val cleanText = text.trim().take(900)
        if (cleanText.isBlank()) {
            return Result.failure(IllegalArgumentException("No text provided for speech synthesis."))
        }

        // TTS models synthesize the supplied transcript; keep instructions out of the spoken text.
        val prompt = cleanText

        suspend fun invokeTts(modelName: String): ByteArray {
            val model = createTtsModel(modelName, language)
            val response = withTimeout(60_000L) {
                model.generateContent(prompt)
            }
            val parts = response.candidates.firstOrNull()?.content?.parts.orEmpty()
            val audioPart = parts.filterIsInstance<InlineDataPart>().firstOrNull()
                ?: throw IllegalStateException("TTS model did not return audio data.")
            return audioPart.inlineData
        }

        return try {
            val bytes = invokeTts("gemini-3.8-flash-tts")
            Result.success(bytes)
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "TTS timed out", e)
            Result.failure(Exception("Voice generation timed out after 60 seconds.", e))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val msg = e.message.orEmpty()
            if (msg.contains("not found", ignoreCase = true) || msg.contains("404")) {
                Log.w(TAG, "gemini-3.8-flash-tts unavailable, retrying with gemini-3.8-flash-lite-tts", e)
                return try {
                    val fallbackBytes = withTimeout(60_000L) {
                        invokeTts("gemini-3.8-flash-lite-tts")
                    }
                    Result.success(fallbackBytes)
                } catch (e2: CancellationException) {
                    throw e2
                } catch (e2: Exception) {
                    Log.e(TAG, "TTS fallback failed", e2)
                    Result.failure(e2)
                }
            }
            Log.e(TAG, "TTS failed", e)
            Result.failure(e)
        }
    }
}
