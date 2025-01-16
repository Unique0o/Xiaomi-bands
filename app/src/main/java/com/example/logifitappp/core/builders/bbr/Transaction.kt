package com.example.logifitappp.core.builders.bbr

import com.example.logifitappp.core.builders.AbstractTransaction
import com.example.logifitappp.core.builders.bbr.actions.Action
import com.example.logifitappp.core.handlers.SocketCallbackHandler
import java.util.Collections

class Transaction(taskName: String): AbstractTransaction(taskName) {
    private val actions = mutableListOf<Action>()

    var socketCallback: SocketCallbackHandler? = null

    fun add(action: Action) = action.let { actions.add(it) }

    override fun getActionCount() = actions.size

    fun getActions(): List<Action> = Collections.unmodifiableList(actions)

    fun isEmpty(): Boolean = actions.isEmpty()
}