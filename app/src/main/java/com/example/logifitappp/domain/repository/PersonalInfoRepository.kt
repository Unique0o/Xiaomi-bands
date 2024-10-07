package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.PersonalInfoModel


interface PersonalInfoRepository {
    suspend fun getPersonalInfo(): List<PersonalInfoModel>
}
