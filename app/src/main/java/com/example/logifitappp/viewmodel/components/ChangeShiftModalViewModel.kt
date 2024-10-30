package com.example.logifitappp.viewmodel.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.ShiftModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = ChangeShiftModalViewModel.ChangeShiftModalViewModelFactory::class)
class ChangeShiftModalViewModel @AssistedInject constructor(
    @Assisted val shiftId: Int?
): ViewModel() {
    @AssistedFactory
    interface ChangeShiftModalViewModelFactory {
        fun create(shiftId: Int?): ChangeShiftModalViewModel
    }

    var shift by mutableStateOf<ShiftModel?>(null)

    var shifts = mutableStateListOf <ShiftModel>()
        private set

    init {
        val user = App.database.userDao().getLoggedIn()!!

        shifts.addAll(App.database.shiftDao().all(user.tenantId))
        fetchShift()
    }

    fun fetchShift() {
        shift = shifts.find { it.id == shiftId }
    }
}