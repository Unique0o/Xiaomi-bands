package com.example.logifitappp.data.dao

import androidx.room.Dao
import com.example.logifitappp.data.dao.commons.WearableTimeDao
import com.example.logifitappp.data.models.XiaomiSleepTimeModel

@Dao
abstract class XiaomiSleepTimeDao: WearableTimeDao<XiaomiSleepTimeModel>("xiaomi_sleep_times") {
}