package com.example.logifitappp.viewmodel.views

import android.icu.util.GregorianCalendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.StoreAudioRequest
import com.example.logifitappp.domain.service.AudioService
import com.example.logifitappp.enums.AudioFileEnum
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MeditationViewModel.MeditationViewModelFactory::class)
class MeditationViewModel @AssistedInject constructor(
    @Assisted private val user: UserModel?,
    private val audioService: AudioService
): ViewModel() {
    @AssistedFactory
    interface MeditationViewModelFactory {
        fun create(user: UserModel?): MeditationViewModel
    }

    var currentAudio by mutableStateOf<AudioFileEnum?>(null)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    var progress by mutableFloatStateOf(0f)
        private set

    var exoPlayer: ExoPlayer? = ExoPlayer.Builder(App.context).build().apply {
        addListener(object: Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                this@MeditationViewModel.isPlaying = isPlaying
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) endUpAudio()
            }
        })
    }

    init {
        trackProgress()
    }

    fun endUpAudio() {
        viewModelScope.launch {
            val type = currentAudio?.code

            exoPlayer?.release()
            currentAudio = null

            if (user == null || type == null) return@launch

            try {
                audioService.store(user.id, StoreAudioRequest(
                    date = DateTimeUtils.format(GregorianCalendar.getInstance().time, "yyyy-MM-dd"),
                    type_audio = type
                ))
            } catch (e: Exception) {
                //TODO: when store audio fails
            }
        }
    }

    override fun onCleared() {
        exoPlayer?.release()
        exoPlayer = null
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun play(audio: AudioFileEnum) {
        if (currentAudio == audio) {
            if (exoPlayer?.isPlaying == true) exoPlayer?.pause()
            else exoPlayer?.play()
        } else {
            exoPlayer?.apply {
                setMediaItem(MediaItem.fromUri(audio.geLocalAudioUri()))
                prepare()
                playWhenReady = true
            }

            currentAudio = audio
        }
    }

    private fun trackProgress() {
        viewModelScope.launch {
            while (true) {
                exoPlayer?.currentPosition?.let { currentPosition ->
                    exoPlayer?.duration?.let { duration ->
                        if (duration > 0) progress = currentPosition.toFloat() / duration
                    }
                }

                delay(500)
            }
        }
    }
}