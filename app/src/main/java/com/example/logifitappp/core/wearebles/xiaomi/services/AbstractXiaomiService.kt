package com.example.logifitappp.core.wearebles.xiaomi.services

import android.content.Context
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.core.wearebles.WearablePreferences
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiCoordinator
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSupport
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto

abstract class AbstractXiaomiService(val support: XiaomiSupport) {
    val coordinator get() = support.getWearable().getWearableCoordinator() as XiaomiCoordinator

    open fun dispose() {

    }

    protected fun getWearablePreferences(): WearablePreferences {
        return App.getWearablePreferences(support.getWearable().getAddress()!!)
    }

    open fun initialize() {

    }

    open fun onDisconnect() {

    }

    fun onSendConfiguration(configuration: String, preferences: AppPreferences) = false

    open fun setContext(context: Context) {

    }

    abstract fun handleCommand(cmd: XiaomiProto.Command)
}