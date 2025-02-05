package com.example.logifitappp.core.builders.bbr

import com.example.logifitappp.core.bluetooth.services.BbrQueue
import com.example.logifitappp.core.builders.bbr.actions.Action
import com.example.logifitappp.core.builders.bbr.actions.WriteAction

class TransactionBuilder(taskName: String) {
    val transaction = Transaction(taskName)

    private var queued = false

    fun add(action: Action) {
        transaction.add(action)
    }

    fun queue(queue: BbrQueue) {
        if (queued) throw IllegalStateException("This builder had already been queued. You must not reuse it.")

        queued = true
        queue.add(transaction)
    }

    fun write(payload: ByteArray) {
        add(WriteAction(payload))
    }
}