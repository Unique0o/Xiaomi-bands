package com.example.logifitappp.core.builders.bbr.actions

import android.bluetooth.BluetoothSocket
import com.example.logifitappp.core.utils.DateTimeUtils
import java.util.Date

abstract class Action {
    private val createdAt = System.currentTimeMillis()

    protected fun getCreationTime(): String = DateTimeUtils.formatDateTime(Date(createdAt))

    override fun toString() = "${getCreationTime()}: ${this::class.simpleName}"

    abstract fun expectsResult(): Boolean
    abstract fun run(socket: BluetoothSocket?): Boolean
}