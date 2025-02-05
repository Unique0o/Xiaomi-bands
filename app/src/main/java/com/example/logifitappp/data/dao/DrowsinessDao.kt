package com.example.logifitappp.data.dao

import android.icu.util.GregorianCalendar
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.DrowsinessModel

@Dao
abstract class DrowsinessDao {
    @Query("SELECT * FROM drowsiness WHERE sent_at IS NULL AND wearable_id = :wearableId")
    abstract fun fetchNotSent(wearableId: Int): List<DrowsinessModel>

    fun fetchNotSentAndLast(wearableId: Int): List<DrowsinessModel> {
        val drowsinessNotSent = fetchNotSent(wearableId).toMutableList()
        val lastDrowsiness = findFromToday(wearableId)

        lastDrowsiness?.let {
            if (drowsinessNotSent.find { drowsiness -> drowsiness.id == it.id } == null) drowsinessNotSent.add(it)
        }

        return drowsinessNotSent
    }

    @Query("SELECT * FROM drowsiness WHERE wearable_id = :wearableId AND created_at LIKE :date || '%' LIMIT 1")
    abstract fun findFromDate(wearableId: Int, date: String): DrowsinessModel?

    fun findFromToday(wearableId: Int): DrowsinessModel? {
        return findFromDate(wearableId, DateTimeUtils.formatReducedIso8601(GregorianCalendar.getInstance().time))
    }

    @Query("UPDATE drowsiness SET sent_at = :date WHERE wearable_id = :wearableId AND sent_at IS NULL")
    abstract fun markAsSent(date: String, wearableId: Int)

    @Upsert
    abstract fun store(drowsiness: DrowsinessModel)
}