package com.example.logifitappp.core.utils

import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import java.util.UUID

object GattDescriptor {
    val UUID_DESCRIPTOR_GATT_CLIENT_CHARACTERISTIC_CONFIGURATION: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "2902"))
}