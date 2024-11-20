package com.example.logifitappp.data.dao

import androidx.room.Dao
import com.example.logifitappp.data.dao.commons.WearableRawActivityDao
import com.example.logifitappp.data.models.HuamiExtendedRawActivityModel

@Dao
abstract class HuamiExtendedRawActivityDao: WearableRawActivityDao<HuamiExtendedRawActivityModel>("huami_extended_raw_activities") {
}