package com.example.logifitappp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.logifitappp.data.dao.UserDao
import com.example.logifitappp.data.dao.WearableDao
import com.example.logifitappp.data.dao.XiaomiRawActivityDao
import com.example.logifitappp.data.dao.XiaomiSleepStageDao
import com.example.logifitappp.data.dao.XiaomiSleepTimeDao
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.models.WearableModel
import com.example.logifitappp.data.models.XiaomiRawActivityModel
import com.example.logifitappp.data.models.XiaomiSleepStageModel
import com.example.logifitappp.data.models.XiaomiSleepTimeModel

@Database(
    entities = [
        UserModel::class,
        WearableModel::class,
        XiaomiRawActivityModel::class,
        XiaomiSleepStageModel::class,
        XiaomiSleepTimeModel::class
    ],

    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun wearableDao(): WearableDao
    abstract fun xiaomiRawActivityDao(): XiaomiRawActivityDao
    abstract fun xiaomiSleepStageDao(): XiaomiSleepStageDao
    abstract fun xiaomiSleepTimeDao(): XiaomiSleepTimeDao
}