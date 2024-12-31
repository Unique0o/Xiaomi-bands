package com.example.logifitappp.core.wearebles.huami

import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableActivityProvider
import com.example.logifitappp.enums.WearableActivityTypeEnum
import com.example.logifitappp.data.models.HuamiExtendedRawActivityModel

class HuamiExtendedActivityProvider(wearable: Wearable): WearableActivityProvider<HuamiExtendedRawActivityModel>(wearable) {
    override fun getWearableRawActivityDao() = App.database.huamiExtendedRawActivityDao()

    override fun getRawActivitiesBetween(from: Long, to: Long): List<HuamiExtendedRawActivityModel> {
        val activities = super.getRawActivitiesBetween(from, to)
        postProcess(activities)

        return activities
    }

    override fun normalizeIntensity(intensity: Int) = intensity / 256.0f

    override fun normalizeType(type: Int) = when (type) {
        TYPE_OUTDOOR_RUNNING -> WearableActivityTypeEnum.RUNNING
        TYPE_NOT_WORN, TYPE_CHARGING -> WearableActivityTypeEnum.NOT_WORN
        TYPE_SLEEP -> WearableActivityTypeEnum.LIGHT_SLEEP
        TYPE_CUSTOM_DEEP_SLEEP -> WearableActivityTypeEnum.DEEP_SLEEP
        TYPE_CUSTOM_REM_SLEEP -> WearableActivityTypeEnum.REM_SLEEP
        else -> WearableActivityTypeEnum.UNKNOWN
    }

    private fun postProcess(activities: List<HuamiExtendedRawActivityModel>) {
        if (activities.isEmpty()) return

        var lastValidType: Int? = null

        for (i in activities.indices) {
            val type = activities[i].type
            val intensity = activities[i].intensity

            when (type) {
                80 -> run {
                    if (!(intensity <= 5 && activities[i].sleep >= 5)) return@run

                    if (lastValidType == type) {
                        activities[i - 1].type = TYPE_SLEEP
                        activities[i - 1].intensity = activities[i - 1].sleep
                    }

                    activities[i].type = TYPE_SLEEP
                    activities[i].intensity = activities[i].sleep
                }

                88, 112 -> run {
                    if (!((lastValidType == type && activities[i - 1].type == TYPE_SLEEP) || intensity == 0)) return@run

                    activities[i].type = TYPE_SLEEP
                    activities[i].intensity = activities[i].sleep
                }

                TYPE_SLEEP -> {
                    activities[i].deepSleep =  activities[i].deepSleep and 127
                    activities[i].remSleep =  activities[i].remSleep and 127

                    if (activities[i].remSleep > 55) {
                        activities[i].type = TYPE_CUSTOM_REM_SLEEP
                        activities[i].intensity = activities[i].remSleep
                    } else if (activities[i].deepSleep > 42) {
                        activities[i].type = TYPE_CUSTOM_DEEP_SLEEP
                        activities[i].intensity = activities[i].deepSleep
                    } else activities[i].intensity = activities[i].sleep
                }
            }

            lastValidType = type
        }
    }

    companion object {
        const val TYPE_CHARGING: Int = 118
        const val TYPE_CUSTOM_UNSET: Int = -1
        const val TYPE_NOT_WORN: Int = 115
        const val TYPE_OUTDOOR_RUNNING: Int = 64
        const val TYPE_SLEEP: Int = 120
        const val TYPE_CUSTOM_DEEP_SLEEP: Int = TYPE_SLEEP + 1
        const val TYPE_CUSTOM_REM_SLEEP: Int = TYPE_SLEEP + 2
    }
}