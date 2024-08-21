package com.example.logifitappp.core.wearebles.xiaomi.miband8

import com.example.logifitappp.core.wearebles.xiaomi.XiaomiCoordinator
import java.util.regex.Pattern

class MiBand8Coordinator: XiaomiCoordinator() {
    override fun getSupportedDeviceName(): Pattern = Pattern.compile("^Xiaomi Smart Band 8 [A-Z0-9]{4}$")
}