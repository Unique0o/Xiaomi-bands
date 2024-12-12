package com.example.logifitappp.core.builders.ble.profiles

import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.profiles.parcelables.NewAlert
import com.example.logifitappp.core.utils.BleTypeConversionsUtils
import com.example.logifitappp.core.utils.GattCharacteristic
import com.example.logifitappp.core.utils.StringUtils
import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import com.example.logifitappp.enums.AlertCategoryEnum
import com.example.logifitappp.enums.OverflowStrategyEnum
import okio.IOException
import java.io.ByteArrayOutputStream
import kotlin.math.min

class AlertNotificationProfile<T: AbstractBleWearableSupport>(support: T): AbstractBleProfile<T>(support) {
    var maxLength = 18

    private fun getAlertMessage(alert: NewAlert, message: String, chunk: Int): ByteArray {
        val stream = ByteArrayOutputStream(100)
        stream.write(BleTypeConversionsUtils.fromUint8(alert.categoryEnum.id).toInt())
        stream.write(BleTypeConversionsUtils.fromUint8(alert.numAlerts).toInt())

        if (alert.categoryEnum === AlertCategoryEnum.CUSTOM_HUAMI) {
            stream.write(BleTypeConversionsUtils.fromUint8(alert.customIcon.toInt()).toInt())
        }

        if (message.isNotEmpty()) {
            stream.write(BleTypeConversionsUtils.toUtf8s(message))
        }

        return stream.toByteArray()
    }

    fun newAlert(builder: TransactionBuilder, alert: NewAlert) {
        newAlert(builder, alert, OverflowStrategyEnum.TRUNCATE)
    }

    private fun newAlert(builder: TransactionBuilder, alert: NewAlert, strategy: OverflowStrategyEnum) {
        getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_NEW_ALERT)?.let {
            var message = alert.message ?: ""

            if (message.length > maxLength && strategy == OverflowStrategyEnum.TRUNCATE) {
                message = StringUtils.truncate(message, maxLength)
            }

            var numChunks = message.length / maxLength

            if (message.length % maxLength > 0) numChunks++

            try {
                var hasAlerted = false

                for (i in 0 ..< numChunks) {
                    val offset = i * maxLength
                    val restLength = message.length - offset

                    message = message.substring(offset, offset + min(maxLength, restLength))

                    if (hasAlerted && message.isEmpty()) break

                    builder.write(it, getAlertMessage(alert, message, 1))
                    hasAlerted = true
                }

                if (!hasAlerted) builder.write(it, getAlertMessage(alert, "", 1))
            } catch (e: IOException) {
                println("Error writing alert message to ByteArrayOutputStream")
            }
        }
    }
}