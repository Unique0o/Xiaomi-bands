package com.example.logifitappp.data.dao

import androidx.room.Dao
import com.example.logifitappp.data.dao.commons.WearableSpo2SampleDao
import com.example.logifitappp.data.models.HuamiSpo2SampleModel

@Dao
abstract class HuamiSpo2SampleDao: WearableSpo2SampleDao<HuamiSpo2SampleModel>("huami_spo2_samples") {
}