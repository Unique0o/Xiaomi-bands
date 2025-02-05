package com.example.logifitappp.viewmodel.states

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.core.analyzers.StepsAmountList
import com.example.logifitappp.core.graphics.StepsDataSet
import com.example.logifitappp.core.wearebles.Wearable

data class StepsDetailState(
    var canGoToNextDay: Boolean = false,
    var date: Calendar = GregorianCalendar.getInstance(),
    val stepsDataSet: StepsDataSet = StepsDataSet(StepsAmountList()),
    val subtitle: String = "N/A",
    var wearable: Wearable? = null
)
