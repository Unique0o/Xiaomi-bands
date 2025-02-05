package com.example.logifitappp.viewmodel.states

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.graphics.SleepDataSet
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.SleepConditionModel
import com.example.logifitappp.data.models.TenantModel

data class SleepDetailState(
    var canGoToNextDay: Boolean = false,
    var date: Calendar = GregorianCalendar.getInstance(),
    val fatigue: FatigueModel? = null,
    val nap: ActivityAmountList = ActivityAmountList(),
    var shift: ShiftModel? = null,
    val sleepCondition: SleepConditionModel? = null,
    val sleepDataSet: SleepDataSet = SleepDataSet(ActivityAmountList()),
    val subtitle: String = DurationUtils.format(0L),
    val summary: String = "",
    val tenant: TenantModel? = null,
    val title: String = App.context.getString(R.string.sleep_time),
    var wearable: Wearable? = null
)