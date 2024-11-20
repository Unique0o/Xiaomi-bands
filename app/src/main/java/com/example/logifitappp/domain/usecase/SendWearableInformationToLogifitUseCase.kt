package com.example.logifitappp.domain.usecase

import android.icu.util.GregorianCalendar
import android.os.Build
import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.utils.AndroidUtils
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.core.utils.HeartRateUtils
import com.example.logifitappp.core.utils.StepsUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableActivityProvider
import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.WearableModel
import com.example.logifitappp.data.models.commons.WearableRawActivityModel
import com.example.logifitappp.data.remote.dto.requests.StoreFatigueMetadataRequest
import com.example.logifitappp.data.remote.dto.requests.StoreFatigueRequest
import com.example.logifitappp.data.remote.dto.requests.StoreHeartRateMetadataRequest
import com.example.logifitappp.data.remote.dto.requests.StoreHeartRateRequest
import com.example.logifitappp.data.remote.dto.requests.StoreRangeSleepRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepByMinuteRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepGraphicMetadataRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepGraphicRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepMetadataRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepRequest
import com.example.logifitappp.data.remote.dto.requests.StoreStepRequest
import com.example.logifitappp.domain.service.SleepService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.SynchronizationProcessingException
import javax.inject.Inject

class SendWearableInformationToLogifitUseCase @Inject constructor(
    private val sleepService: SleepService
) {
    suspend operator fun invoke(shift: ShiftModel?, wearable: Wearable): Int {
        if (shift == null) throw SynchronizationProcessingException(AppStatusCodeEnum.UNSELECTED_SHIFT)

        val provider = wearable
            .getWearableCoordinator()
            .getActivityProvider(wearable)

        val wearableModel = provider.getStoredWearable()!!
        val response = sleepService.store(structureSleeps(provider, shift, wearable, wearableModel))

        App.database.drowsinessDao().markAsSent(
            DateTimeUtils.formatExtendedIso8601(GregorianCalendar.getInstance().time),
            wearableModel.id
        )

        sleepService.storeHeartRates(structureHeartRates(provider, wearable))
        sleepService.storeGraphics(structureGraphics(provider, shift, wearable))

        App.database.fatigueDao().findFromToday(wearableModel.id)?.let {
            sleepService.storeFatigue(StoreFatigueRequest(
                fatigue = StoreFatigueMetadataRequest(
                    identifier = wearable.getAddress()!!,
                    reem_cycles = it.remCycles,
                    total_awake_time = it.totalAwakeSeconds.toString(),
                    total_reem_sleep = it.totalRemSeconds?.toString(),
                    total_sleep = it.totalSleepSeconds.toString(),
                    with_awakening_overcome = if (it.withAwakeningOvercome) 1 else 0,
                    with_hypertension = if (it.withHypertension) 1 else 0,
                    with_little_reem_sleep = if (it.withLittleReemSleep == true) 1 else 0,
                    with_little_sleep = if (it.withLittleSleep) 1 else 0,
                    with_long_awake =  if (it.withLongAwake) 1 else 0
                )
            ))
        }

        return response.points
    }

    private fun structureGraphics(
        provider: WearableActivityProvider<out WearableRawActivityModel>,
        shift: ShiftModel,
        wearable: Wearable
    ): StoreSleepGraphicRequest {
        val startTs = shift.getStartDateTimestamp()
        val endTs = shift.getEndDateTimestamp()

        val activities = provider.getRawActivities(shift)

        return StoreSleepGraphicRequest(
            sleep_graphics = StoreSleepGraphicMetadataRequest(
                dateEnd = DateTimeUtils.formatReducedIso8601(endTs.time),
                dateStart = DateTimeUtils.formatReducedIso8601(startTs.time),

                detail = activities.map {
                    val type = it.getNormalizedType()
                    val intensity = it.getNormalizedIntensity()

                    StoreSleepByMinuteRequest(
                        heartRate = it.heartRate,
                        intensity = if (type == WearableActivityTypeEnum.NOT_WORN) 0.01 else intensity + 0.01,
                        rawKind = type.getCode(),
                        steps = it.steps,
                        timestamp = it.timestamp.toString()
                    )
                },

                firmware = wearable.getFirmwareVersion(),
                identifier = wearable.getAddress()!!,
                oper_system = "Android",
                oper_system_version = Build.VERSION.RELEASE,
                phone_brand = Build.BRAND,
                phone_model = Build.MODEL
            )
        )
    }

    private fun structureHeartRates(
        provider: WearableActivityProvider<out WearableRawActivityModel>,
        wearable: Wearable
    ): StoreHeartRateRequest {
        val lastActivity = provider.findLastRawActivity()
        val lpm = lastActivity?.heartRate ?: 0

        return StoreHeartRateRequest(
            heart_rate = arrayOf(
                StoreHeartRateMetadataRequest(
                    level = HeartRateUtils.getMeasureLevelToLpm(lpm),
                    lpm = lpm,
                    identifier = wearable.getAddress()!!
                )
            ).toList()
        )
    }

    private fun structureSleeps(
        provider: WearableActivityProvider<out WearableRawActivityModel>,
        shift: ShiftModel,
        wearable: Wearable,
        wearableModel: WearableModel
    ): StoreSleepRequest {
        val sleeps = App.database.sleepDao().fetchFromToday(wearableModel.id)

        if (sleeps.isEmpty()) throw SynchronizationProcessingException(AppStatusCodeEnum.NO_WEARABLE_SLEEP_DATA)

        val analyzer = ActivityAnalyzer()
        val drowsinessToSend = App.database.drowsinessDao().fetchNotSentAndLast(wearableModel.id)

        val activities = provider.getRawActivitiesBetweenDay(GregorianCalendar.getInstance())
        val amounts = analyzer.calculate(activities)

        val distance = StepsUtils.calculateDistance(amounts.totalSteps)
        val kcal = StepsUtils.calculateKcal(amounts.totalSteps)

        return StoreSleepRequest(
            real_sleeps = drowsinessToSend.map {
                StoreRangeSleepRequest(
                    date = DateTimeUtils.parse(it.createdAt, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"),
                    realSleepIntervalText = DurationUtils.format(it.totalSleepSeconds),
                    realSleepIntervalValue = it.totalSleepSeconds.toString(),
                    version = AndroidUtils.getAppVersion(App.context)
                )
            },

            shift_id = shift.id,

            sleeps = sleeps.map {
                StoreSleepMetadataRequest(
                    battery = wearable.getBatteryLevel().toString(),
                    dateRegister = DateTimeUtils.parse(it.createdAt, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"),
                    deepSleepText =  DurationUtils.format(it.deepSleepSeconds),
                    deepSleepValue = it.deepSleepSeconds.toString(),
                    identifier = wearable.getAddress(),
                    interruptions = it.interruptions,
                    lightSleepText = DurationUtils.format(it.lightSleepSeconds),
                    lightSleepValue = it.lightSleepSeconds.toString(),
                    location_id = "1",
                    reemSleepText = DurationUtils.format(it.remSleepSeconds),
                    reemSleepValue = it.remSleepSeconds.toString(),
                    referenceAppId = 0,
                    sleepIni = it.startAt,
                    sleepEnd = it.endAt,
                    status = 1,
                    syncDate = it.createdAt,
                    totalSleepText = DurationUtils.format(it.totalSleepSeconds),
                    totalSleepValue = it.totalSleepSeconds.toString()
                )
            },

            steps = arrayOf(StoreStepRequest(
                caloriesValue = kcal,
                caloriesText = "$kcal Kcal",
                distanceText = StepsUtils.formatDistance(distance),
                distanceValue = distance,
                step = amounts.totalSteps
            )).toList()
        )
    }
}