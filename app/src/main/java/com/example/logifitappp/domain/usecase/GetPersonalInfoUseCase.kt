package com.example.logifitappp.domain.usecase


import com.example.logifitappp.data.models.PersonalInfoModel
import com.example.logifitappp.domain.repository.PersonalInfoRepository
import javax.inject.Inject

class GetPersonalInfoUseCase @Inject constructor(private val repository: PersonalInfoRepository) {
    suspend operator fun invoke(): List<PersonalInfoModel> { return repository.getPersonalInfo()  }
}
