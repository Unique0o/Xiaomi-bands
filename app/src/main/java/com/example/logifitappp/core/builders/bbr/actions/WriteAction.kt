package com.example.logifitappp.core.builders.bbr.actions

import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.OutputStream

class WriteAction(private val payload: ByteArray): Action() {
    private var outputStream: OutputStream? = null

    override fun expectsResult() = true

    override fun run(socket: BluetoothSocket?): Boolean {
        try {
            outputStream = socket?.outputStream

            if (outputStream == null) {
                return false.also { println("outputStream is null") }
            }

            return writeValue(payload)
        } catch (e: IOException) {
            println("Can not get the output stream")
        }

        return false
    }

    protected fun writeValue(payload: ByteArray): Boolean {
        try {
            outputStream?.let {
                it.write(payload)
                it.flush()
            }

            return true
        } catch (e: IOException) {
            println("Error writing to socket: $e")
        }

        return false
    }
}