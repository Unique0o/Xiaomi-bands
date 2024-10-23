package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evaluation_results")
data class EvaluationResultModel(
    @ColumnInfo(name = "evaluation_id") val evaluationId: Int,
    val condition: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @PrimaryKey val id: Int,
    val result: String,
    val title: String,
    @ColumnInfo(name = "user_id") val userId: Int
)