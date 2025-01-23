package com.example.logifitappp.di

import android.content.Context
import com.example.logifitappp.core.App
import com.example.logifitappp.data.remote.api.AdminApi
import com.example.logifitappp.data.remote.api.AudioApi
import com.example.logifitappp.data.remote.api.AuthApi
import com.example.logifitappp.data.remote.api.DocumentTypeApi
import com.example.logifitappp.data.remote.api.EvaluationApi
import com.example.logifitappp.data.remote.api.LessonApi
import com.example.logifitappp.data.remote.api.LocationApi
import com.example.logifitappp.data.remote.api.NotificationApi
import com.example.logifitappp.data.remote.api.SleepApi
import com.example.logifitappp.data.remote.api.SleepWrittenDataApi
import com.example.logifitappp.data.remote.api.TenantApi
import com.example.logifitappp.data.remote.api.TrainingApi
import com.example.logifitappp.data.remote.api.UserApi
import com.example.logifitappp.data.remote.api.WearableApi
import com.example.logifitappp.data.repository.HealthInfoRepositoryImpl
import com.example.logifitappp.data.repository.OccupationalInfoRepositoryImpl
import com.example.logifitappp.data.repository.PersonalInfoRepositoryImpl
import com.example.logifitappp.data.repository.SleepWrittenDataRepositoryImpl
import com.example.logifitappp.domain.repository.HealthInfoRepository
import com.example.logifitappp.domain.repository.OccupationalInfoRepository
import com.example.logifitappp.domain.repository.PersonalInfoRepository
import com.example.logifitappp.domain.repository.SleepWrittenDataRepository
import com.example.logifitappp.domain.service.SleepWrittenDataService
import com.example.logifitappp.domain.usecase.GetOccupationalInfoUseCase
import com.example.logifitappp.domain.usecase.GetPersonalInfoUseCase
import com.example.logifitappp.domain.usecase.HealthInfoUseCase
import com.example.logifitappp.utils.Constants.BASE_URL
import com.example.logifitappp.viewmodel.views.HealthInfo.HealthInfoViewModel
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoViewModel
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
    fun provideAdminApi(retrofit: Retrofit): AdminApi = retrofit.create(AdminApi::class.java)

    @Provides
    @Singleton
    fun provideAudioApi(retrofit: Retrofit): AudioApi = retrofit.create(AudioApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideDocumentTypeApi(retrofit: Retrofit): DocumentTypeApi = retrofit.create(DocumentTypeApi::class.java)

    @Provides
    @Singleton
    fun provideEvaluationApi(retrofit: Retrofit): EvaluationApi = retrofit.create(EvaluationApi::class.java)

    @Provides
    @Singleton
    fun provideLessonApi(retrofit: Retrofit): LessonApi = retrofit.create(LessonApi::class.java)

    @Provides
    @Singleton
    fun provideLocationApi(retrofit: Retrofit): LocationApi = retrofit.create(LocationApi::class.java)

    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi = retrofit.create(NotificationApi::class.java)

    @Provides
    @Singleton
    fun provideSleepApi(retrofit: Retrofit): SleepApi = retrofit.create(SleepApi::class.java)

    @Provides
    @Singleton
    fun provideTenantApi(retrofit: Retrofit): TenantApi = retrofit.create(TenantApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideTrainingApi(retrofit: Retrofit): TrainingApi = retrofit.create(TrainingApi::class.java)

    @Provides
    @Singleton
    fun provideWearableApi(retrofit: Retrofit): WearableApi = retrofit.create(WearableApi::class.java)


    @Provides
    @Singleton
    fun providesUserDao(@ApplicationContext context: Context) = App.database.userDao()

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
    fun providePersonalInfoRepository(context: Context): PersonalInfoRepository {
        return PersonalInfoRepositoryImpl(context)
    }

    @Provides
    fun provideGetPersonalInfoUseCase(repository: PersonalInfoRepository): GetPersonalInfoUseCase {
        return GetPersonalInfoUseCase(repository)
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
    fun provideSleepWrittenDataRepositoryImpl(api: SleepWrittenDataApi) = SleepWrittenDataRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideSleepWrittenDataRepository(repositoryImpl: SleepWrittenDataRepositoryImpl): SleepWrittenDataRepository = repositoryImpl

    @Provides
    @Singleton
    fun provideSleepWrittenDataService(repository: SleepWrittenDataRepository) = SleepWrittenDataService(repository)

    @Provides
    @Singleton
    fun provideSleepWrittenDataApi(retrofit: Retrofit): SleepWrittenDataApi = retrofit.create(SleepWrittenDataApi::class.java)


}