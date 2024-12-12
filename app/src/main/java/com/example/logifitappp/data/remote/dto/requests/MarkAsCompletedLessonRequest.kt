package com.example.logifitappp.data.remote.dto.requests

data class MarkAsCompletedLessonRequest(
    val lesson_id: Int,
    val user_id: Int
)