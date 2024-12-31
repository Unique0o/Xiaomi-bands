package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.analyzers.HeartRateAmountList
import com.example.logifitappp.core.analyzers.Spo2AmountList
import com.example.logifitappp.core.analyzers.StepsAmountList
import com.example.logifitappp.core.graphics.HeartRateDataSet
import com.example.logifitappp.core.graphics.SleepBarDataSet
import com.example.logifitappp.core.graphics.Spo2DataSet
import com.example.logifitappp.core.graphics.StepsBarDataSet
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.DrowsinessModel
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.enums.AppStatusCodeEnum

data class GraphicsState(
    val drowsiness: DrowsinessModel? = null,
    val heartRateDataSet: HeartRateDataSet = HeartRateDataSet(HeartRateAmountList()),
    val isLoading: Boolean = false,
    val shift: ShiftModel? = null,
    val sleepDataSet: SleepBarDataSet = SleepBarDataSet(ActivityAmountList()),
    val spo2DataSet: Spo2DataSet = Spo2DataSet(Spo2AmountList()),
    val stepsDataset: StepsBarDataSet = StepsBarDataSet(StepsAmountList()),
    val status: AppStatusCodeEnum = AppStatusCodeEnum.TRANSFERRING_WEARABLE_INFORMATION,
    val wearable: Wearable? = null
)