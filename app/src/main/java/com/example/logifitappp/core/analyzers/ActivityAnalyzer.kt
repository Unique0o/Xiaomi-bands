package com.example.logifitappp.core.analyzers

import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

class ActivityAnalyzer {
    fun calculateSleepAmounts(activities: List<WearableRawActivityModel>): List<ActivityAmount> {
        val amounts = mutableListOf<ActivityAmount>()
        var previousActivity: WearableRawActivityModel? = null

        activities.forEach {
            val amount = ActivityAmount(it.provider?.normalizeType(it.type) ?: WearableActivityTypeEnum.NOT_WORN)

            val isEmpty = amounts.isEmpty()
            var previousAmount = amounts.lastOrNull()

            if (previousAmount == null) {
                previousAmount = amount
                previousAmount.setStartDate(it.timestamp)
                previousAmount.setEndDate(it.timestamp)
                amounts.add(previousAmount)
            }

            if (previousActivity != null) {
               if (isEmpty) previousAmount.setStartDate(previousActivity!!.timestamp)

                val timeDifference = it.timestamp - previousActivity!!.timestamp

                if (previousActivity!!.type == it.type) {
                    previousAmount.addSeconds(timeDifference)
                    previousAmount.setEndDate(it.timestamp)
                } else {
                    val sharedTimeDifference = (timeDifference / 2.0f).toLong()

                    previousAmount.addSeconds(sharedTimeDifference)
                    previousAmount.setEndDate(it.timestamp - sharedTimeDifference)

                    amount.addSeconds(sharedTimeDifference)
                    amount.setStartDate(it.timestamp + sharedTimeDifference)
                    amount.setEndDate(it.timestamp + sharedTimeDifference)
                    amounts.add(amount)
                }
            }

            previousActivity = it
        }

        return amounts.filter { it.endDate.time != it.startDate.time }
    }
}