package com.example.logifitappp.core.wearebles.huami.operations

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.R
import com.example.logifitappp.core.wearebles.AbstractRepeatingFetchOperation
import com.example.logifitappp.core.wearebles.huami.HuamiFetchDataTypeEnum
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.data.models.HuamiStressSampleModel
import com.example.logifitappp.enums.StressTypeEnum

class FetchStressAutoOperation(support: HuamiSupport): AbstractRepeatingFetchOperation(support, HuamiFetchDataTypeEnum.STRESS_AUTOMATIC) {
    override fun getLastSyncTimeKey() = "lastStressAutoTimeMillis"

    override fun handleActivityData(timestamp: GregorianCalendar, bytes: ByteArray): Boolean {
        val samples = mutableListOf<HuamiStressSampleModel>()

        for (byte in bytes) {
            if (byte == (-1).toByte()) {
                timestamp.add(Calendar.MINUTE, 1)
                continue
            }

            val stress = byte.toInt() and 0xff
            println("Stress (auto) at ${timestamp.time}: $stress")

            samples.add(HuamiStressSampleModel(
                stress = stress,
                timestamp = timestamp.timeInMillis,
                typeNum = StressTypeEnum.AUTOMATIC.num
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