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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.BondingStyleEnum
import com.example.logifitappp.core.bluetooth.ScanEvent
import com.example.logifitappp.core.bluetooth.ScanEventProcessor
import com.example.logifitappp.core.utils.BondingUtils
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.core.wearebles.WearableCoordinator
import com.example.logifitappp.core.wearebles.WearableHelper
import com.example.logifitappp.core.wearebles.WearableSettingPreferenceConstants
import com.example.logifitappp.domain.service.WearableService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.viewmodel.states.WearableDetectionState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = WearableDetectionViewModel.WearableDetectionViewModelFactory::class)
class WearableDetectionViewModel @AssistedInject constructor(
    @Assisted private val navigation: NavHostController,
    private val wearableService: WearableService
): ViewModel(), ScanEventProcessor.Callback {
    @AssistedFactory
    interface WearableDetectionViewModelFactory {
        fun create(navigation: NavHostController): WearableDetectionViewModel
    }

    private var adapter: BluetoothAdapter? = null
    private val handler = Handler(Looper.getMainLooper())
    private var refreshAt = System.currentTimeMillis()
    private var scanCallback = BleScanCallback()
    private val scanEventProcessor = ScanEventProcessor(this)

    private val stopRunnable = Runnable {
        stopDiscovery()
        println("Discovery stopped by thread timeout.")
    }

    var candidates = mutableStateListOf<WearableCandidate>()
        private set

    var state by mutableStateOf(WearableDetectionState())
        private set

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    fun authenticate() {
        val wearableType = WearableHelper.getInstance().resolveWearableType(state.currentCandidate!!)

        if (!wearableType.isSupported()) {
            println("Unsupported device candidate ${state.currentCandidate}")
            return
        }

        state = state.copy(currentPage = 1)
        stopDiscovery()

        val coordinator = wearableType.getWearableCoordinator()
        println("Using device candidate ${state.currentCandidate} with coordinator ${coordinator::class.java}")

        if (coordinator.getBondingStyle() == BondingStyleEnum.BONDING_STYLE_REQUIRE_KEY) {
            val key = state.authenticationKey.text

            App.getWearableSpecificSharedPrefs(state.currentCandidate!!.getMacAddress())?.edit()?.let {
                it.putString("authentication_key", key)
                it.apply()
            }
        }

        if (coordinator.suggestUnbindBeforePair() && state.currentCandidate!!.IsBonded()) {
            AlertDialog.Builder(App.context)
                .setTitle(R.string.unbind_before_pair_title)
                .setMessage(R.string.unbind_before_pair_message)
                .setPositiveButton(R.string.button_ok) { _, _ ->
                    startPair(state.currentCandidate!!, coordinator)
                }
                .setNegativeButton(R.string.button_cancel, null)
                .show()
        } else startPair(state.currentCandidate!!, coordinator)
    }

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

    fun checkWearableConnection() {
        val wearables = App.wearableManager.getWearables()

        wearables.firstOrNull()?.let {
            if (it.isInitialized()) {
                App.getWearablePreferences(it.getAddress()!!).getPreferences()
                    .edit()
                    .putBoolean(WearableSettingPreferenceConstants.PREF_FIRST_CONNECTION, true)
                    .apply()

                navigation.navigate(MainRoutes.SplashScreen)
            }
        }
    }

    fun closeBottomSheet() {
        state = state.copy(isBottomSheetVisible = false)
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

    fun handleAuthenticationKeyFailed() {
        state = state.copy(
            currentPage = 0,
            status = AppStatusCodeEnum.INVALID_WEARABLE_AUTHENTICATION_KEY
        )
    }

    fun handleBluetoothStateChanged(state: Int) {
        if (state == BluetoothAdapter.STATE_ON) {
            val manager = App.context.getSystemService(BluetoothManager::class.java)
            adapter = manager.adapter
        } else adapter = null
    }

    fun handleCandidatePressed(candidate: WearableCandidate) {
        viewModelScope.launch {
            try {
                state = state.copy(authenticationKey = TextFieldValue(wearableService.fetchAuthenticationKey(candidate.getMacAddress()) ?: ""))
            } catch (e: Exception) {
                state = state.copy(authenticationKey = TextFieldValue(""))
            } finally {
                state = state.copy(currentCandidate = candidate, isBottomSheetVisible = true)
            }
        }
    }

    fun handleWearableFound(event: ScanEvent) {
        scanEventProcessor.scheduleProcessing(event)
    }

    override fun onWearableChanged() {
        refreshWearableList(true)
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
        if (state.isScanning) {
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

        state = state.copy(isScanning = true)
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

        state = state.copy(isScanning = false)
        scanEventProcessor.stop()
        handler.removeMessages(0, stopRunnable)

        refreshWearableList(false)
    }

    fun stopProcessing() {
        state = state.copy(status = null)
    }

    fun toggleDiscovery() {
        if (state.isScanning) stopDiscovery()
        else startDiscovery()
    }

    fun updateAuthenticationKey(value: TextFieldValue) {
        state = state.copy(authenticationKey = value)
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