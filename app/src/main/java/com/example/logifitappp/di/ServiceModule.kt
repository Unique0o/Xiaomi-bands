package com.example.logifitappp.di

import com.example.logifitappp.domain.repository.AdminRepository
import com.example.logifitappp.domain.repository.AudioRepository
import com.example.logifitappp.domain.repository.AuthRepository
import com.example.logifitappp.domain.repository.DocumentTypeRepository
import com.example.logifitappp.domain.repository.EvaluationRepository
import com.example.logifitappp.domain.repository.LessonRepository
import com.example.logifitappp.domain.repository.LocationRepository
import com.example.logifitappp.domain.repository.NotificationRepository
import com.example.logifitappp.domain.repository.SleepRepository
import com.example.logifitappp.domain.repository.TenantRepository
import com.example.logifitappp.domain.repository.TrainingRepository
import com.example.logifitappp.domain.repository.UserRepository
import com.example.logifitappp.domain.repository.WearableRepository
import com.example.logifitappp.domain.service.AdminService
import com.example.logifitappp.domain.service.AudioService
import com.example.logifitappp.domain.service.AuthService
import com.example.logifitappp.domain.service.DocumentTypeService
import com.example.logifitappp.domain.service.EvaluationService
import com.example.logifitappp.domain.service.LessonService
import com.example.logifitappp.domain.service.LocationService
import com.example.logifitappp.domain.service.NotificationService
import com.example.logifitappp.domain.service.SleepService
import com.example.logifitappp.domain.service.TenantService
import com.example.logifitappp.domain.service.TrainingService
import com.example.logifitappp.domain.service.UserService
import com.example.logifitappp.domain.service.WearableService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideAdminService(adminRepository: AdminRepository) = AdminService(adminRepository)

    @Provides
    @Singleton
    fun provideAudioService(audioRepository: AudioRepository) = AudioService(audioRepository)

    @Provides
    @Singleton
    fun provideAuthService(authRepository: AuthRepository) = AuthService(authRepository)

    @Provides
    @Singleton
    fun provideDocumentTypeService(documentTypeRepository: DocumentTypeRepository) = DocumentTypeService(documentTypeRepository)

    @Provides
    @Singleton
    fun provideEvaluationService(evaluationRepository: EvaluationRepository) = EvaluationService(evaluationRepository)

    @Provides
    @Singleton
    fun provideLessonService(lessonRepository: LessonRepository) = LessonService(lessonRepository)

    @Provides
    @Singleton
    fun provideLocationService(locationRepository: LocationRepository) = LocationService(locationRepository)

    @Provides
    @Singleton
    fun provideNotificationService(notificationRepository: NotificationRepository) = NotificationService(notificationRepository)

    @Provides
    @Singleton
    fun provideSleepService(sleepRepository: SleepRepository) = SleepService(sleepRepository)

    @Provides
    @Singleton
    fun provideTenantService(tenantRepository: TenantRepository) = TenantService(tenantRepository)

    @Provides
    @Singleton
    fun provideTrainingService(trainingRepository: TrainingRepository) = TrainingService(trainingRepository)

    @Provides
    @Singleton
    fun provideUserService(userRepository: UserRepository) = UserService(userRepository)

    @Provides
    @Singleton
    fun provideWearableService(wearableRepository: WearableRepository) = WearableService(wearableRepository)
}