package com.example.logifitappp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.logifitappp.data.dao.CountryDao
import com.example.logifitappp.data.dao.EvaluationResultDao
import com.example.logifitappp.data.dao.GroupDao
import com.example.logifitappp.data.dao.LocationDao
import com.example.logifitappp.data.dao.RestParameterDao
import com.example.logifitappp.data.dao.ShiftDao
import com.example.logifitappp.data.dao.SleepConditionDao
import com.example.logifitappp.data.dao.TenantDao
import com.example.logifitappp.data.dao.UserDao
import com.example.logifitappp.data.dao.WearableDao
import com.example.logifitappp.data.dao.XiaomiRawActivityDao
import com.example.logifitappp.data.dao.XiaomiSleepStageDao
import com.example.logifitappp.data.dao.XiaomiSleepTimeDao
import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.data.models.EvaluationResultModel
import com.example.logifitappp.data.models.GroupModel
import com.example.logifitappp.data.models.LocationModel
import com.example.logifitappp.data.models.RestParameterModel
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.SleepConditionModel
import com.example.logifitappp.data.models.SleepModel
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.models.WearableModel
import com.example.logifitappp.data.models.XiaomiRawActivityModel
import com.example.logifitappp.data.models.XiaomiSleepStageModel
import com.example.logifitappp.data.models.XiaomiSleepTimeModel

@Database(
    entities = [
        EvaluationResultModel::class,
        GroupModel::class,
        LocationModel::class,
        RestParameterModel::class,
        ShiftModel::class,
        SleepModel::class,
        SleepConditionModel::class,
        TenantModel::class,
        UserModel::class,
        WearableModel::class,
        XiaomiRawActivityModel::class,
        XiaomiSleepStageModel::class,
        XiaomiSleepTimeModel::class,
        CountryModel::class
    ],

    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun evaluationResultDao(): EvaluationResultDao
    abstract fun groupDao(): GroupDao
    abstract fun locationDao(): LocationDao
    abstract fun restParameterDao(): RestParameterDao
    abstract fun shiftDao(): ShiftDao
    abstract fun sleepConditionDao(): SleepConditionDao
    abstract fun tenantDao(): TenantDao
    abstract fun userDao(): UserDao
    abstract fun wearableDao(): WearableDao
    abstract fun xiaomiRawActivityDao(): XiaomiRawActivityDao
    abstract fun xiaomiSleepStageDao(): XiaomiSleepStageDao
    abstract fun xiaomiSleepTimeDao(): XiaomiSleepTimeDao
    abstract fun countryDao() : CountryDao
}