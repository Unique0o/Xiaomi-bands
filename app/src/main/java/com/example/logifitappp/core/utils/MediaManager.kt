package com.example.logifitappp.core.utils

import android.media.MediaMetadata
import android.media.session.PlaybackState
import com.example.logifitappp.core.specs.MusicSpec
import com.example.logifitappp.core.specs.MusicStateSpec

class MediaManager {
    companion object {
        fun extractMusicSpec(d: MediaMetadata?): MusicSpec? {
            if (d == null) return null

            val musicSpec = MusicSpec()

            try {
                if (d.containsKey(MediaMetadata.METADATA_KEY_ARTIST)) {
                    musicSpec.artist = d.getString(MediaMetadata.METADATA_KEY_ARTIST)
                }

                if (d.containsKey(MediaMetadata.METADATA_KEY_ALBUM)) {
                    musicSpec.album = d.getString(MediaMetadata.METADATA_KEY_ALBUM)
                }

                if (d.containsKey(MediaMetadata.METADATA_KEY_TITLE)) {
                    musicSpec.track = d.getString(MediaMetadata.METADATA_KEY_TITLE)
                }

                if (d.containsKey(MediaMetadata.METADATA_KEY_DURATION)) {
                    musicSpec.duration = d.getLong(MediaMetadata.METADATA_KEY_DURATION).toInt() / 1000
                }

                if (d.containsKey(MediaMetadata.METADATA_KEY_NUM_TRACKS)) {
                    musicSpec.trackCount = d.getLong(MediaMetadata.METADATA_KEY_NUM_TRACKS).toInt()
                }

                if (d.containsKey(MediaMetadata.METADATA_KEY_TRACK_NUMBER)) {
                    musicSpec.trackNr = d.getLong(MediaMetadata.METADATA_KEY_TRACK_NUMBER).toInt()
                }
            } catch (e: Exception) {
                println("Failed to extract music spec $e")
            }

            return musicSpec
        }

        fun extractMusicStateSpec(s: PlaybackState?): MusicStateSpec? {
            if (s == null) return null

            val stateSpec = MusicStateSpec()

            try {
                stateSpec.position = (s.position / 1000).toInt()
                stateSpec.playRate = Math.round(100 * s.playbackSpeed)
                stateSpec.repeat = MusicStateSpec.STATE_UNKNOWN
                stateSpec.shuffle = MusicStateSpec.STATE_UNKNOWN

                when (s.state) {
                    PlaybackState.STATE_PLAYING -> stateSpec.state = MusicStateSpec.STATE_PLAYING
                    PlaybackState.STATE_STOPPED -> stateSpec.state = MusicStateSpec.STATE_STOPPED
                    PlaybackState.STATE_PAUSED -> stateSpec.state = MusicStateSpec.STATE_PAUSED
                    else -> stateSpec.state = MusicStateSpec.STATE_UNKNOWN
                }
            } catch (e: Exception) {
                println("Failed to extract music state spec $e")
            }

            return stateSpec
        }
    }
}