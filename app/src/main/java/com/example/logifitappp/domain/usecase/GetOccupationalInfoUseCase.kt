package com.example.logifitappp.domain.usecase

import com.example.logifitappp.domain.repository.OccupationalInfoRepository
import com.example.logifitappp.data.models.OccupationalInfoItem
import javax.inject.Inject

class GetOccupationalInfoUseCase @Inject constructor(
    private val repository: OccupationalInfoRepository
) {
    suspend operator fun invoke(): List<OccupationalInfoItem> {
        return repository.getOccupationalInfo()
    }
}