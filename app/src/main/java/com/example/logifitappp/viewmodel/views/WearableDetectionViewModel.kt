package com.example.logifitappp.viewmodel.views

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.BondingStyleEnum
import com.example.logifitappp.core.analyzers.ActivityAmount
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.bluetooth.ScanEvent
import com.example.logifitappp.core.bluetooth.ScanEventProcessor
import com.example.logifitappp.core.utils.BondingUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.core.wearebles.WearableCoordinator
import com.example.logifitappp.core.wearebles.WearableHelper
import java.util.GregorianCalendar

class WearableDetectionViewModel: ViewModel(), ScanEventProcessor.Callback {
    private var adapter: BluetoothAdapter? = null
    private val handler = Handler(Looper.getMainLooper())
    private var refreshAt = System.currentTimeMillis()
    private var scanCallback = BleScanCallback()
    private val scanEventProcessor = ScanEventProcessor(this)

    private val stopRunnable = Runnable {
        stopDiscovery()
        println("Discovery stopped by thread timeout.")
    }

    var authenticationKey by mutableStateOf(TextFieldValue(""))

    var candidates = mutableStateListOf<WearableCandidate>()
        private set

    var isBottomSheetVisible by mutableStateOf(false)

    var isScanning by mutableStateOf(false)
        private set

    var wearables = mutableStateListOf<Wearable>()
        private set

    var sleeps = mutableStateListOf<ActivityAmount>()
        private set

    private fun checkBluetoothAvailable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(App.context.applicationContext, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                println("No BLUETOOTH_SCAN permission")

                this.adapter = null
                return false
            }

            if (ActivityCompat.checkSelfPermission(App.context.applicationContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                println("No BLUETOOTH_CONNECT permission")

                this.adapter = null
                return false
            }
        }

        val bluetoothService = App.context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?

        if (bluetoothService == null) {
            println("No bluetooth service available")

            this.adapter = null
            return false
        }

        val adapter = bluetoothService.adapter

        if (adapter == null) {
            println("No bluetooth adapter available")

            this.adapter = null
            return false
        }

        if (!adapter.isEnabled) {
            println("Bluetooth not enabled")

            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            App.context.startActivity(intent)

            this.adapter = null
            return false
        }

        this.adapter = adapter
        return true
    }

    @RequiresPermission("android.permission.BLUETOOTH_SCAN")
    private fun ensureBluetoothReady(): Boolean {
        if (checkBluetoothAvailable()) {
            adapter?.cancelDiscovery()
            return true
        }

        return false
    }

    fun getCandidateByDevice(device: BluetoothDevice): WearableCandidate? {
        for (candidate in candidates) {
            if (candidate.getMacAddress() == device.address) return candidate
        }

        return null
    }

    private fun getPostMessage(runnable: Runnable): Message {
        val message = Message.obtain(handler, runnable)
        message.obj = runnable

        return message
    }

    fun getWantedPermissions(): List<String> {
        val permissions = arrayListOf<String>()

        if (ContextCompat.checkSelfPermission(App.context.applicationContext, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.BLUETOOTH)
        }

        if (ContextCompat.checkSelfPermission(App.context.applicationContext, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.BLUETOOTH_ADMIN)
        }

        if (ContextCompat.checkSelfPermission(App.context.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(App.context.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(App.context.applicationContext, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
                permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            }
            if (ActivityCompat.checkSelfPermission(App.context.applicationContext, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }

        return permissions
    }

    fun handleBluetoothStateChanged(state: Int) {
        if (state == BluetoothAdapter.STATE_ON) {
            val manager = App.context.getSystemService(BluetoothManager::class.java)
            adapter = manager.adapter
        } else adapter = null
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    fun handleCandidatePressed(candidate: WearableCandidate) {
        val wearableType = WearableHelper.getInstance().resolveWearableType(candidate)

        if (!wearableType.isSupported()) {
            println("Unsupported device candidate $candidate")
            return
        }

        stopDiscovery()

        val coordinator = wearableType.getWearableCoordinator()
        println("Using device candidate $candidate with coordinator ${coordinator::class.java}")

        if (coordinator.getBondingStyle() == BondingStyleEnum.BONDING_STYLE_REQUIRE_KEY) {
            val key = "2db23445563c8ba96ebe74a0dd1e4253"
            val sharedPrefers = App.getWearableSpecificSharedPrefs(candidate.getMacAddress())
            val editor = sharedPrefers?.edit()

            editor?.putString("authentication_key", key)
            editor?.apply()
        }

        if (coordinator.suggestUnbindBeforePair() && candidate.IsBonded()) {
            AlertDialog.Builder(App.context)
                .setTitle(R.string.unbind_before_pair_title)
                .setMessage(R.string.unbind_before_pair_message)
                .setPositiveButton(R.string.button_ok) { _, _ ->
                    startPair(candidate, coordinator)
                }
                .setNegativeButton(R.string.button_cancel, null)
                .show()
        } else startPair(candidate, coordinator)
    }

    fun handleWearableFound(event: ScanEvent) {
        scanEventProcessor.scheduleProcessing(event)
    }

    override fun onWearableChanged() {
        refreshWearableList(true)
    }

    fun refreshPairedWearables() {
        wearables.clear()
        wearables.addAll(App.wearableManager.getWearables())

        if (wearables.isNotEmpty()) {
            val now = GregorianCalendar.getInstance()
            val endTs = (now.timeInMillis / 1000).toInt()
            val startTs = endTs - 3 * 24 * 60 * 60 - 1

            val analyzer = ActivityAnalyzer()
            val activities = wearables[0].getWearableCoordinator().getActivityProvider(wearables[0]).getRawActivitiesBetween(startTs, endTs)

            sleeps.clear()
            sleeps.addAll(analyzer.calculateSleepAmounts(activities))
        }
    }

    fun refreshSingleWearable(wearable: Wearable) {
        val index = wearables.indexOf(wearable)

        if (index > 0) wearables[index].copyFromDevice(wearable)
        else refreshPairedWearables()
    }

    private fun refreshWearableList(throttle: Boolean) {
        handler.post {
            if (throttle && System.currentTimeMillis() - refreshAt < 1000L) return@post

            println("Refreshing device list")

            candidates.clear()
            candidates.addAll(scanEventProcessor.getWearables())

            refreshAt = System.currentTimeMillis()
        }
    }

    @RequiresPermission("android.permission.BLUETOOTH_SCAN")
    private fun startBluetoothDiscovery() {
        println("Starting BT discovery")

        try {
            stopBluetoothDiscovery()
        } catch (ignored: Exception) {
        }

        handler.removeMessages(0, stopRunnable)
        handler.sendMessageDelayed(getPostMessage(stopRunnable), SCAN_DURATION)

        if (adapter!!.startDiscovery()) println("Discovery started successfully")
        else println("Discovery starting failed")
    }

    @RequiresPermission("android.permission.BLUETOOTH_SCAN")
    private fun startBluetoothLEDiscovery() {
        println("Starting BLE discovery")

        handler.removeMessages(0, stopRunnable)
        handler.sendMessageDelayed(getPostMessage(stopRunnable), SCAN_DURATION)
        adapter!!.bluetoothLeScanner.startScan(null, ScanEventProcessor.getSettings(), scanCallback)

        println("Bluetooth LE discovery started successfully")
    }

    private fun startDiscovery(): Boolean {
        if (isScanning) {
            println("Not starting discovery, because already scanning.")
            return false
        }

        println("Starting discovery")

        scanEventProcessor.clear()
        scanEventProcessor.start()

        refreshWearableList(false)

        try {
            if (!ensureBluetoothReady()) {
                println("Disabled bluetooth")
                //TODO disabled bluetooth
                return false
            }

            if (App.supportsBluetoothLE()) startBluetoothLEDiscovery()

            startBluetoothDiscovery()
        } catch (e: SecurityException) {
            println("SecurityException on startDiscovery")

            scanEventProcessor.stop()
            return false
        }

        isScanning = true
        return true
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    private fun startPair(candidate: WearableCandidate, coordinator: WearableCoordinator) {
        when (coordinator.getBondingStyle()) {
            BondingStyleEnum.BONDING_STYLE_NONE,
            BondingStyleEnum.BONDING_STYLE_LAZY -> {
                println("No bonding needed, according to coordinator, so connecting right away")
                BondingUtils.connectThenComplete(candidate)
            }

            else -> {
                try {
                    BondingUtils.initiateCorrectBonding(candidate, coordinator)
                } catch (e: Exception) {
                    println("Error pairing device ${candidate.getMacAddress()}")
                }
            }
        }
    }

    @RequiresPermission("android.permission.BLUETOOTH_SCAN")
    private fun stopBluetoothDiscovery() {
        if (adapter == null) return

        adapter!!.cancelDiscovery()
        println("Stopped BT discovery")
    }

    @RequiresPermission("android.permission.BLUETOOTH_SCAN")
    private fun stopBluetoothLEDiscovery() {
        if (adapter == null) return

        val bluetoothScanner = adapter!!.bluetoothLeScanner

        if (bluetoothScanner == null) {
            println("Could not get BluetoothLeScanner()")
            return
        }

        try {
            bluetoothScanner.stopScan(scanCallback)
        } catch (e: NullPointerException) {
            println("Internal NullPointerException when stopping the scan!")
            return
        }

        println("Stopped BLE discovery")
    }

    fun stopDiscovery() {
        println("Stopping discovery")

        try {
            stopBluetoothDiscovery()
            stopBluetoothLEDiscovery()
        } catch (e: SecurityException) {
            println("SecurityException on stopDiscovery")
        }

        isScanning = false
        scanEventProcessor.stop()
        handler.removeMessages(0, stopRunnable)

        refreshWearableList(false)
    }

    fun toggleDiscovery() {
        if (isScanning) stopDiscovery()
        else startDiscovery()
    }

    companion object {
        const val SCAN_DURATION: Long = 30000
    }

    private class BleScanCallback: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
        }
    }
}