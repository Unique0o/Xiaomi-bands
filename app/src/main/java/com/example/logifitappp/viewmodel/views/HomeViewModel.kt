package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.core.RecordedDataTypesEnum
import com.example.logifitappp.core.analyzers.ActivityAmount
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableUpdateSubjectEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

): ViewModel() {
    var isLoading by mutableStateOf(false)

    var sleeps = mutableStateListOf<ActivityAmount>()
        private set

    var wearables = mutableStateListOf<Wearable>()
        private set

    fun connect(wearable: Wearable) {
        isLoading = true

        if (!wearable.getWearableCoordinator().isConnectable()) {
            wearable.setState(Wearable.State.WAITING_FOR_SCAN)
            wearable.sendDeviceUpdateIntent(App.context, WearableUpdateSubjectEnum.CONNECTION_STATE)

            return
        }

        App.getWearableServiceTo(wearable).connect()
    }

    fun fetchActivities(wearable: Wearable) {
        isLoading = true

        App.getWearableServiceTo(wearable).onFetchRecordedData(RecordedDataTypesEnum.TYPE_SYNC)
    }

    private fun fetchSleeps() {
        val now = GregorianCalendar.getInstance()
        val endTs = (now.timeInMillis / 1000).toInt()
        val startTs = endTs - 24 * 60 * 60 - 1

        val analyzer = ActivityAnalyzer()
        val activities = wearables[0].getWearableCoordinator().getActivityProvider(wearables[0]).getRawActivitiesBetween(startTs, endTs)

        sleeps.clear()
        sleeps.addAll(analyzer.calculateSleepAmounts(activities))

        isLoading = false
    }

    fun refreshPairedWearables() {
        wearables.clear()
        wearables.addAll(App.wearableManager.getWearables())

        if (wearables.isNotEmpty()) fetchSleeps()
    }

    fun refreshSingleWearable(wearable: Wearable) {
        val index = wearables.indexOf(wearable)

        if (index > 0) {
            wearables[index].copyFromDevice(wearable)
            fetchSleeps()
        } else refreshPairedWearables()
    }
}