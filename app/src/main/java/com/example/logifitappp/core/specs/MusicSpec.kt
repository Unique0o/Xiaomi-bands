package com.example.logifitappp.core.specs

class MusicSpec(old: MusicSpec? = null) {
    var album: String? = null
    var artist: String? = null
    var duration = MUSIC_UNKNOWN
    var track: String? = null
    var trackCount = MUSIC_UNKNOWN
    var trackNr = MUSIC_UNKNOWN

    init {
        old?.let {
            album = old.album
            artist = old.artist
            duration = old.duration
            track = old.track
            trackCount = old.trackCount
            trackNr = old.trackNr
        }
    }

    companion object {
        const val MUSIC_UNKNOWN = -1
    }
}