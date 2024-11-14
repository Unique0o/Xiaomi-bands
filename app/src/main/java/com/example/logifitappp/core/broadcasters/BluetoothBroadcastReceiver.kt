package com.example.logifitappp.core.broadcasters

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.example.logifitappp.core.bluetooth.ScanEvent
import com.example.logifitappp.core.utils.AndroidUtils
import com.example.logifitappp.core.utils.BondingUtils
import com.example.logifitappp.core.utils.parcelableArrayExtra
import com.example.logifitappp.core.utils.parcelableExtra
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableCandidate
import java.util.Objects

abstract class BluetoothBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        when (Objects.requireNonNull(intent.action)) {
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> println("ACTION_DISCOVERY_STARTED")

            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                println("ACTION_STATE_CHANGED")
                handleStateChanged(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF))
            }

            BluetoothDevice.ACTION_FOUND -> {
                val device = intent.parcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                if (device == null) {
                    println("ACTION_FOUND with null device")
                    return
                }

                println("ACTION_FOUND ${device.address}")

                val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Wearable.RSSI_UNKNOWN)
                handleWearableFound(ScanEvent(device, rssi, null))
            }

            BluetoothDevice.ACTION_UUID -> {
                val device = intent.parcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                if (device == null) {
                    println("ACTION_UUID with null device")
                    return
                }

                println("ACTION_UUID ${device.address}")

                val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Wearable.RSSI_UNKNOWN)
                val uuids = intent.parcelableArrayExtra<Parcelable>(BluetoothDevice.EXTRA_UUID)
                val uuids2 = AndroidUtils.toParcelUuids(uuids)

                handleWearableFound(ScanEvent(device, rssi, uuids2))
            }

            BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                println("ACTION_BOND_STATE_CHANGED")

                val device = intent.parcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE) ?: return

                val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE)
                println("Bond state: $bondState")

                if (bondState == BluetoothDevice.BOND_BONDED) BondingUtils.handleDeviceBonded(getCandidateByDevice(device))
            }
        }
    }

    abstract fun getCandidateByDevice(device: BluetoothDevice): WearableCandidate?
    abstract fun handleStateChanged(state: Int)
    abstract fun handleWearableFound(event: ScanEvent)
}