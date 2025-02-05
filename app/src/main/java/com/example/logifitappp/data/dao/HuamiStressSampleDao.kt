package com.example.logifitappp.data.dao

import androidx.room.Dao
import com.example.logifitappp.data.dao.commons.WearableSampleDao
import com.example.logifitappp.data.models.HuamiStressSampleModel

@Dao
abstract class HuamiStressSampleDao: WearableSampleDao<HuamiStressSampleModel>("huami_stress_samples") {
}