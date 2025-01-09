package com.example.logifitappp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.logifitappp.data.dao.CountryDao
import com.example.logifitappp.data.dao.DepartmentDao
import com.example.logifitappp.data.dao.DocumentTypeDao
import com.example.logifitappp.data.dao.DrowsinessDao
import com.example.logifitappp.data.dao.EvaluationResultDao
import com.example.logifitappp.data.dao.FatigueDao
import com.example.logifitappp.data.dao.FitProRawActivityDao
import com.example.logifitappp.data.dao.GroupDao
import com.example.logifitappp.data.dao.HuamiExtendedRawActivityDao
import com.example.logifitappp.data.dao.HuamiSpo2SampleDao
import com.example.logifitappp.data.dao.LocationDao
import com.example.logifitappp.data.dao.ProvinceDao
import com.example.logifitappp.data.dao.RestParameterDao
import com.example.logifitappp.data.dao.RosterLocationDao
import com.example.logifitappp.data.dao.ShiftDao
import com.example.logifitappp.data.dao.SleepConditionDao
import com.example.logifitappp.data.dao.SleepDao
import com.example.logifitappp.data.dao.TenantDao
import com.example.logifitappp.data.dao.UserDao
import com.example.logifitappp.data.dao.WearableDao
import com.example.logifitappp.data.dao.XiaomiRawActivityDao
import com.example.logifitappp.data.dao.XiaomiSleepStageDao
import com.example.logifitappp.data.dao.XiaomiSleepTimeDao
import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.data.models.DepartmentModel
import com.example.logifitappp.data.models.DocumentTypeModel
import com.example.logifitappp.data.models.DrowsinessModel
import com.example.logifitappp.data.models.EvaluationResultModel
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.models.FitProRawActivityModel
import com.example.logifitappp.data.models.GroupModel
import com.example.logifitappp.data.models.HuamiExtendedRawActivityModel
import com.example.logifitappp.data.models.HuamiSpo2SampleModel
import com.example.logifitappp.data.models.LocationModel
import com.example.logifitappp.data.models.ProvinceModel
import com.example.logifitappp.data.models.RestParameterModel
import com.example.logifitappp.data.models.RosterLocationModel
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
        CountryModel::class,
        DepartmentModel::class,
        DocumentTypeModel::class,
        DrowsinessModel::class,
        EvaluationResultModel::class,
        FatigueModel::class,
        FitProRawActivityModel::class,
        GroupModel::class,
        HuamiExtendedRawActivityModel::class,
        HuamiSpo2SampleModel::class,
        LocationModel::class,
        ProvinceModel::class,
        RestParameterModel::class,
        RosterLocationModel::class,
        ShiftModel::class,
        SleepModel::class,
        SleepConditionModel::class,
        TenantModel::class,
        UserModel::class,
        WearableModel::class,
        XiaomiRawActivityModel::class,
        XiaomiSleepStageModel::class,
        XiaomiSleepTimeModel::class
    ],

    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun documentTypeDao(): DocumentTypeDao
    abstract fun drowsinessDao(): DrowsinessDao
    abstract fun evaluationResultDao(): EvaluationResultDao
    abstract fun fatigueDao(): FatigueDao
    abstract fun fitProRawActivityDao(): FitProRawActivityDao
    abstract fun groupDao(): GroupDao
    abstract fun huamiExtendedRawActivityDao(): HuamiExtendedRawActivityDao
    abstract fun huamiSpo2SampleDao(): HuamiSpo2SampleDao
    abstract fun locationDao(): LocationDao
    abstract fun provinceDao(): ProvinceDao
    abstract fun restParameterDao(): RestParameterDao
    abstract fun rosterLocationDao(): RosterLocationDao
    abstract fun shiftDao(): ShiftDao
    abstract fun sleepDao(): SleepDao
    abstract fun sleepConditionDao(): SleepConditionDao
    abstract fun tenantDao(): TenantDao
    abstract fun userDao(): UserDao
    abstract fun wearableDao(): WearableDao
    abstract fun xiaomiRawActivityDao(): XiaomiRawActivityDao
    abstract fun xiaomiSleepStageDao(): XiaomiSleepStageDao
    abstract fun xiaomiSleepTimeDao(): XiaomiSleepTimeDao
}