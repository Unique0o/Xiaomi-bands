package com.example.logifitappp.di

import android.content.Context
import com.example.logifitappp.core.App
import com.example.logifitappp.domain.repository.WearableRepository
import com.example.logifitappp.domain.service.WearableService
import com.example.logifitappp.data.remote.api.AuthApi
import com.example.logifitappp.data.remote.api.EvaluationApi
import com.example.logifitappp.data.remote.api.TenantApi
import com.example.logifitappp.data.repository.AuthRepositoryImpl
import com.example.logifitappp.data.repository.EvaluationRepositoryImpl
import com.example.logifitappp.data.repository.HealthInfoRepositoryImpl
import com.example.logifitappp.data.repository.OccupationalInfoRepositoryImpl
import com.example.logifitappp.data.repository.PersonalInfoRepositoryImpl
import com.example.logifitappp.data.repository.TenantRepositoryImpl
import com.example.logifitappp.data.repository.TrainingRepositoryImpl
import com.example.logifitappp.data.repository.UserRepositoryImpl
import com.example.logifitappp.domain.repository.AuthRepository
import com.example.logifitappp.domain.repository.EvaluationRepository
import com.example.logifitappp.domain.repository.HealthInfoRepository
import com.example.logifitappp.domain.repository.OccupationalInfoRepository
import com.example.logifitappp.domain.repository.PersonalInfoRepository
import com.example.logifitappp.domain.repository.TenantRepository
import com.example.logifitappp.domain.repository.TrainingRepository
import com.example.logifitappp.domain.repository.UserRepository
import com.example.logifitappp.domain.service.AuthService
import com.example.logifitappp.domain.service.EvaluationService
import com.example.logifitappp.domain.service.TenantService
import com.example.logifitappp.domain.usecase.GetOccupationalInfoUseCase
import com.example.logifitappp.domain.usecase.GetPersonalInfoUseCase
import com.example.logifitappp.domain.usecase.GetTrainingLessonsUseCase
import com.example.logifitappp.domain.usecase.GetTrainingUseCase
import com.example.logifitappp.domain.usecase.HealthInfoUseCase
import com.example.logifitappp.domain.usecase.LoginUseCase
import com.example.logifitappp.domain.usecase.RecoverPasswordUseCase
import com.example.logifitappp.domain.usecase.UpdateTenantInformationUseCase
import com.example.logifitappp.utils.Constants.BASE_URL
import com.example.logifitappp.viewmodel.views.HealthInfo.HealthInfoViewModel
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoViewModel
import com.example.logifitappp.viewmodel.views.PersonalInfo.PersonalInfoViewModel
import com.example.logifitappp.viewmodel.views.Trainings.TrainingDetailViewModel
import com.example.logifitappp.viewmodel.views.Trainings.TrainingViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(Interceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")

            App.database.userDao().getLoggedIn()?.let { user ->
                request.addHeader("Authorization", "Bearer ${user.accessToken}")
            }

            it.proceed(request.build())
        })
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository = authRepositoryImpl

    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(authApi: AuthApi) = AuthRepositoryImpl(authApi)

    @Provides
    @Singleton
    fun provideAuthService(authRepository: AuthRepository) = AuthService(authRepository)

    @Provides
    @Singleton
    fun provideLoginUseCase(
        authService: AuthService,
        updateTenantInformationUseCase: UpdateTenantInformationUseCase
    ) = LoginUseCase(authService, updateTenantInformationUseCase)

    @Provides
    @Singleton
    fun provideEvaluationApi(retrofit: Retrofit): EvaluationApi = retrofit.create(EvaluationApi::class.java)

    @Provides
    @Singleton
    fun provideEvaluationRepository(evaluationRepositoryImpl: EvaluationRepositoryImpl): EvaluationRepository = evaluationRepositoryImpl

    @Provides
    @Singleton
    fun provideEvaluationRepositoryImpl(evaluationApi: EvaluationApi) = EvaluationRepositoryImpl(evaluationApi)

    @Provides
    @Singleton
    fun provideEvaluationService(evaluationRepository: EvaluationRepository) = EvaluationService(evaluationRepository)

    @Provides
    @Singleton
    fun provideTenantApi(retrofit: Retrofit): TenantApi = retrofit.create(TenantApi::class.java)

    @Provides
    @Singleton
    fun provideTenantRepository(tenantRepositoryImpl: TenantRepositoryImpl): TenantRepository = tenantRepositoryImpl

    @Provides
    @Singleton
    fun provideTenantRepositoryImpl(tenantApi: TenantApi) = TenantRepositoryImpl(tenantApi)

    @Provides
    @Singleton
    fun provideTenantService(tenantRepository: TenantRepository) = TenantService(tenantRepository)

    @Provides
    @Singleton
    fun provideUpdateTenantInformationUseCase(
        tenantService: TenantService,
        evaluationService: EvaluationService
    ) = UpdateTenantInformationUseCase(tenantService, evaluationService)





    @Provides
    @Singleton
    fun provideWearableRepository(retrofit: Retrofit): WearableRepository = retrofit.create(
        WearableRepository::class.java
    )

    @Provides
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository {
        return userRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideOccupationalInfoRepository(context: Context): OccupationalInfoRepository {
        return OccupationalInfoRepositoryImpl(context)
    }

    @Provides
    fun provideGetOccupationalInfoUseCase(repository: OccupationalInfoRepository): GetOccupationalInfoUseCase {
        return GetOccupationalInfoUseCase(repository)
    }

    @Provides
    fun provideOccupationalInfoViewModel(getOccupationalInfoUseCase: GetOccupationalInfoUseCase): OccupationalInfoViewModel {
        return OccupationalInfoViewModel(getOccupationalInfoUseCase)
    }

    @Provides
    @Singleton
    fun provideRecoverPasswordUseCase(authService: AuthService): RecoverPasswordUseCase {
        return RecoverPasswordUseCase(authService)
    }

    @Provides
    @Singleton
    fun provideWearableService(wearableRepository: WearableRepository) = WearableService(wearableRepository)



    @Provides
    @Singleton
    fun providePersonalInfoRepository(context: Context): PersonalInfoRepository {
        return PersonalInfoRepositoryImpl(context)
    }

    @Provides
    fun provideGetPersonalInfoUseCase(repository: PersonalInfoRepository): GetPersonalInfoUseCase {
        return GetPersonalInfoUseCase(repository)
    }

    @Provides
    fun providePersonalInfoViewModel(getPersonalInfoUseCase: GetPersonalInfoUseCase): PersonalInfoViewModel {
        return PersonalInfoViewModel(getPersonalInfoUseCase)
    }

    @Provides
    @Singleton
    fun provideHealthInfoRepository(context: Context): HealthInfoRepository {
        return HealthInfoRepositoryImpl(context)
    }

    @Provides
    fun provideGetHealthInfoUseCase(repository: HealthInfoRepository): HealthInfoUseCase {
        return HealthInfoUseCase(repository)
    }

    @Provides
    fun provideHealthInfoViewModel(getHealthUseCase: HealthInfoUseCase): HealthInfoViewModel {
        return HealthInfoViewModel(getHealthUseCase)
    }

    @Provides
    @Singleton
    fun provideTrainingInfoRepository(context: Context): TrainingRepository {
        return TrainingRepositoryImpl(context)
    }

    @Provides
    fun provideGetTrainingInfoUseCase(repository: TrainingRepository): GetTrainingUseCase {
        return GetTrainingUseCase(repository)
    }
    @Provides
    fun provideGetTrainingDetailsUseCases(repository: TrainingRepository): GetTrainingLessonsUseCase {
        return GetTrainingLessonsUseCase(repository)
    }

    @Provides
    fun provideTrainingInfoViewModel(getTrainingUseCase: GetTrainingUseCase): TrainingViewModel {
        return TrainingViewModel(getTrainingUseCase)
    }

    @Provides
    fun provideTrainingDetailViewModel(useCase: GetTrainingLessonsUseCase, useCase2: GetTrainingUseCase): TrainingDetailViewModel {
        return TrainingDetailViewModel(useCase2, useCase)
    }

}