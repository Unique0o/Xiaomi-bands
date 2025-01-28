package com.example.logifitappp.core.wearebles.huami.operations

import android.icu.util.GregorianCalendar
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.BleTypeConversionsUtils
import com.example.logifitappp.core.wearebles.AbstractRepeatingFetchOperation
import com.example.logifitappp.core.wearebles.huami.HuamiFetchDataTypeEnum
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.data.models.HuamiStressSampleModel
import com.example.logifitappp.enums.StressTypeEnum
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FetchStressManualOperation(support: HuamiSupport): AbstractRepeatingFetchOperation(support, HuamiFetchDataTypeEnum.STRESS_MANUAL) {
    override fun getLastSyncTimeKey() = "lastStressManualTimeMillis"

    override fun handleActivityData(timestamp: GregorianCalendar, bytes: ByteArray): Boolean {
        if (bytes.size % 5 != 0) return false.also { println("Unexpected buffered stress data size ${bytes.size} is not a multiple of 5") }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        val lastSyncTimestamp = GregorianCalendar()

        val samples = mutableListOf<HuamiStressSampleModel>()

        while (buffer.position() < bytes.size) {
            val currentTimestamp = BleTypeConversionsUtils.toUnsigned(buffer.getInt()) * 1000
            val stress = buffer.get().toInt() and 0xff

            timestamp.timeInMillis = currentTimestamp
            println("Stress (manual) at ${lastSyncTimestamp.time}: $stress")

            samples.add(HuamiStressSampleModel(
                stress = stress,
                timestamp = timestamp.timeInMillis,
                typeNum = StressTypeEnum.MANUAL.num
            ))
        }

        return persistSamples(samples)
    }

    protected fun persistSamples(samples: List<HuamiStressSampleModel>): Boolean {
        try {
            getSupport()
                .getCoordinator()
                .getStressSampleProvider(wearable)
                .store(*samples.toTypedArray())
        } catch (e: Exception) {
            return false.also { println("Error saving auto stress samples: $e") }
        }

        return true
    }

    override fun taskDescription() = context.getString(R.string.busy_task_fetch_stress_data)
}