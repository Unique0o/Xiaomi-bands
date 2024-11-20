package com.example.logifitappp.data.dao

import androidx.room.Dao
import com.example.logifitappp.data.dao.commons.WearableRawActivityDao
import com.example.logifitappp.data.models.MiBandRawActivityModel

@Dao
abstract class MiBandRawActivityDao: WearableRawActivityDao<MiBandRawActivityModel>("mi_band_raw_activities") {
}