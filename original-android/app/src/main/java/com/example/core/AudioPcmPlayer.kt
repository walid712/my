package com.example.core

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class AudioPcmPlayer {
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    @Volatile
    private var currentTrack: AudioTrack? = null

    suspend fun playPcm24kHz(pcmBytes: ByteArray) = withContext(Dispatchers.IO) {
        stop()
        if (pcmBytes.isEmpty()) return@withContext
        val audioBytes = extractPcmPayload(pcmBytes)
        if (audioBytes.isEmpty()) return@withContext

        val sampleRate = 24_000
        val minBuf = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ).coerceAtLeast(4096)

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(audioBytes.size.coerceAtLeast(minBuf))
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        currentTrack = track
        try {
            val written = track.write(audioBytes, 0, audioBytes.size)
            if (written > 0) {
                _isPlaying.value = true
                track.play()
                val durationMs = (audioBytes.size / (sampleRate * 2.0) * 1000.0).toLong().coerceAtLeast(300L)
                val start = System.currentTimeMillis()
                while (_isPlaying.value && System.currentTimeMillis() - start < durationMs + 250L) {
                    Thread.sleep(100)
                }
            }
        } finally {
            _isPlaying.value = false
            try {
                track.stop()
            } catch (_: Exception) {
            }
            track.release()
            if (currentTrack === track) {
                currentTrack = null
            }
        }
    }

    /**
     * Firebase Gemini TTS returns a WAV container. AudioTrack expects raw PCM,
     * so locate and return the data chunk. Raw PCM from older SDK responses is
     * still accepted as a compatibility fallback.
     */
    private fun extractPcmPayload(bytes: ByteArray): ByteArray {
        fun ascii(offset: Int, value: String): Boolean {
            if (offset < 0 || offset + value.length > bytes.size) return false
            return value.indices.all { bytes[offset + it].toInt().toChar() == value[it] }
        }
        if (bytes.size < 12 || !ascii(0, "RIFF") || !ascii(8, "WAVE")) return bytes

        var offset = 12
        var channels = 0
        var sampleRate = 0
        var bitsPerSample = 0
        var audioFormat = 0
        while (offset + 8 <= bytes.size) {
            val chunkSize = (bytes[offset + 4].toInt() and 0xff) or
                ((bytes[offset + 5].toInt() and 0xff) shl 8) or
                ((bytes[offset + 6].toInt() and 0xff) shl 16) or
                ((bytes[offset + 7].toInt() and 0xff) shl 24)
            val dataStart = offset + 8
            if (chunkSize < 0 || dataStart + chunkSize > bytes.size) return byteArrayOf()
            when {
                ascii(offset, "fmt ") && chunkSize >= 16 -> {
                    audioFormat = (bytes[dataStart].toInt() and 0xff) or
                        ((bytes[dataStart + 1].toInt() and 0xff) shl 8)
                    channels = (bytes[dataStart + 2].toInt() and 0xff) or
                        ((bytes[dataStart + 3].toInt() and 0xff) shl 8)
                    sampleRate = (bytes[dataStart + 4].toInt() and 0xff) or
                        ((bytes[dataStart + 5].toInt() and 0xff) shl 8) or
                        ((bytes[dataStart + 6].toInt() and 0xff) shl 16) or
                        ((bytes[dataStart + 7].toInt() and 0xff) shl 24)
                    bitsPerSample = (bytes[dataStart + 14].toInt() and 0xff) or
                        ((bytes[dataStart + 15].toInt() and 0xff) shl 8)
                }
                ascii(offset, "data") -> {
                    if (audioFormat != 1 || channels != 1 || sampleRate != 24_000 || bitsPerSample != 16) {
                        return byteArrayOf()
                    }
                    return bytes.copyOfRange(dataStart, dataStart + chunkSize)
                }
            }
            offset = dataStart + chunkSize + (chunkSize and 1)
        }
        return byteArrayOf()
    }

    fun stop() {
        _isPlaying.value = false
        currentTrack?.let { track ->
            try {
                track.stop()
            } catch (_: Exception) {
            }
            try {
                track.release()
            } catch (_: Exception) {
            }
        }
        currentTrack = null
    }
}
