package com.example.logifitappp.core.wearebles.huami.miband

import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import java.util.UUID

object MiBandService {
    val UUID_SERVICE_MIBAND_SERVICE: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "FEE0"))
    val UUID_SERVICE_MIBAND2_SERVICE: UUID = UUID.fromString(String.format(AbstractBleWearableSupport.BASE_UUID, "FEE1"))
}