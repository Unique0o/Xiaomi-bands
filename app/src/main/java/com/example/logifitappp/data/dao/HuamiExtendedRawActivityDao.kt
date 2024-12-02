package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.logifitappp.data.dao.commons.WearableRawActivityDao
import com.example.logifitappp.data.models.HuamiExtendedRawActivityModel

@Dao
abstract class HuamiExtendedRawActivityDao: WearableRawActivityDao<HuamiExtendedRawActivityModel>("huami_extended_raw_activities") {
    @Query("SELECT * FROM huami_extended_raw_activities WHERE wearable_id = :wearableId AND timestamp < :timestamp AND type NOT IN (0, 10, -1, 16, 80, 96, 112) ORDER BY timestamp DESC LIMIT 1")
    abstract fun findPreviousValidRawActivity(wearableId: Int, timestamp: Long): HuamiExtendedRawActivityModel?
}