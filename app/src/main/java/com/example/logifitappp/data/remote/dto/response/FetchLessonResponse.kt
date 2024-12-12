package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class FetchLessonResponse(
    val description: String?,
    val duration: Long,
    val id: Int,
    @SerializedName("image_url") val image: String?,
    @SerializedName("lesson_completed") val isCompleted: Boolean,
    val name: String,
    @SerializedName("video_url") val video: String
)