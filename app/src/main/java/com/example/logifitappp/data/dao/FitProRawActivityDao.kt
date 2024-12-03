package com.example.logifitappp.data.dao

import androidx.room.Dao
import com.example.logifitappp.data.dao.commons.WearableRawActivityDao
import com.example.logifitappp.data.models.FitProRawActivityModel

@Dao
abstract class FitProRawActivityDao: WearableRawActivityDao<FitProRawActivityModel>("fit_pro_raw_activities") {
}