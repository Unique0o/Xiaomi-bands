package com.example.logifitappp.core.wearebles

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.wearebles.huami.operations.AbstractFetchOperation
import com.example.logifitappp.core.wearebles.huami.HuamiFetchDataTypeEnum
import com.example.logifitappp.core.wearebles.huami.HuamiSupport

abstract class AbstractRepeatingFetchOperation(
    support: HuamiSupport,
    private val dataType: HuamiFetchDataTypeEnum
): AbstractFetchOperation(support) {
    init {
        setName("fetching ${dataType.name}")
    }

    private fun needsAnotherFetch(lastSyncTimestamp: GregorianCalendar): Boolean {
        val lastFetchRange = lastSyncTimestamp.timeInMillis - startTimestamp!!.timeInMillis

        if (lastFetchRange < 1000L) {
            return false.also { println("Fetch round $fetchCount of ${getName()} got $lastFetchRange ms of data, stopping to avoid infinite loop") }
        }

        if (fetchCount > 5) {
            return false.also { println("Already have $fetchCount fetch rounds for ${getName()}, not doing another one") }
        }

        if (lastSyncTimestamp.timeInMillis >= System.currentTimeMillis()) {
            return false.also { println("Not doing another fetch since last synced timestamp is in the future: ${lastSyncTimestamp.time}") }
        }

        return true.also { println("Doing another fetch since last sync timestamp is still too old: ${lastSyncTimestamp.time}") }
    }

    override fun processBufferedData(): Boolean {
        println("${getName()} has finished round $fetchCount, got ${buffer.size()} bytes in buffer")

        if (buffer.size() == 0) return true

        val bytes = buffer.toByteArray()
        val timestamp = this.startTimestamp!!.clone() as GregorianCalendar

        val handleSuccess = handleActivityData(timestamp, bytes)

        if (!handleSuccess) return false

        timestamp.add(Calendar.MINUTE, 1)
        saveLastSyncTimestamp(timestamp)

        if (needsAnotherFetch(timestamp)) {
            buffer.reset()
            getSupport().getFetchOperationQueue().add(0, this)
        }

        return true
    }

    override fun startFetching(builder: TransactionBuilder) {
        val sinceWhen = getLastSuccessfulSyncTime()
        println("start ${getName()} since ${sinceWhen.time}")
        startFetching(builder, dataType.code, sinceWhen)
    }

    abstract fun handleActivityData(timestamp: GregorianCalendar, bytes: ByteArray): Boolean
}