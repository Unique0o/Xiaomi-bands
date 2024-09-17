package com.example.logifitappp.core.builders.ble

import com.example.logifitappp.core.builders.AbstractTransaction
import com.example.logifitappp.core.builders.ble.actions.ServerAction
import com.example.logifitappp.core.handlers.BluetoothGattServerCallbackHandler
import java.util.Collections
import java.util.Locale

class ServerTransaction(taskName: String) : AbstractTransaction(taskName) {
    private val actions = mutableListOf<ServerAction>()

    var callbackHandler: BluetoothGattServerCallbackHandler? = null

    fun add(action: ServerAction) {
        actions.add(action)
    }

    override fun getActionCount() = actions.size

    fun getActions(): List<ServerAction> = Collections.unmodifiableList(actions)

    fun isEmpty() = actions.isEmpty()

    override fun toString() = String.format(Locale.US, "%s: Transaction task: %s with %d actions", getCreationTime(), getTaskName(), actions.size)
}