package com.example.logifitappp.core.utils

import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import java.util.UUID

object GattService {
    val UUID_SERVICE_ALERT_NOTIFICATION: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "1811"))
    val UUID_SERVICE_BATTERY_SERVICE: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "180F"))
    val UUID_SERVICE_DEVICE_INFORMATION: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "180A"))
    val UUID_SERVICE_GENERIC_ACCESS: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "1800"))
    val UUID_SERVICE_GENERIC_ATTRIBUTE: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "1801"))
    val UUID_SERVICE_HEART_RATE: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "180D"))
    val UUID_SERVICE_IMMEDIATE_ALERT: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "1802"))
}