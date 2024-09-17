package com.example.logifitappp.core.utils

import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.companion.AssociationRequest
import android.companion.BluetoothDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Context
import android.content.IntentSender
import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.BondingStyleEnum
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.core.wearebles.WearableCoordinator
import com.example.logifitappp.core.wearebles.WearableHelper

object BondingUtils {
    const val REQUEST_CODE = 1

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    private fun askCompanionPairing(candidate: WearableCandidate, macAddress: String) = AlertDialog.Builder(App.context)
        .setTitle(Resources.getSystem().getString(R.string.companion_pairing_request_title))
        .setMessage(Resources.getSystem().getString(R.string.companion_pairing_request_message))
        .setPositiveButton(Resources.getSystem().getString(R.string.button_ok)) { _, _ ->
            companionDeviceManagerBond(candidate, macAddress)
        }
        .setNegativeButton(Resources.getSystem().getString(R.string.button_cancel)) { _, _ ->
            bluetoothBond(candidate)
        }
        .show()

    private fun attemptToFirstConnect(candidate: WearableCandidate) {
        val mainLooper = Looper.getMainLooper()

        Handler(mainLooper).postDelayed({
            App.wearableService.disconnect()
            connectToWearable(WearableHelper.getInstance().toSupportedDevice(candidate))
        }, 10)
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    private fun bluetoothBond(candidate: WearableCandidate) {
        val device = candidate.getDevice()

        if (device.createBond()) {
            println("Bonding in progress...")
        } else {
            println("Bonding failed immediately! ${device.name} (${device.address}) ${device.type}")

            val bluetoothClass = device.bluetoothClass

            if (bluetoothClass != null) println("BluetoothClass: $bluetoothClass")

            when (device.bondState) {
                BluetoothDevice.BOND_BONDED -> {
                    println("For some reason the device is already bonded, but let's try first connect")
                    attemptToFirstConnect(candidate)
                }

                BluetoothDevice.BOND_BONDING -> println("Device is still bonding after an error")
                else -> println("Bonding failed immediately and no bond was made")
            }
        }
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    private fun companionDeviceManagerBond(candidate: WearableCandidate, macAddress: String) {
        val deviceFilter = BluetoothDeviceFilter.Builder()
            .setAddress(macAddress)
            .build()

        val pairingRequest = AssociationRequest.Builder()
            .addDeviceFilter(deviceFilter)
            .setSingleDevice(true)
            .build()

        val manager = App.context.getSystemService(Context.COMPANION_DEVICE_SERVICE) as CompanionDeviceManager
        println("Searching for $macAddress associations")

        for (association in manager.associations) {
            println("Already associated with: $association")

            if (association.equals(macAddress)) {
                println("The device has already been bonded through CompanionDeviceManager, using regular")

                bluetoothBond(candidate)
                return
            }
        }

        println("Starting association request")
        manager.associate(pairingRequest, getCompanionDeviceManagerCallback(), null)
    }

    fun connectThenComplete(candidate: WearableCandidate) = connectThenComplete(WearableHelper.getInstance().getSupportedWearable(candidate))

    private fun connectThenComplete(wearable: Wearable) {
        App.getWearableServiceTo(wearable).disconnect()
        App.getWearableServiceTo(wearable).connect(true)
    }

    private fun connectToWearable(wearable: Wearable) = App.getWearableServiceTo(wearable).connect(true)

    private fun getCompanionDeviceManagerCallback() = object: CompanionDeviceManager.Callback() {
        override fun onFailure(error: CharSequence?) = println("Bonding failed immediately: $error")

        @Deprecated("Deprecated in Java")
        override fun onDeviceFound(chooserLauncher: IntentSender) {
            try {
                val activity = App.context.findActivity() ?: return
                startIntentSenderForResult(activity, chooserLauncher, REQUEST_CODE, null, 0, 0, 0, null)
            } catch (e: IntentSender.SendIntentException) {
                println(e.toString())
            }
        }
    }


    fun handleDeviceBonded(candidate: WearableCandidate?) {
        if (candidate == null) {
            println("candidate was null! Can't handle bonded device!")

            return
        }

        connectThenComplete(candidate)
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    fun initiateCorrectBonding(candidate: WearableCandidate, coordinator: WearableCoordinator) {
        when (coordinator.getBondingStyle()) {
            BondingStyleEnum.BONDING_STYLE_NONE,
            BondingStyleEnum.BONDING_STYLE_LAZY -> return

            BondingStyleEnum.BONDING_STYLE_ASK -> {
                AlertDialog.Builder(App.context)
                    .setTitle(Resources.getSystem().getString(R.string.discovery_pair_title, candidate.getName()))
                    .setMessage(Resources.getSystem().getString(R.string.discovery_pair_message))
                    .setPositiveButton(Resources.getSystem().getString(R.string.button_ok)) { dialog, whichButton ->
                        tryBondThenComplete(candidate, candidate.getMacAddress())
                    }
                    .setNegativeButton(Resources.getSystem().getString(R.string.button_cancel)) { dialog, whichButton ->
                        connectThenComplete(candidate)
                    }
                    .show()
            }

            else -> tryBondThenComplete(candidate, candidate.getMacAddress())
        }

        println("Bonding initiated")
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    private fun tryBondThenComplete(candidate: WearableCandidate, macAddress: String) {
        val device = candidate.getDevice()

        when (device.bondState) {
            BluetoothDevice.BOND_BONDING -> {
                println("Bonding in progress: ${device.name} (${device.address})")
                return
            }

            BluetoothDevice.BOND_BONDED -> {
                println("Already bonded with ${device.name} (${device.address})")
                askCompanionPairing(candidate, macAddress)
            }

            else -> {
                println("Creating bond with ${device.name} (${device.address})")
                bluetoothBond(candidate)
            }
        }
    }
}