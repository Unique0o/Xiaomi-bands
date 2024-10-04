package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.OccupationalInfoItem

interface OccupationalInfoRepository {
    suspend fun getOccupationalInfo(): List<OccupationalInfoItem>
}