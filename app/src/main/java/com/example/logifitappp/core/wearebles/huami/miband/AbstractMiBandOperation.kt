package com.example.logifitappp.core.wearebles.huami.miband

import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.wearebles.AbstractBleOperation
import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import com.example.logifitappp.enums.OperationStatusEnum
import okio.IOException

abstract class AbstractMiBandOperation<T: AbstractBleWearableSupport>(support: T): AbstractBleOperation<T>(support) {
    private fun handleFinished(builder: TransactionBuilder) {
        enableNeededNotifications(builder, false)
        enableOtherNotifications(builder, true)
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun operationFinished() {
        operationStatus = OperationStatusEnum.FINISHED

        if (wearable.isConnected()) {
            unsetBusy()

            try {
                val builder = performInitialized("reenabling disabled notifications")
                handleFinished(builder)
                builder.setCallback(null)
                queue?.let { builder.queue(it) }
            } catch (e: IOException) {
                println("Error enabling Mi Band notifications, you may need to connect and disconnect $e")
            }
        }
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun prePerform() {
        super.prePerform()
        wearable.setBusyTask("Operation starting...")

        val builder = performInitialized("disabling some notifications")
        enableOtherNotifications(builder, false)
        enableNeededNotifications(builder, true)
        queue?.let { builder.queue(it) }
    }

    abstract fun enableNeededNotifications(builder: TransactionBuilder, enable: Boolean)
    abstract fun enableOtherNotifications(builder: TransactionBuilder, enable: Boolean)
}