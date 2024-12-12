package com.example.logifitappp.core.specs

class MusicStateSpec(old: MusicStateSpec? = null) {
    var playRate = STATE_UNKNOWN
    var position = STATE_UNKNOWN
    var repeat = STATE_UNKNOWN
    var shuffle = STATE_UNKNOWN
    var state = STATE_UNKNOWN

    init {
        old?.let {
            playRate = it.playRate
            position = it.position
            repeat = it.repeat
            shuffle = it.shuffle
            state = it.state
        }
    }

    companion object {
        const val STATE_PAUSED = 1
        const val STATE_PLAYING = 0
        const val STATE_SHUFFLE_ENABLED = 1
        const val STATE_STOPPED = 2
        const val STATE_UNKNOWN = -1
    }
}