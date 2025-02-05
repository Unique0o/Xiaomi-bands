package com.example.logifitappp.core.wearebles

import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.bluetooth.services.BbrQueue
import com.example.logifitappp.core.builders.bbr.TransactionBuilder
import com.example.logifitappp.core.handlers.SocketCallbackHandler
import java.util.UUID

abstract class AbstractBbrWearableSupport: AbstractWearableSupport(), SocketCallbackHandler {
    private var bufferSize = 1024
    private var supportedService: UUID? = null

    private var queue: BbrQueue? = null

    fun addSupportedService(supportedService: UUID) {
        this.supportedService = supportedService
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun connect(): Boolean {
        if (queue == null) {
            queue = BbrQueue(getBluetoothAdapter(), getWearable(), getContext(), this, getSupportedService(), getBufferSize())
        }

        return queue?.connect() ?: false
    }

    fun createTransactionBuilder(taskName: String) = TransactionBuilder(taskName)

    fun disconnect() {
        queue?.disconnect()
    }

    override fun dispose() {
        queue?.dispose()
        queue = null
    }

    private fun getBufferSize() = bufferSize

    protected open fun getSupportedService() = supportedService

    fun getQueue() = queue

    protected open fun initializeDevice(builder: TransactionBuilder): TransactionBuilder = builder

    override fun onConnectionEstablished() {
        try {
            getQueue()?.let {
                initializeDevice(createTransactionBuilder("Initializing device")).queue(it)
            }
        } catch (e: Exception) {
            getWearable().let {
                it.setState(Wearable.State.WAITING_FOR_RECONNECT)
                it.sendDeviceUpdateIntent(getContext())
            }
        }
    }
}