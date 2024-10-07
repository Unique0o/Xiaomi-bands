package com.example.logifitappp.domain.usecase

import com.example.logifitappp.data.models.TrainingInfoModel
import com.example.logifitappp.domain.repository.TrainingRepository
import javax.inject.Inject


class GetTrainingUseCase @Inject constructor(private val repository: TrainingRepository) {
    suspend operator fun invoke(): List<TrainingInfoModel> { return repository.getTrainingInfo()  }
}
