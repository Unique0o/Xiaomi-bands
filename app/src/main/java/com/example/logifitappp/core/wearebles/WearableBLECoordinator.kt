package com.example.logifitappp.core.wearebles

import com.example.logifitappp.core.bluetooth.ConnectionTypeEnum

abstract class WearableBLECoordinator: WearableCoordinator() {
    override fun getConnectionType(): ConnectionTypeEnum {
        return ConnectionTypeEnum.BLE
    }
}