package com.example.logifitappp.core.wearebles.huami.operations

import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.core.wearebles.huami.miband.AbstractMiBandOperation

abstract class AbstractHuamiOperation(huamiSupport: HuamiSupport): AbstractMiBandOperation<HuamiSupport>(huamiSupport) {
    override fun enableOtherNotifications(builder: TransactionBuilder, enable: Boolean) {
        // TODO: check which notifications we should disable and re-enable here
    }
}