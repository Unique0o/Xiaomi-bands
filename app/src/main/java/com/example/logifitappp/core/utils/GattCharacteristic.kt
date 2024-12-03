package com.example.logifitappp.core.utils

import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import java.util.UUID

object GattCharacteristic {
    val UUID_CHARACTERISTIC_BATTERY_LEVEL: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "2A19"))
    val UUID_CHARACTERISTIC_CURRENT_TIME: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "2A2B"))
    val UUID_CHARACTERISTIC_FIRMWARE_REVISION_STRING: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "2A26"))
    val UUID_CHARACTERISTIC_HARDWARE_REVISION_STRING: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "2A27"))
    val UUID_CHARACTERISTIC_SOFTWARE_REVISION_STRING: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "2A28"))
}