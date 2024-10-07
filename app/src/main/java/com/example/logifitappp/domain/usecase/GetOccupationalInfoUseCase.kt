package com.example.logifitappp.domain.usecase

import com.example.logifitappp.domain.repository.OccupationalInfoRepository
import com.example.logifitappp.data.models.OccupationalInfoItemModel
import javax.inject.Inject

class GetOccupationalInfoUseCase @Inject constructor(
    private val repository: OccupationalInfoRepository
) {
    suspend operator fun invoke(): List<OccupationalInfoItemModel> {
        return repository.getOccupationalInfo()
    }
}