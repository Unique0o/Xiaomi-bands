package com.example.logifitappp.viewmodel.views.AddSleepData

import android.net.Uri
import java.time.LocalDateTime

sealed class AddSleepDataEvent {
    data class SetFellAsleepTime(val time: LocalDateTime) : AddSleepDataEvent()
    data class SetWokeUpTime(val time: LocalDateTime) : AddSleepDataEvent()
    data class SetDuration(val duration: String) : AddSleepDataEvent()
    data class AttachMedia(val uri: Uri) : AddSleepDataEvent()
    object RemoveMedia : AddSleepDataEvent()
    object SaveSleepData : AddSleepDataEvent()
}