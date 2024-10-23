package com.example.logifitappp.domain.usecase

import com.example.logifitappp.data.models.LessonModel
import com.example.logifitappp.domain.repository.TrainingRepository
import javax.inject.Inject


class GetTrainingLessonsUseCase @Inject constructor(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(trainingId: String): List<LessonModel> {
        return repository.getTrainingLessons(trainingId)
    }
}