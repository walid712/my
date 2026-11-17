package com.example.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.AppLanguage
import com.example.core.AudioPcmPlayer
import com.example.data.repository.AiMarketplaceRepository
import com.example.data.repository.GroundedAiAnswer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "AiAssistantVM"

sealed interface AiQueryUiState {
    data object Idle : AiQueryUiState
    data object Loading : AiQueryUiState
    data class Success(val answer: GroundedAiAnswer) : AiQueryUiState
    data class Error(val message: String) : AiQueryUiState
}

sealed interface TtsUiState {
    data object Idle : TtsUiState
    data class Generating(val activeTag: String) : TtsUiState
    data class Playing(val activeTag: String) : TtsUiState
    data class Error(val message: String) : TtsUiState
}

class AiAssistantViewModel(
    private val aiRepository: AiMarketplaceRepository = AiMarketplaceRepository(),
    private val audioPlayer: AudioPcmPlayer = AudioPcmPlayer()
) : ViewModel() {

    private val _conciergeState = MutableStateFlow<AiQueryUiState>(AiQueryUiState.Idle)
    val conciergeState: StateFlow<AiQueryUiState> = _conciergeState.asStateFlow()

    private val _mapsInsightsState = MutableStateFlow<AiQueryUiState>(AiQueryUiState.Idle)
    val mapsInsightsState: StateFlow<AiQueryUiState> = _mapsInsightsState.asStateFlow()

    private val _ttsState = MutableStateFlow<TtsUiState>(TtsUiState.Idle)
    val ttsState: StateFlow<TtsUiState> = _ttsState.asStateFlow()

    fun askSearchGroundedConcierge(
        query: String,
        language: AppLanguage,
        cityContext: String? = null
    ) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return

        viewModelScope.launch {
            _conciergeState.value = AiQueryUiState.Loading
            try {
                val result = aiRepository.askWithSearchGrounding(trimmed, language, cityContext)
                result.onSuccess { answer ->
                    _conciergeState.value = AiQueryUiState.Success(answer)
                }.onFailure { err ->
                    _conciergeState.value = AiQueryUiState.Error(
                        err.localizedMessage ?: "Search-grounded AI request failed."
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected AI concierge error", e)
                _conciergeState.value = AiQueryUiState.Error(
                    e.localizedMessage ?: "AI concierge error."
                )
            }
        }
    }

    fun fetchNearbyMapsInsights(
        latitude: Double,
        longitude: Double,
        city: String,
        tradeLabel: String,
        language: AppLanguage
    ) {
        viewModelScope.launch {
            _mapsInsightsState.value = AiQueryUiState.Loading
            try {
                val result = aiRepository.findNearbyInsightsWithMapsGrounding(
                    latitude = latitude,
                    longitude = longitude,
                    city = city,
                    tradeLabel = tradeLabel,
                    language = language
                )
                result.onSuccess { answer ->
                    _mapsInsightsState.value = AiQueryUiState.Success(answer)
                }.onFailure { err ->
                    _mapsInsightsState.value = AiQueryUiState.Error(
                        err.localizedMessage ?: "Maps grounding failed."
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected Maps grounding error", e)
                _mapsInsightsState.value = AiQueryUiState.Error(
                    e.localizedMessage ?: "Maps grounding error."
                )
            }
        }
    }

    fun speakText(tag: String, text: String, language: AppLanguage) {
        val current = _ttsState.value
        if (current is TtsUiState.Playing && current.activeTag == tag) {
            stopAudio()
            return
        }

        viewModelScope.launch {
            audioPlayer.stop()
            _ttsState.value = TtsUiState.Generating(tag)
            try {
                val result = aiRepository.synthesizeSpeech(text, language)
                result.onSuccess { pcmBytes ->
                    _ttsState.value = TtsUiState.Playing(tag)
                    audioPlayer.playPcm24kHz(pcmBytes)
                    if (_ttsState.value is TtsUiState.Playing) {
                        _ttsState.value = TtsUiState.Idle
                    }
                }.onFailure { err ->
                    _ttsState.value = TtsUiState.Error(
                        err.localizedMessage ?: "Text-to-speech synthesis failed."
                    )
                }
            } catch (e: CancellationException) {
                audioPlayer.stop()
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected TTS playback error", e)
                _ttsState.value = TtsUiState.Error(
                    e.localizedMessage ?: "Voice playback failed."
                )
            }
        }
    }

    fun stopAudio() {
        audioPlayer.stop()
        _ttsState.value = TtsUiState.Idle
    }

    fun clearTtsError() {
        if (_ttsState.value is TtsUiState.Error) {
            _ttsState.value = TtsUiState.Idle
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
    }
}
