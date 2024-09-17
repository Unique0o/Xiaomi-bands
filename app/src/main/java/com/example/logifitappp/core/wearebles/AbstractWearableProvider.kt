package com.example.logifitappp.core.wearebles

import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.WearableModel

abstract class AbstractWearableProvider(private val wearable: Wearable) {
    fun getStoredWearable(): WearableModel? {
        val user = App.database.userDao().getLoggedIn()

        return App.database.wearableDao().find(this.wearable.getAddress()!!, user?.id ?: 0)
    }
}