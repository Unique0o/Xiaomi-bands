package com.example.logifitappp.viewmodel.states

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.core.analyzers.Spo2AmountList
import com.example.logifitappp.core.graphics.Spo2DataSet
import com.example.logifitappp.core.wearebles.Wearable

data class Spo2DetailState(
    var canGoToNextDay: Boolean = false,
    var date: Calendar = GregorianCalendar.getInstance(),
    var spo2DataSet: Spo2DataSet = Spo2DataSet(Spo2AmountList()),
    val subtitle: String = "N/A",
    var wearable: Wearable? = null
)
