package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothGatt

abstract class AbortTransactionAction: PlainAction() {
    override fun run(gatt: BluetoothGatt?): Boolean = when (shouldAbort()) {
        true -> true
        false -> false.also { println("Aborting transaction because abort criteria met.") }
    }

    override fun toString(): String = "${getCreationTime()}: ${this::class.simpleName}: aborting? ${shouldAbort()}"

    abstract fun shouldAbort(): Boolean
}