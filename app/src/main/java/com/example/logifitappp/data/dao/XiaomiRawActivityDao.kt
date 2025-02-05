package com.example.logifitappp.data.dao

import androidx.room.Dao
import com.example.logifitappp.data.dao.commons.WearableRawActivityDao
import com.example.logifitappp.data.models.XiaomiRawActivityModel

@Dao
abstract class XiaomiRawActivityDao: WearableRawActivityDao<XiaomiRawActivityModel>("xiaomi_raw_activities") {
}