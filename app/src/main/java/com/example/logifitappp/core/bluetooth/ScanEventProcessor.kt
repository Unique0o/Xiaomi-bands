package com.example.logifitappp.core.bluetooth

import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.core.wearebles.WearableHelper
import java.util.Objects
import java.util.concurrent.LinkedBlockingQueue

class ScanEventProcessor(private val callback: Callback): Runnable {
    private val candidates = LinkedHashMap<String, WearableCandidate?>()
    private var isRunning = false
    private val processingMap = HashMap<String, ArrayList<ScanEvent>>()
    private val processingQueue = LinkedBlockingQueue<String?>()
    private var thread: Thread?= null

    fun clear() {
        candidates.clear()
        processingQueue.clear()
        processingMap.clear()
    }

    fun getWearables(): ArrayList<WearableCandidate> {
        val wearables = ArrayList<WearableCandidate>()

        synchronized(candidates) {
            for (entry in candidates.entries) entry.value?.let { wearables.add(it) }
        }

        return wearables
    }

    private fun processAllScanEvents(address: String): Boolean {
        val events: ArrayList<ScanEvent>?

        synchronized(processingMap) {
            events = processingMap.remove(address)
        }

        if (events.isNullOrEmpty()) {
            println("Attempted to process $address, but found no events")

            return false
        }

        println("Processing ${events.size} events for $address")

        var candidate = candidates[address]
        var previousName: String? = null
        var previousUuids: Array<ParcelUuid>? = null
        var firstTime = false

        if (candidate == null) {
            println("Found $address for the first time")

            val firstEvent = events.removeAt(0)

            firstTime = true
            candidate = WearableCandidate(firstEvent.getWearable(), firstEvent.getRssi(), firstEvent.getServices())
        } else {
            previousName = candidate.getName()
            previousUuids = candidate.getServices()
        }

        for (event in events) {
            candidate.setRssi(event.getRssi())
            candidate.addUuids(event.getServices())
        }

        candidate.refreshNameIfUnknown()

        try {
            candidate.addUuids(candidate.getDevice().uuids)
        } catch (e: SecurityException) {
            println("SecurityException on candidate.getDevice().getUuids()")
        }


        if (!firstTime && Objects.equals(candidate.getName(), previousName) && candidate.getServices().contentEquals(previousUuids)) {
            println("Not reprocessing $address due to no changes")
            return false
        }

        if (processCandidate(candidate)) {
            println("Device ${candidate.getDevice()} (${candidate.getName()}) is supported as '${WearableHelper.getInstance().resolveWearableType(candidate, false)}' without scanning services")

            return true
        }

        val candidateServices = candidate.getServices()

        if (candidateServices.isNullOrEmpty() || (candidateServices.size == 1 && candidateServices[0] == ZERO_UUID)) {
            println("Fetching uuids for ${candidate.getDevice().address} with sdp")

            try {
                candidate.getDevice().fetchUuidsWithSdp()
            } catch (e: SecurityException) {
                println("SecurityException on candidate.getDevice().fetchUuidsWithSdp()")
            }
        }

        return true
    }

    private fun processCandidate(candidate: WearableCandidate): Boolean {
        println("found device: ${candidate.getName()}, ${candidate.getMacAddress()}")

        val type = WearableHelper.getInstance().resolveWearableType(candidate, false)

        if (type.isSupported()) candidates[candidate.getMacAddress()] = candidate

        return type.isSupported()
    }

    override fun run() {
        println("Device found processor thread started")

        while (isRunning) {
            try {
                println("Polling found devices queue, current size = ${processingQueue.size}")

                val address = processingQueue.take()

                if (address != null && processAllScanEvents(address)) callback.onWearableChanged()
            } catch (e: InterruptedException) {
                println("Processing thread interrupted")
                Thread.currentThread().interrupt()

                break
            }
        }
    }

    fun scheduleProcessing(event: ScanEvent) {
        println("Scheduling ${event.getWearable().address} for processing (${event.getServices()})")

        val address = event.getWearable().address

        synchronized(processingMap) {
            if (!processingMap.containsKey(address)) processingMap[address] = arrayListOf()

            Objects.requireNonNull(processingMap[address])!!.add(event)
        }

        try {
            processingQueue.put(address)
        } catch (e: InterruptedException) {
            println("Failed to put device on processing queue $e")
        }
    }

    fun start() {
        if (isRunning) {
            println("Already running!")
            return
        }

        isRunning = true
        thread = object: Thread("Device Found Processor Thread") {
            override fun run() {
                this@ScanEventProcessor.run()
            }
        }

        thread!!.start()
    }

    fun stop() {
        isRunning = false

        if (thread == null) return

        thread!!.interrupt()
        thread = null
    }

    interface Callback {
        fun onWearableChanged()
    }

    companion object {
        private val ZERO_UUID = ParcelUuid.fromString("00000000-0000-0000-0000-000000000000")

        fun getSettings(): ScanSettings? {
            val builder = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            builder.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            builder.setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            builder.setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
            builder.setPhy(ScanSettings.PHY_LE_ALL_SUPPORTED)

            return builder.build()
        }
    }
}