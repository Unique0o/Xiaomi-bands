package com.example.logifitappp.core.builders.ble

import com.example.logifitappp.core.builders.AbstractTransaction
import com.example.logifitappp.core.builders.ble.actions.Action
import com.example.logifitappp.core.handlers.BluetoothGattCallbackHandler
import java.util.Collections

class Transaction(taskName: String) : AbstractTransaction(taskName) {
    private val actions = mutableListOf<Action>()

    var callbackHandler: BluetoothGattCallbackHandler? = null
        set(value) {
            field = value
            modifyCallbackHandler = true
        }

    var modifyCallbackHandler = false

    fun add(action: Action) = action.let { actions.add(it) }

    override fun getActionCount(): Int = actions.size

    fun getActions(): List<Action> = Collections.unmodifiableList(actions)

    fun isEmpty(): Boolean = actions.isEmpty()
}