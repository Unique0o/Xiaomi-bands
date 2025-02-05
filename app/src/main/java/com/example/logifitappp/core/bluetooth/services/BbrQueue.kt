package com.example.logifitappp.core.bluetooth.services

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.os.Process
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.App
import com.example.logifitappp.core.builders.bbr.Transaction
import com.example.logifitappp.core.handlers.SocketCallbackHandler
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.enums.WearableUpdateSubjectEnum
import java.io.IOException
import java.util.Locale
import java.util.UUID

class BbrQueue(
    private val bluetoothAdapter: BluetoothAdapter?,
    private val wearable: Wearable,
    private val context: Context,
    private val socketCallback: SocketCallbackHandler,
    private val supportedService: UUID?,
    private val bufferSize: Int
) {
    private var bluetoothSocket: BluetoothSocket? = null
    private var disposed = false

    private var writeHandler: Handler? = null
    private val writeHandlerThread = HandlerThread("Write Thread", Process.THREAD_PRIORITY_BACKGROUND)

    private var readThread: Thread? = object: Thread("Read Thread") {
        override fun run() {
            val buffer = ByteArray(bufferSize)
            var read: Int?

            while (!disposed) {
                try {
                    read = bluetoothSocket?.inputStream?.read(buffer)

                    if (read == -1) throw IOException("End of stream")
                } catch (e: IOException) {
                    println("IO exception while reading message from socket, breaking out of read thread: $e")
                    break
                }

                println("Receiver $read bytes: ${buffer.contentToString()}")

                try {
                    read?.let { socketCallback.onSocketRead(buffer.copyOf(it)) }
                } catch (e: Throwable) {
                    println("Failed to process received bytes in onSocketRead callback: $e")
                }
            }

            println("Exited read thread loop, disconnecting")
            App.getWearableServiceTo(wearable).disconnect()
        }
    }

    init {
        writeHandlerThread.start()

        writeHandler = object: Handler(writeHandlerThread.looper) {
            @SuppressLint("MissingPermission")
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    HANDLER_SUBJECT_CONNECT -> {
                        try {
                            bluetoothSocket?.connect()

                            println("Connected to RFCOMM socket for ${wearable.getName()}")
                            setWearableConnectionState(Wearable.State.CONNECTED)

                            readThread?.name = String.format(Locale.ENGLISH, "Read Thread for %s", wearable.getName())
                            writeHandlerThread.name =  String.format(Locale.ENGLISH, "Write Thread for %s", wearable.getName())

                            readThread?.start()
                            onConnectionEstablished()
                        } catch (e: IOException) {
                            println("IO exception while establishing socket connection: $e")
                            setWearableConnectionState(Wearable.State.NOT_CONNECTED)
                        }

                        return
                    }

                    HANDLER_SUBJECT_PERFORM_TRANSACTION -> {
                        try {
                            if (!isConnected()) {
                                println("Not connected, updating device state to WAITING_FOR_RECONNECT")
                                setWearableConnectionState(Wearable.State.WAITING_FOR_RECONNECT)
                                return
                            }

                            if (msg.obj !is Transaction) {
                                println("msg.obj is not an instance of Transaction")
                                return
                            }

                            val transaction = msg.obj as Transaction

                            for (action in transaction.getActions()) {
                                if (action.run(bluetoothSocket)) println("Action ok: $action")
                                else {
                                    println("Action returned false, cancelling further actions in transaction: $action")
                                    break
                                }
                            }
                        } catch (e: Throwable) {
                            println("IO Write Thread died: $e")
                        }

                        return
                    }
                }

                println("Unhandled write handler message ${msg.what}")
            }
        }
    }

    fun add(transaction: Transaction) {
        println("Adding transaction to looper message queue: $transaction")

        if (!transaction.isEmpty()) {
            writeHandler?.obtainMessage(HANDLER_SUBJECT_PERFORM_TRANSACTION, transaction)?.sendToTarget()
        }
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    fun connect(): Boolean {
        if (isConnected()) return false.also { println("Ignoring connect() because already connected.") }

        println("Attempting to connect to ${wearable.getName()} (${wearable.getAddress()})")

        bluetoothAdapter?.cancelDiscovery()

        val originalState = wearable.getState()
        setWearableConnectionState(Wearable.State.CONNECTING)

        try {
            val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(wearable.getAddress())
            bluetoothSocket = bluetoothDevice?.createRfcommSocketToServiceRecord(supportedService)
        } catch (e: IOException) {
            setWearableConnectionState(originalState)
            bluetoothSocket = null

            return false.also { println("Unable to connect to RFCOMM endpoint: $e") }
        }

        writeHandler?.let {
            it.sendMessageAtFrontOfQueue(it.obtainMessage(HANDLER_SUBJECT_CONNECT))
        }

        return true.also { println("Socket created, connecting in handler") }
    }

    fun disconnect() {
        if (writeHandlerThread.isAlive) writeHandlerThread.quit()

        if (bluetoothSocket?.isConnected == true) {
            try {
                bluetoothSocket?.close()
            } catch (e: IOException) {
                println("IO exception while closing socket in disconnect(): $e")
            }
        }

        bluetoothSocket = null
        setWearableConnectionState(Wearable.State.NOT_CONNECTED)
    }

    fun dispose() {
        if (disposed) return

        disposed = true
        disconnect()

        if (readThread?.isAlive == true) {
            readThread?.interrupt()
            readThread = null
        }
    }

    private fun isConnected() = wearable.isConnected() && bluetoothSocket?.isConnected == true

    protected fun onConnectionEstablished() {
        socketCallback.onConnectionEstablished()
    }

    private fun setWearableConnectionState(newState: Wearable.State) {
        println("New device connection state: $newState")
        wearable.setState(newState)
        wearable.sendDeviceUpdateIntent(context, WearableUpdateSubjectEnum.CONNECTION_STATE)
    }

    companion object {
        const val HANDLER_SUBJECT_CONNECT = 0
        const val HANDLER_SUBJECT_PERFORM_TRANSACTION = 1
    }
}