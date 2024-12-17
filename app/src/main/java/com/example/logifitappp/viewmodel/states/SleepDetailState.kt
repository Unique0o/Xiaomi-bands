package com.example.logifitappp.viewmodel.states

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.graphics.SleepBarDataSet
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel

data class SleepDetailState(
    var canGoToNextDay: Boolean = false,
    var date: Calendar = GregorianCalendar.getInstance(),
    var shift: ShiftModel? = null,
    val sleepDataSet: SleepBarDataSet = SleepBarDataSet(ActivityAmountList()),
    val subtitle: String = DurationUtils.format(0L),
    val summary: String = "",
    val title: String = App.context.getString(R.string.sleep_time),
    var wearable: Wearable? = null
)