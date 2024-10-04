package com.example.logifitappp.domain.repository

import com.example.logifitappp.ui.screens.occupationalInfo.OccupationalInfoItem

interface OccupationalInfoRepository {
    suspend fun getOccupationalInfo(): List<OccupationalInfoItem>
}