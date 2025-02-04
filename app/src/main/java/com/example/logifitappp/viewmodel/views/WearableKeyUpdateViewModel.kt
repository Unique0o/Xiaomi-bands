package com.example.logifitappp.viewmodel.views

import androidx.lifecycle.ViewModel
import com.example.logifitappp.domain.service.WearableService
import javax.inject.Inject

class WearableKeyUpdateViewModel @Inject constructor(
    private val wearableService: WearableService
): ViewModel() {

}