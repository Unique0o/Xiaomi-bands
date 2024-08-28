package com.example.logifitappp.data.models

import java.time.LocalDateTime

data class MediaModel(
    val id: Int,
    val date: LocalDateTime,
    //val type: AudioTypeEnum,
    val userExternalIdentifier: Int
)