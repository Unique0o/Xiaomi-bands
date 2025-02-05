package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.R
import com.example.logifitappp.enums.ChipStatusEnum
import com.google.gson.annotations.SerializedName

data class FetchTrainingResponse(
    val description: String?,
    val id: Int,
    @SerializedName("image_url") val image: String?,
    @SerializedName("lessons_count") val lessonsCount: Int,
    val name: String,
    @SerializedName("completed_lessons_count") val completedLessonsCount: Int,
    @SerializedName("completion_percentage") var progressPercentage: Float?
) {
    fun getStatusPair(): Pair<Int, ChipStatusEnum>? = when {
        progressPercentage == 100f -> Pair(R.string.completed, ChipStatusEnum.SUCCESS)
        progressPercentage != null && progressPercentage!! > 0 -> Pair(R.string.in_progress, ChipStatusEnum.WARNING)
        else -> null
    }
}
