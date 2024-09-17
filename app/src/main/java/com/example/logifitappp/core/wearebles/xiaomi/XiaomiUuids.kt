package com.example.logifitappp.core.wearebles.xiaomi

import java.util.UUID

object XiaomiUuids {
    val bleUuids = linkedMapOf(
        UUID.fromString("0000fe95-0000-1000-8000-00805f9b34fb") to XiaomiBleUuidSet(
            true,
            UUID.fromString("00000051-0000-1000-8000-00805f9b34fb"),
            UUID.fromString("00000052-0000-1000-8000-00805f9b34fb"),
            UUID.fromString("00000053-0000-1000-8000-00805f9b34fb"),
            UUID.fromString("00000055-0000-1000-8000-00805f9b34fb")
        ),

        UUID.fromString("16186f00-0000-1000-8000-00807f9b34fb") to XiaomiBleUuidSet(
            false,
            UUID.fromString("16186f01-0000-1000-8000-00807f9b34fb"),
            UUID.fromString("16186f02-0000-1000-8000-00807f9b34fb"),
            UUID.fromString("16186f03-0000-1000-8000-00807f9b34fb"),
            UUID.fromString("16186f04-0000-1000-8000-00807f9b34fb")
        ),

        UUID.fromString("16187f00-0000-1000-8000-00807f9b34fb") to XiaomiBleUuidSet(
            false,
            UUID.fromString("16187f02-0000-1000-8000-00807f9b34fb"),
            UUID.fromString("16187f01-0000-1000-8000-00807f9b34fb"),
            UUID.fromString("16187f03-0000-1000-8000-00807f9b34fb"),
            UUID.fromString("16187f04-0000-1000-8000-00807f9b34fb")
        ),

        UUID.fromString("1314f000-1000-9000-7000-301291e21220") to XiaomiBleUuidSet(
            false,
            UUID.fromString("1314f005-1000-9000-7000-301291e21220"),
            UUID.fromString("1314f001-1000-9000-7000-301291e21220"),
            UUID.fromString("1314f002-1000-9000-7000-301291e21220"),
            UUID.fromString("1314f007-1000-9000-7000-301291e21220")
        ),

        UUID.fromString("7495fe00-a7f3-424b-92dd-4a006a3aef56") to XiaomiBleUuidSet(
            false, // FIXME check
            UUID.fromString("74950002-a7f3-424b-92dd-4a006a3aef56"),
            UUID.fromString("74950001-a7f3-424b-92dd-4a006a3aef56"),
            UUID.fromString("74950003-a7f3-424b-92dd-4a006a3aef56"),
            null
        )
    )

    class XiaomiBleUuidSet(
        val encrypted: Boolean,
        val characteristicCommandRead: UUID,
        val characteristicCommandWrite: UUID,
        val characteristicActivityData: UUID,
        val characteristicDataUpload: UUID?
    ) {

    }
}