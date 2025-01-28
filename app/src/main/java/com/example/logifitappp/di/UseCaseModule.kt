package com.example.logifitappp.di

import com.example.logifitappp.domain.service.AuthService
import com.example.logifitappp.domain.service.DocumentTypeService
import com.example.logifitappp.domain.service.EvaluationService
import com.example.logifitappp.domain.service.LocationService
import com.example.logifitappp.domain.service.SleepService
import com.example.logifitappp.domain.service.TenantService
import com.example.logifitappp.domain.service.TrainingService
import com.example.logifitappp.domain.service.UserService
import com.example.logifitappp.domain.usecase.CalculateFatigueUseCase
import com.example.logifitappp.domain.usecase.CalculateSleepProcessingUseCase
import com.example.logifitappp.domain.usecase.FetchActivitiesByShiftUseCase
import com.example.logifitappp.domain.usecase.FetchActivityAmountsBetweenDayUseCase
import com.example.logifitappp.domain.usecase.FetchActivityAmountsByShiftUseCase
import com.example.logifitappp.domain.usecase.FetchNapAmountsByShiftUseCase
import com.example.logifitappp.domain.usecase.FetchSpo2SampleAmountsBetweenDayUseCase
import com.example.logifitappp.domain.usecase.FetchStressSampleAmountBetweenDayUseCase
import com.example.logifitappp.domain.usecase.FindAppropriateSleepConditionUseCase
import com.example.logifitappp.domain.usecase.LoadAppWhenAnUserIsAuthenticatedUseCase
import com.example.logifitappp.domain.usecase.LoginUseCase
import com.example.logifitappp.domain.usecase.ProcessSynchronizedWearableDataUseCase
import com.example.logifitappp.domain.usecase.SendWearableInformationToLogifitUseCase
import com.example.logifitappp.domain.usecase.ShareEvaluationDetailUseCase
import com.example.logifitappp.domain.usecase.ShareTrainingCertificateUseCase
import com.example.logifitappp.domain.usecase.SynchronizeWearableUseCase
import com.example.logifitappp.domain.usecase.UpdateNotificationTokenUseCase
import com.example.logifitappp.domain.usecase.UpdatePersonalInformationFormDataUseCase
import com.example.logifitappp.domain.usecase.UpdateTenantInformationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideCalculateFatigueUseCase() = CalculateFatigueUseCase()

    @Provides
    @Singleton
    fun provideCalculateSleepProcessingUseCase(
        calculateFatigueUseCase: CalculateFatigueUseCase
    ) = CalculateSleepProcessingUseCase(calculateFatigueUseCase)

    @Provides
    @Singleton
    fun provideFetchActivitiesByShiftUseCase() = FetchActivitiesByShiftUseCase()

    @Provides
    @Singleton
    fun provideFetchActivityAmountsFromLast24hUseCase() = FetchActivityAmountsBetweenDayUseCase()

    @Provides
    @Singleton
    fun provideFetchActivityAmountsByShiftUseCase(
        fetchActivitiesByShiftUseCase: FetchActivitiesByShiftUseCase
    ) = FetchActivityAmountsByShiftUseCase(fetchActivitiesByShiftUseCase)

    @Provides
    @Singleton
    fun provideFetchNapAmountsByShiftUseCase() = FetchNapAmountsByShiftUseCase()

    @Provides
    @Singleton
    fun provideFetchSpo2SamplesBetweenDayUseCase() = FetchSpo2SampleAmountsBetweenDayUseCase()

    @Provides
    @Singleton
    fun provideFetchStressSampleAmountBetweenDayUseCase() = FetchStressSampleAmountBetweenDayUseCase()

    @Provides
    @Singleton
    fun provideFindAppropriateSleepConditionUseCase() = FindAppropriateSleepConditionUseCase()

    @Provides
    @Singleton
    fun provideLoadAppWhenAnUserIsAuthenticatedUseCase(
        userService: UserService,
        updateNotificationTokenUseCase: UpdateNotificationTokenUseCase,
        updateTenantInformationUseCase: UpdateTenantInformationUseCase
    ) = LoadAppWhenAnUserIsAuthenticatedUseCase(userService, updateNotificationTokenUseCase, updateTenantInformationUseCase)

    @Provides
    @Singleton
    fun provideLoginUseCase(
        authService: AuthService,
        updateNotificationTokenUseCase: UpdateNotificationTokenUseCase,
        updateTenantInformationUseCase: UpdateTenantInformationUseCase
    ) = LoginUseCase(authService, updateNotificationTokenUseCase, updateTenantInformationUseCase)

    @Provides
    @Singleton
    fun provideProcessSynchronizedWearableDataUseCase(
        calculateSleepProcessingUseCase: CalculateSleepProcessingUseCase
    ) = ProcessSynchronizedWearableDataUseCase(calculateSleepProcessingUseCase)

    @Provides
    @Singleton
    fun provideSendWearableInformationToLogifitUseCase(
        sleepService: SleepService
    ) = SendWearableInformationToLogifitUseCase(sleepService)

    @Provides
    @Singleton
    fun provideShareEvaluationDetailUseCase(
        evaluationService: EvaluationService
    ) = ShareEvaluationDetailUseCase(evaluationService)

    @Provides
    @Singleton
    fun provideShareTrainingCertificateUseCase(
        trainingService: TrainingService
    ) = ShareTrainingCertificateUseCase(trainingService)

    @Provides
    @Singleton
    fun provideSynchronizeWearableUseCase() = SynchronizeWearableUseCase()

    @Provides
    @Singleton
    fun provideUpdateNotificationTokenUseCase(authService: AuthService) = UpdateNotificationTokenUseCase(authService)

    @Provides
    @Singleton
    fun provideUpdatePersonalInformationFormDataUseCase(
        documentTypeService: DocumentTypeService,
        locationService: LocationService
    ) = UpdatePersonalInformationFormDataUseCase(documentTypeService, locationService)

    @Provides
    @Singleton
    fun provideUpdateTenantInformationUseCase(
        tenantService: TenantService,
        evaluationService: EvaluationService
    ) = UpdateTenantInformationUseCase(tenantService, evaluationService)
}