package com.example.logifitappp.core.wearebles.xiaomi.services

import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityFileFetcher
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityFileId
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSupport
import com.google.protobuf.ByteString
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto
import java.nio.ByteBuffer
import java.nio.ByteOrder

class XiaomiHealthService(support: XiaomiSupport) : AbstractXiaomiService(support) {
    private val activityFetcher = XiaomiActivityFileFetcher(this)

    fun ackRecordedData(fileId: XiaomiActivityFileId) {
        support.sendCommand(
            "ack recorded data",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_ACTIVITY_FETCH_ACK)
                .setHealth(XiaomiProto.Health.newBuilder().setActivitySyncAckFileIds(
                    ByteString.copyFrom(fileId.toBytes())
                ))
                .build()
        )
    }

    private fun fetchRecordedDataPast() {
        support.sendCommand(
            "fetch recorded data past",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_ACTIVITY_FETCH_PAST)
                .build()
        )
    }

    private fun fetchRecordedDataToday() {
        support.sendCommand(
            "fetch recorded data today",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_ACTIVITY_FETCH_TODAY)
                .setHealth(XiaomiProto.Health.newBuilder().setActivitySyncRequestToday(
                    XiaomiProto.ActivitySyncRequestToday.newBuilder().setUnknown1(0)
                ))
                .build()
        )
    }

    fun getActivityFetcher() = activityFetcher

    private fun handleActivityFetchResponse(subtype: Int, recordIds: ByteArray) {
        if (recordIds.size % 7 != 0) {
            println("recordIds ${recordIds.contentToString()} length = ${recordIds.size}, not a multiple of 7, can't parse")
            return
        }

        println("Got ${recordIds.size / 7} activity file IDs")

        val buffer = ByteBuffer.wrap(recordIds).order(ByteOrder.LITTLE_ENDIAN)
        val fileIds = mutableListOf<XiaomiActivityFileId>()

        while (buffer.position() < buffer.limit()) {
            val fileId = XiaomiActivityFileId.from(buffer)
            println("Got activity to fetch: $fileId")

            if (fileId.getTimestamp().time == 0.toLong() && fileId.getVersion() == 0) {
                println("Skipping invalid file with no timestamp and version")
                continue
            }

            fileIds.add(fileId)
        }

        activityFetcher.fetch(fileIds)

        if (subtype == CMD_ACTIVITY_FETCH_TODAY) {
            println("Fetch recorded data from the past")
            //fetchRecordedDataPast()
        }
    }

    override fun handleCommand(cmd: XiaomiProto.Command) {
        when (cmd.subtype) {
            CMD_ACTIVITY_FETCH_PAST,
            CMD_ACTIVITY_FETCH_TODAY -> handleActivityFetchResponse(cmd.subtype, cmd.health.activityRequestFileIds.toByteArray())
        }
    }

    fun onFetchRecordedData(dataTypes: Int) {
        println("Fetch recorded data: ${String.format("0x%08X", dataTypes)}")
        fetchRecordedDataToday()
    }

    fun requestRecordedData(fileId: XiaomiActivityFileId) {
        support.sendCommand(
            "request recorded data",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_ACTIVITY_FETCH_REQUEST)
                .setHealth(XiaomiProto.Health.newBuilder().setActivityRequestFileIds(
                    ByteString.copyFrom(fileId.toBytes())
                ))
                .build()
        )
    }

    companion object {
        private const val CMD_ACTIVITY_FETCH_ACK = 5
        private const val CMD_ACTIVITY_FETCH_PAST = 2
        private const val CMD_ACTIVITY_FETCH_TODAY = 1
        private const val CMD_ACTIVITY_FETCH_REQUEST = 3
        const val COMMAND_TYPE = 8
    }
}