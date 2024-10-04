package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.PersonalInfoItem


interface PersonalInfoRepository {
    suspend fun getPersonalInfo(): List<PersonalInfoItem>
}
