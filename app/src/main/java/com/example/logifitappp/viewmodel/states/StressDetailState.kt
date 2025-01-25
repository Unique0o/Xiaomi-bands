package com.example.logifitappp.viewmodel.states

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.core.analyzers.StressAmountList
import com.example.logifitappp.core.graphics.StressDataSet
import com.example.logifitappp.core.wearebles.Wearable

data class StressDetailState(
    var canGoToNextDay: Boolean = false,
    var date: Calendar = GregorianCalendar.getInstance(),
    var stressDataSet: StressDataSet = StressDataSet(StressAmountList()),
    val subtitle: String = "N/A",
    var wearable: Wearable? = null
)