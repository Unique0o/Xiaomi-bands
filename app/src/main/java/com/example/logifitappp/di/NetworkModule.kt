package com.example.logifitappp.di

import com.example.logifitappp.data.repository.UserRepositoryImpl
import com.example.logifitappp.di.repositories.AuthRepository
import com.example.logifitappp.domain.repository.UserRepository
import com.example.logifitappp.domain.service.AuthService
import com.example.logifitappp.domain.usecase.LoginUseCase
import com.example.logifitappp.utils.Constants.BASE_URL
import dagger.Binds
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
    fun provideAuthRepository(retrofit: Retrofit): AuthRepository = retrofit.create(AuthRepository::class.java)

    @Provides
    @Singleton
    fun provideAuthService(authRepository: AuthRepository) = AuthService(authRepository)

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
}