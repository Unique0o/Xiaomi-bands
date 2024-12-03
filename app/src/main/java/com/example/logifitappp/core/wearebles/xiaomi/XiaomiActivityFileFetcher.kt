package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.BleTypeConversionsUtils
import com.example.logifitappp.core.utils.CheckSumUtils
import com.example.logifitappp.core.wearebles.xiaomi.parsers.XiaomiActivityParser
import com.example.logifitappp.core.wearebles.xiaomi.services.XiaomiHealthService
import org.bouncycastle.util.Arrays
import java.io.ByteArrayOutputStream
import java.util.PriorityQueue

class XiaomiActivityFileFetcher(private val healthService: XiaomiHealthService) {
    private var buffer = ByteArrayOutputStream()
    private val fetchQueue = PriorityQueue<XiaomiActivityFileId>()
    private var isFetching = false

    fun addChunk(payload: ByteArray) {
        val total = BleTypeConversionsUtils.toUint16(payload, 0)
        val num = BleTypeConversionsUtils.toUint16(payload, 2)

        println("Got activity chunk $num / $total")

        buffer.write(payload, 4, payload.size - 4)

        if (num != total) return

        val data = buffer.toByteArray()
        buffer = ByteArrayOutputStream()

        if (data.size  < 13) {
            println("Activity data length of ${data.size} is too short")
            triggerNextFetch()
            return
        }

        val arrCrc32 = CheckSumUtils.getCRC32(data, 0, data.size - 4)
        val expectedCrc32 = BleTypeConversionsUtils.toUint32(data, data.size - 4)

        if (arrCrc32 != expectedCrc32) {
            println("Invalid activity data checksum: got ${String.format("%08X", arrCrc32)}, expected ${String.format("%08X", expectedCrc32)}")
            triggerNextFetch()
            return
        }

        if (data[7] != 0.toByte()) {
            println("Unexpected activity payload byte ${String.format("0x%02X", data[7])} at position 7 - parsing might fail")
        }

        val fileIdBytes = Arrays.copyOfRange(data, 0, 7)
        val fileId = XiaomiActivityFileId.from(fileIdBytes)

        if (!XiaomiPreferences.keepActivityDataOnDevice(healthService.support.getWearable())) {
            println("Acking recorded data $fileId")
            healthService.ackRecordedData(fileId)
        }

        val activityParser = XiaomiActivityParser.create(fileId)

        if (activityParser == null) {
            println("Failed to find parser for $fileId")
            triggerNextFetch()
            return
        }

        try {
            if (activityParser.parse(healthService.support, fileId, data)) println("Successfully parsed $fileId")
            else println("Failed to parse $fileId")
        } catch (e: Exception) {
            println("Exception while parsing $fileId")
        }

        triggerNextFetch()
    }

    fun fetch(fileIds: List<XiaomiActivityFileId>) {
        fetchQueue.addAll(fileIds)

        if (isFetching) return

        isFetching = true

        val support = healthService.support
        val context = support.getContext()

        support.getWearable().apply {
            setBusyTask(context.getString(R.string.busy_task_fetch_activity_data))
            sendDeviceUpdateIntent(context)
        }

        triggerNextFetch()
    }

    private fun triggerNextFetch() {
        val fileId = fetchQueue.poll()

        if (fileId == null) {
            println("Nothing more to fetch")
            isFetching = false
            healthService.support.getWearable().unsetBusyTask()
            App.signalFetchingActivityDataFinish(healthService.support.getWearable())
            healthService.support.getWearable().sendDeviceUpdateIntent(healthService.support.getContext())
            return
        }

        println("Triggering next fetch for: $fileId")
        healthService.requestRecordedData(fileId)
    }
}