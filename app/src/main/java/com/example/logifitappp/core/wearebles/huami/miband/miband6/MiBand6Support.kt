package com.example.logifitappp.core.wearebles.huami.miband.miband6

import com.example.logifitappp.core.wearebles.huami.miband.miband5.MiBand5Support

class MiBand6Support: MiBand5Support() {
    override fun force2021Protocol() = true

    override fun getRawActivitySize() = 8
}