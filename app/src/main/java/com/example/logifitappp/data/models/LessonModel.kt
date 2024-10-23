package com.example.logifitappp.data.models

data class LessonModel(
    val id: Int,
    val description: String,
    val duration: Int,
    val externalIdentifier: Int,
    val isCompleted: Boolean,
    val name: String,
    val path: String,
    val trainingExternalIdentifier: Int,
    val videoUrl: String,
    val imageRes: Int
)
