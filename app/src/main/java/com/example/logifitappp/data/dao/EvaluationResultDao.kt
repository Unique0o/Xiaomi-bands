package com.example.logifitappp.data.dao

import android.icu.util.GregorianCalendar
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.EvaluationResultModel

@Dao
abstract class EvaluationResultDao {
    @Query("DELETE FROM evaluation_results")
    abstract fun delete()

    @Query("SELECT * FROM evaluation_results WHERE created_at LIKE :date || '%' AND user_id = :userId")
    abstract fun fetchFromDate(date: String, userId: Int): List<EvaluationResultModel>

    fun fetchFromToday(userId: Int): List<EvaluationResultModel> {
        return fetchFromDate(DateTimeUtils.formatReducedIso8601(GregorianCalendar.getInstance().time), userId)
    }

    fun findFromToday(evaluationId: Int, userId: Int): EvaluationResultModel? {
        return findBy(DateTimeUtils.formatReducedIso8601(GregorianCalendar.getInstance().time), evaluationId, userId)
    }

    @Query("SELECT * FROM evaluation_results WHERE created_at LIKE :date || '%' AND user_id = :userId AND evaluation_id = :evaluationId LIMIT 1")
    abstract fun findBy(date: String, evaluationId: Int, userId: Int): EvaluationResultModel?

    @Upsert
    abstract fun store(vararg evaluations: EvaluationResultModel)
}