package com.example.logifitappp.viewmodel.views.graphics

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.data.models.HeartRateDetailModel
import com.example.logifitappp.data.models.HeartRateFragmentModel
import com.example.logifitappp.domain.service.HeartRateService
import kotlinx.coroutines.launch
import java.time.LocalDate


class HeartRateDetailViewModel : ViewModel() {
    private val _graphState = mutableStateOf(HeartRateDetailModel(
        averageHeartRate = 0.0,
        date = LocalDate.now(),
        latestMeasuredHeartRate = 0,
        maxMeasuredHeartRate = 0,
        minMeasuredHeartRate = 0,
        rangeTimeInformation = ""
    ))


    val graphState: HeartRateDetailModel get() = _graphState.value

    fun fetchHeartRateInformation(date: LocalDate) {
         val heartRateService = HeartRateService()

        viewModelScope.launch {
            try {
                _graphState.value = _graphState.value.copy(
                    //averageHeartRate = heartRateService.calculateAverageHeartRate(),
                )
            } catch (e: Exception) {
                Log.e("HeartRateDetailViewModel", "Error de red: ${e.message}")
            }
        }
    }

    fun handlePressHeartRateFragment(fragment: HeartRateFragmentModel) {
        _graphState.value = _graphState.value.copy(
            maxMeasuredHeartRate = fragment.max,
            minMeasuredHeartRate = fragment.min,
            rangeTimeInformation = "${fragment.startAt} - ${fragment.endAt}"
        )
    }

    fun handleChangeDate(date: LocalDate) {
        _graphState.value = _graphState.value.copy(date = date)
        fetchHeartRateInformation(date)
    }
}
