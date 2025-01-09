package com.example.logifitappp.viewmodel.states

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.core.analyzers.HeartRateAmountList
import com.example.logifitappp.core.graphics.HeartRateDataSet
import com.example.logifitappp.core.wearebles.Wearable

data class HeartRateDetailState(
    var canGoToNextDay: Boolean = false,
    var date: Calendar = GregorianCalendar.getInstance(),
    val heartRateDataSet: HeartRateDataSet = HeartRateDataSet(HeartRateAmountList()),
    val subtitle: String = "N/A",
    var wearable: Wearable? = null
)
