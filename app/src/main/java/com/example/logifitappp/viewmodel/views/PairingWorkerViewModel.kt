package com.example.logifitappp.viewmodel.views

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.AssociateWearableRequest
import com.example.logifitappp.data.remote.dto.response.WorkerItemResponse
import com.example.logifitappp.domain.service.WearableService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.states.PairingWorkerState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PairingWorkerViewModel.PairingWorkerViewModelFactory::class)
class PairingWorkerViewModel @AssistedInject constructor(
    @Assisted private val navigation: NavHostController,
    @Assisted private val mac: String,
    @Assisted private val user: UserModel?,
    private val wearableService: WearableService
): ViewModel() {
    @AssistedFactory
    interface PairingWorkerViewModelFactory {
        fun create(navigation: NavHostController, mac: String, user: UserModel?): PairingWorkerViewModel
    }

    var state by mutableStateOf(PairingWorkerState())
        private set

    init {
        state = state.copy(wearable = App.wearableManager.getWearableByMac(mac))
    }

    fun handleSelectedWorker(worker: WorkerItemResponse?) {
        state = state.copy(selectedWorker = worker)
    }

    fun pair() {
        if (state.selectedWorker == null) return

        if (state.wearable == null) return

        if (user == null) return

        viewModelScope.launch {
            try {
                state = state.copy(
                    isPairing = true,
                    pairingStatus = AppStatusCodeEnum.ASSOCIATING_WORKER
                )

                wearableService.associate(state.selectedWorker!!.id, AssociateWearableRequest(
                    device_mac = state.wearable!!.getAddress()!!,
                    oper_system = "Android",
                    oper_system_version = Build.VERSION.RELEASE,
                    phone_brand = Build.BRAND,
                    phone_model = Build.MODEL
                ))

                val model = App.database.wearableDao().find(state.wearable!!.getAddress()!!, user.id)!!
                model.alias = "${state.selectedWorker!!.firstName} ${state.selectedWorker!!.lastName}"

                App.database.wearableDao().store(model)

                state.wearable?.setAlias(model.alias)
                state = state.copy(pairingStatus = AppStatusCodeEnum.SUCCESSFUL_WORKER_ASSOCIATED)
            } catch (e: HttpConsumerException) {
                state = state.copy(pairingStatus = e.getStatus())
            }
        }
    }

    fun stopProcessing() {
        state = state.copy(isPairing = false)

        if (state.pairingStatus == AppStatusCodeEnum.SUCCESSFUL_WORKER_ASSOCIATED) navigation.popBackStack()
    }
}