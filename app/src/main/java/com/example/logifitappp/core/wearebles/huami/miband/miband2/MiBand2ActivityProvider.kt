package com.example.logifitappp.core.wearebles.huami.miband.miband2

import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum
import com.example.logifitappp.core.wearebles.huami.HuamiConst
import com.example.logifitappp.core.wearebles.huami.miband.AbstractMiBandActivityProvider
import com.example.logifitappp.data.models.HuamiExtendedRawActivityModel

class MiBand2ActivityProvider(wearable: Wearable): AbstractMiBandActivityProvider(wearable) {
    private fun determinePreviousValidActivityType(activity: HuamiExtendedRawActivityModel): Int {
        val result = this.getWearableRawActivityDao().findPreviousValidRawActivity(activity.wearableId, activity.timestamp)

        if (result != null) return result.type and 0xf

        return HuamiConst.TYPE_UNSET
    }

    override fun getWearableRawActivityDao() = App.database.huamiExtendedRawActivityDao()

    override fun getRawActivitiesBetween(from: Long, to: Long): List<HuamiExtendedRawActivityModel> {
        val activities = super.getRawActivitiesBetween(from, to)
        postProcess(activities)

        return activities
    }

    override fun normalizeType(type: Int) = when (type) {
        HuamiConst.TYPE_DEEP_SLEEP -> WearableActivityTypeEnum.DEEP_SLEEP
        HuamiConst.TYPE_LIGHT_SLEEP -> WearableActivityTypeEnum.LIGHT_SLEEP
        HuamiConst.TYPE_ACTIVITY, HuamiConst.TYPE_RUNNING, HuamiConst.TYPE_WAKE_UP -> WearableActivityTypeEnum.ACTIVITY
        HuamiConst.TYPE_NONWEAR, HuamiConst.TYPE_CHARGING -> WearableActivityTypeEnum.NOT_WORN
        HuamiConst.TYPE_RIDE_BIKE -> WearableActivityTypeEnum.CYCLING
        else -> WearableActivityTypeEnum.UNKNOWN
    }

    private fun postProcess(activities: List<HuamiExtendedRawActivityModel>) {
        if (activities.isEmpty()) return

        var lastValidType = determinePreviousValidActivityType(activities[0])

        activities.forEach {
            var type = it.type

            if (type != HuamiConst.TYPE_UNSET) {
                type = type and 0xf
                it.type = type
            }

            when (type) {
                HuamiConst.TYPE_IGNORE, HuamiConst.TYPE_NO_CHANGE -> {
                    if (lastValidType != HuamiConst.TYPE_UNSET) it.type = lastValidType
                }

                else -> lastValidType = type
            }
        }
    }
}