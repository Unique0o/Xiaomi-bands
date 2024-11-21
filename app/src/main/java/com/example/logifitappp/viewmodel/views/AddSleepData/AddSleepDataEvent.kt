package com.example.logifitappp.viewmodel.views.AddSleepData

import android.net.Uri
import java.time.LocalDateTime

sealed class AddSleepDataEvent {
    data class SetFellAsleepTime(val index: Int, val time: LocalDateTime) : AddSleepDataEvent()
    data class SetWokeUpTime(val index: Int, val time: LocalDateTime) : AddSleepDataEvent()
    object AddSleepEntry : AddSleepDataEvent()
    data class RemoveSleepEntry(val index: Int) : AddSleepDataEvent()
    data class AttachMedia(val uri: Uri) : AddSleepDataEvent()
    object RemoveMedia : AddSleepDataEvent()
    object SaveSleepData : AddSleepDataEvent()
}
