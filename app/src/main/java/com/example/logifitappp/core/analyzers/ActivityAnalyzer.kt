package com.example.logifitappp.core.analyzers

import com.example.logifitappp.data.models.commons.WearableRawActivityModel
import java.util.Date

class ActivityAnalyzer {
    fun calculateActivityAmounts(activities: List<WearableRawActivityModel>): ActivityAmountList {
        val amounts = ActivityAmountList()
        var previousActivity: WearableRawActivityModel? = null

        activities.forEach {
            println("activity ${Date(it.timestamp * 1000)}: $it")
            val amount = ActivityAmount(it.getNormalizedType())

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

                previousAmount.addSeconds(timeDifference)
                previousAmount.addSteps(it.steps)
                previousAmount.setEndDate(it.timestamp)

                if (previousActivity!!.type != it.type) {
                    amount.setStartDate(it.timestamp)
                    amount.setEndDate(it.timestamp)
                    amounts.add(amount)
                }
            }

            previousActivity = it
        }

        amounts.calculateInformation()

        return amounts
    }
}