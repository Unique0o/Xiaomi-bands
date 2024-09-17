package com.example.logifitappp.data.dao

import androidx.room.Dao
import com.example.logifitappp.data.dao.commons.WearableTimeDao
import com.example.logifitappp.data.models.XiaomiSleepStageModel

@Dao
abstract class XiaomiSleepStageDao: WearableTimeDao<XiaomiSleepStageModel>("xiaomi_sleep_stages") {
}