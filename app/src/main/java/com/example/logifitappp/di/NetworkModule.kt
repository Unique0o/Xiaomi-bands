package com.example.logifitappp.di

import com.example.logifitappp.data.remote.api.AuthApi
import com.example.logifitappp.data.repository.AuthRepositoryImpl
import com.example.logifitappp.data.repository.UserRepositoryImpl
import com.example.logifitappp.domain.repository.AuthRepository
import com.example.logifitappp.domain.repository.UserRepository
import com.example.logifitappp.domain.service.AuthService
import com.example.logifitappp.domain.usecase.LoginUseCase
import com.example.logifitappp.domain.usecase.RecoverPasswordUseCase
import com.example.logifitappp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideAuthRepositoryImpl(authApi: AuthApi): AuthRepositoryImpl = AuthRepositoryImpl(authApi)

    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository = authRepositoryImpl


    @Provides
    @Singleton
    fun provideAuthService(authRepository: AuthRepository): AuthService = AuthService(authRepository)

    @Provides
    @Singleton
    fun provideLoginUseCase(authService: AuthService): LoginUseCase {
        return LoginUseCase(authService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository {
        return userRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideRecoverPasswordUseCase(authService: AuthService): RecoverPasswordUseCase {
        return RecoverPasswordUseCase(authService)
    }
}