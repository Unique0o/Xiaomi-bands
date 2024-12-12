package com.example.logifitappp.core.handlers

import com.example.logifitappp.core.specs.CallSpec
import com.example.logifitappp.core.specs.MusicSpec
import com.example.logifitappp.core.specs.MusicStateSpec
import com.example.logifitappp.core.specs.NotificationSpec

interface WearableEventHandler {
    fun onDeleteNotification(id: Int)
    fun onFetchRecordedData(dataTypes: Int)
    fun onNotification(notificationSpec: NotificationSpec)
    fun onSetCallState(callSpec: CallSpec)
    fun onSetMusicInfo(musicSpec: MusicSpec?)
    fun onSetMusicState(stateSpec: MusicStateSpec)
}