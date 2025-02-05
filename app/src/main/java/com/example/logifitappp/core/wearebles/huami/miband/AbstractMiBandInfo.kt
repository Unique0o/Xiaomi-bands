package com.example.logifitappp.core.wearebles.huami.miband

open class AbstractMiBandInfo(payload: ByteArray) {
    protected val data = ByteArray(payload.size)

    init {
        System.arraycopy(payload, 0, data, 0, payload.size)
    }
}