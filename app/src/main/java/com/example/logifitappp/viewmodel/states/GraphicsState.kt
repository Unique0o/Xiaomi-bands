package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.analyzers.HeartRateAmountList
import com.example.logifitappp.core.analyzers.StepsAmountList
import com.example.logifitappp.core.graphics.HeartRateDataSet
import com.example.logifitappp.core.graphics.SleepBarDataSet
import com.example.logifitappp.core.graphics.StepsBarDataSet
import com.example.logifitappp.data.models.ShiftModel

data class GraphicsState(
    val heartRateDataSet: HeartRateDataSet = HeartRateDataSet(HeartRateAmountList()),
    val shift: ShiftModel? = null,
    val sleepDataSet: SleepBarDataSet = SleepBarDataSet(ActivityAmountList()),
    val stepsDataset: StepsBarDataSet = StepsBarDataSet(StepsAmountList())
)