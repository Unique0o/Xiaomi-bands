package com.example.logifitappp.core.wearebles.xiaomi.miband9

import com.example.logifitappp.core.bluetooth.ConnectionTypeEnum
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiCoordinator
import java.util.regex.Pattern

class MiBand9Coordinator: XiaomiCoordinator() {
    override fun getConnectionType() = ConnectionTypeEnum.BT_CLASSIC

    override fun getSupportedWearableName(): Pattern = Pattern.compile("^Xiaomi Smart Band 9 [0-9A-F]{4}$")
}