package com.example.logifitappp.core.wearebles.xiaomi

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.App
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.PlainAction
import com.example.logifitappp.core.builders.ble.actions.SetWearableStateAction
import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.enums.AppStatusCodeEnum
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto

class XiaomiBleConnectionSupport(private val xiaomiSupport: XiaomiSupport): XiaomiConnectionSupport() {
    private var characteristicActivityData: XiaomiCharacteristic? = null
    private var characteristicCommandRead: XiaomiCharacteristic? = null
    private var characteristicCommandWrite: XiaomiCharacteristic? = null
    private var characteristicDataUpload: XiaomiCharacteristic? = null

    private val commsSupport = object: AbstractBleWearableSupport() {
        @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
        override fun disconnect() {
            xiaomiSupport.onDisconnect()
            super.disconnect()
        }

        override fun getAutoReconnect() = xiaomiSupport.getAutoReconnect()

        override fun getSupportedServices() = XiaomiUuids.bleUuids.keys

        private fun getCharacteristicsSet(): XiaomiUuids.XiaomiBleUuidSet? {
            for ((key, value) in XiaomiUuids.bleUuids) {
                if (getCharacteristic(value.characteristicCommandRead) == null) continue

                if (getCharacteristic(value.characteristicCommandWrite) == null) continue

                if (getCharacteristic(value.characteristicActivityData) == null) continue

                if (value.characteristicDataUpload == null || getCharacteristic(value.characteristicDataUpload) == null) {
                    println("btCharacteristicDataUpload characteristic is null")
                }

                println("Found Xiaomi service: $key")
                return value
            }

            return null
        }

        override fun initializeDevice(builder: TransactionBuilder): TransactionBuilder {
            val uuidSet = getCharacteristicsSet()

            if (uuidSet == null) {
                App.signalFailedConnectionWithWearable(AppStatusCodeEnum.UNSUPPORTED_WEARABLE)

                println("Failed to find known Xiaomi service")
                builder.add(SetWearableStateAction(getWearable(), Wearable.State.NOT_CONNECTED, getContext()))
                return builder
            }

            if (getWearable().getFirmwareVersion() == null) {
                getWearable().setFirmwareVersion(xiaomiSupport.getCachedFirmwareVersion() ?: "N/A")
            }

            val expectedMtu = 247
            val characteristicRead = getCharacteristic(uuidSet.characteristicCommandRead)!!
            val characteristicWrite = getCharacteristic(uuidSet.characteristicCommandWrite)!!
            val mCharacteristicActivityData = getCharacteristic(uuidSet.characteristicActivityData)!!
            val mCharacteristicDataUpload = getCharacteristic(uuidSet.characteristicDataUpload)

            characteristicCommandRead = XiaomiCharacteristic(this@XiaomiBleConnectionSupport, characteristicRead, xiaomiSupport.getAuthService())
            characteristicCommandRead?.setIsEncrypted(uuidSet.encrypted)
            characteristicCommandRead?.setHandler(object: XiaomiChannelHandler {
                override fun handle(payload: ByteArray) = xiaomiSupport.handleCommandBytes(payload)
            })
            characteristicCommandRead?.setMtu(expectedMtu)

            characteristicCommandWrite = XiaomiCharacteristic(this@XiaomiBleConnectionSupport, characteristicWrite, xiaomiSupport.getAuthService())
            characteristicCommandWrite?.setIsEncrypted(uuidSet.encrypted)
            characteristicCommandWrite?.setMtu(expectedMtu)

            characteristicActivityData = XiaomiCharacteristic(this@XiaomiBleConnectionSupport, mCharacteristicActivityData, xiaomiSupport.getAuthService())
            characteristicActivityData?.setHandler(object: XiaomiChannelHandler {
                override fun handle(payload: ByteArray) = xiaomiSupport.getHealthService().getActivityFetcher().addChunk(payload)
            })
            characteristicActivityData?.setIsEncrypted(uuidSet.encrypted)
            characteristicActivityData?.setMtu(expectedMtu)

            mCharacteristicDataUpload?.let {
                characteristicDataUpload = XiaomiCharacteristic(this@XiaomiBleConnectionSupport, it, xiaomiSupport.getAuthService())
                characteristicDataUpload?.setIsEncrypted(uuidSet.encrypted)
                characteristicDataUpload?.setIncrementNonce(false)
                characteristicDataUpload?.setMtu(expectedMtu)
            }

            builder.requestMtu(512)
            builder.add(SetWearableStateAction(getWearable(), Wearable.State.INITIALIZING, getContext()))
            builder.notify(characteristicWrite, true)
            builder.notify(characteristicRead, true)
            builder.notify(mCharacteristicActivityData, true)
            builder.notify(mCharacteristicDataUpload, true)
            builder.add(SetWearableStateAction(getWearable(), Wearable.State.AUTHENTICATING, getContext()))
            builder.add(object: PlainAction() {
                override fun run(gatt: BluetoothGatt?): Boolean {
                    if (uuidSet.encrypted) xiaomiSupport.getAuthService().startEncryptedHandshake()
                    else xiaomiSupport.getAuthService().startClearTextHandshake()

                    return true
                }
            })

            return builder
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic): Boolean {
            if (super.onCharacteristicChanged(gatt, characteristic)) return true

            when (characteristic.uuid) {
                characteristicCommandRead?.characteristicUuid -> {
                    characteristicCommandRead?.onCharacteristicChanged(characteristic.value)
                    return true
                }

                characteristicCommandWrite?.characteristicUuid -> {
                    characteristicCommandWrite?.onCharacteristicChanged(characteristic.value)
                    return true
                }

                characteristicActivityData?.characteristicUuid -> {
                    characteristicActivityData?.onCharacteristicChanged(characteristic.value)
                    return true
                }

                characteristicDataUpload?.characteristicUuid -> {
                    characteristicDataUpload?.onCharacteristicChanged(characteristic.value)
                    return true
                }
            }

            println("Unhandled characteristic changed: ${characteristic.uuid} [${characteristic.value.contentToString()}]")
            return false
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)

            characteristicCommandRead?.setMtu(mtu)
            characteristicCommandWrite?.setMtu(mtu)
            characteristicActivityData?.setMtu(mtu)
            characteristicDataUpload?.setMtu(mtu)
        }

        override fun useAutoConnect() = xiaomiSupport.useAutoConnect()
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun connect() = commsSupport.connect()

    fun createTransactionBuilder(taskName: String): TransactionBuilder {
        return commsSupport.createTransactionBuilder(taskName)
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    override fun dispose() {
        commsSupport.dispose()
    }

    fun getQueue() = commsSupport.getQueue()!!

    override fun onAuthSuccess() {
        characteristicCommandRead?.reset()
        characteristicCommandWrite?.reset()
        characteristicActivityData?.reset()
        characteristicDataUpload?.reset()
    }

    override fun sendCommand(taskName: String, command: XiaomiProto.Command) {
        characteristicCommandWrite?.write(taskName, command.toByteArray())
    }

    override fun setAutoReconnect(enabled: Boolean) {
        commsSupport.setAutoReconnect(enabled)
    }

    override fun setContext(wearable: Wearable, adapter: BluetoothAdapter, context: Context) {
        commsSupport.setContext(wearable, adapter, context)
    }
}