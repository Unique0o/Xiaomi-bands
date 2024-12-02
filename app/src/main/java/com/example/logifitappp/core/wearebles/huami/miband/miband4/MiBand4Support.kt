package com.example.logifitappp.core.wearebles.huami.miband.miband4

import com.example.logifitappp.core.wearebles.huami.miband.miband3.MiBand3Support

open class MiBand4Support: MiBand3Support() {
    override fun getCryptFlags() = 0x80.toByte()
}