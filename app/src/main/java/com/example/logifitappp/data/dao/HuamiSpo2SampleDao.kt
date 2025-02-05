package com.example.logifitappp.data.dao

import androidx.room.Dao
import com.example.logifitappp.data.dao.commons.WearableSampleDao
import com.example.logifitappp.data.models.HuamiSpo2SampleModel

@Dao
abstract class HuamiSpo2SampleDao: WearableSampleDao<HuamiSpo2SampleModel>("huami_spo2_samples") {
}