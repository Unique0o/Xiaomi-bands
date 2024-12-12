package com.example.logifitappp.di

import com.example.logifitappp.data.remote.api.AuthApi
import com.example.logifitappp.data.remote.api.EvaluationApi
import com.example.logifitappp.data.remote.api.LessonApi
import com.example.logifitappp.data.remote.api.NotificationApi
import com.example.logifitappp.data.remote.api.SleepApi
import com.example.logifitappp.data.remote.api.TenantApi
import com.example.logifitappp.data.remote.api.TrainingApi
import com.example.logifitappp.data.remote.api.UserApi
import com.example.logifitappp.data.remote.api.WearableApi
import com.example.logifitappp.data.repository.AuthRepositoryImpl
import com.example.logifitappp.data.repository.EvaluationRepositoryImpl
import com.example.logifitappp.data.repository.LessonRepositoryImpl
import com.example.logifitappp.data.repository.NotificationRepositoryImpl
import com.example.logifitappp.data.repository.SleepRepositoryImpl
import com.example.logifitappp.data.repository.TenantRepositoryImpl
import com.example.logifitappp.data.repository.TrainingRepositoryImpl
import com.example.logifitappp.data.repository.UserRepositoryImpl
import com.example.logifitappp.data.repository.WearableRepositoryImpl
import com.example.logifitappp.domain.repository.AuthRepository
import com.example.logifitappp.domain.repository.EvaluationRepository
import com.example.logifitappp.domain.repository.LessonRepository
import com.example.logifitappp.domain.repository.NotificationRepository
import com.example.logifitappp.domain.repository.SleepRepository
import com.example.logifitappp.domain.repository.TenantRepository
import com.example.logifitappp.domain.repository.TrainingRepository
import com.example.logifitappp.domain.repository.UserRepository
import com.example.logifitappp.domain.repository.WearableRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository = authRepositoryImpl

    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(authApi: AuthApi) = AuthRepositoryImpl(authApi)

    @Provides
    @Singleton
    fun provideEvaluationRepository(evaluationRepositoryImpl: EvaluationRepositoryImpl): EvaluationRepository = evaluationRepositoryImpl

    @Provides
    @Singleton
    fun provideEvaluationRepositoryImpl(evaluationApi: EvaluationApi) = EvaluationRepositoryImpl(evaluationApi)

    @Provides
    @Singleton
    fun provideLessonRepository(lessonRepositoryImpl: LessonRepositoryImpl): LessonRepository = lessonRepositoryImpl

    @Provides
    @Singleton
    fun provideLessonRepositoryImpl(lessonApi: LessonApi) = LessonRepositoryImpl(lessonApi)

    @Provides
    @Singleton
    fun provideNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository = notificationRepositoryImpl

    @Provides
    @Singleton
    fun provideNotificationRepositoryImpl(notificationApi: NotificationApi) = NotificationRepositoryImpl(notificationApi)

    @Provides
    @Singleton
    fun provideSleepRepository(sleepRepositoryImpl: SleepRepositoryImpl): SleepRepository = sleepRepositoryImpl

    @Provides
    @Singleton
    fun provideSleepRepositoryImpl(sleepApi: SleepApi) = SleepRepositoryImpl(sleepApi)

    @Provides
    @Singleton
    fun provideTenantRepository(tenantRepositoryImpl: TenantRepositoryImpl): TenantRepository = tenantRepositoryImpl

    @Provides
    @Singleton
    fun provideTenantRepositoryImpl(tenantApi: TenantApi) = TenantRepositoryImpl(tenantApi)

    @Provides
    @Singleton
    fun provideTrainingRepository(trainingRepositoryImpl: TrainingRepositoryImpl): TrainingRepository = trainingRepositoryImpl

    @Provides
    @Singleton
    fun provideTrainingRepositoryImpl(trainingApi: TrainingApi) = TrainingRepositoryImpl(trainingApi)

    @Provides
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository = userRepositoryImpl

    @Provides
    @Singleton
    fun provideUserRepositoryImpl(userApi: UserApi) = UserRepositoryImpl(userApi)

    @Provides
    @Singleton
    fun provideWearableRepository(wearableRepositoryImpl: WearableRepositoryImpl): WearableRepository = wearableRepositoryImpl

    @Provides
    @Singleton
    fun provideWearableRepositoryImpl(wearableApi: WearableApi) = WearableRepositoryImpl(wearableApi)
}