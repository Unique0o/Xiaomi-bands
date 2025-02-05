package com.example.logifitappp.core.wearebles.huami.miband.miband3

import com.example.logifitappp.core.wearebles.huami.miband.amazfitbip.AmazfitBipSupport

open class MiBand3Support: AmazfitBipSupport() {
    override fun getAuthFlags() = 0x00.toByte()
}