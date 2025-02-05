package com.example.logifitappp.core.builders.ble.actions

abstract class PlainAction: Action(null) {
    override fun expectsResult(): Boolean = false

    override fun toString(): String = "${getCreationTime()}: ${this::class.simpleName}"
}