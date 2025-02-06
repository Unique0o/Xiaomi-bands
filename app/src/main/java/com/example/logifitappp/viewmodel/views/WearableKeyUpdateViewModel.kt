package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.data.remote.dto.requests.StoreXiaomiMacRequest
import com.example.logifitappp.data.remote.dto.requests.XiaomiMac
import com.example.logifitappp.data.remote.dto.response.FetchXiaomiCredentialSelectableItem
import com.example.logifitappp.data.remote.dto.response.FetchXiaomiMacResponse
import com.example.logifitappp.domain.service.WearableService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.ui.components.BottomSheetSelectableItem
import com.example.logifitappp.viewmodel.states.WearableKeyUpdateState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = WearableKeyUpdateViewModel.WearableKeyUpdateViewModelFactory::class)
class WearableKeyUpdateViewModel @AssistedInject constructor(
    @Assisted private val tenant: TenantModel?,
    private val wearableService: WearableService
): ViewModel() {
    @AssistedFactory
    interface WearableKeyUpdateViewModelFactory {
        fun create(tenant: TenantModel?): WearableKeyUpdateViewModel
    }

    val credentials = mutableStateListOf<FetchXiaomiCredentialSelectableItem>()

    val macs = mutableStateListOf<FetchXiaomiMacResponse>()
    val selectedMacs = mutableStateListOf<FetchXiaomiMacResponse>()

    val types = listOf(
        object: BottomSheetSelectableItem(0) { override fun toString() = App.context.getString(R.string.smart_band_5_6_and_7_label) },
        object: BottomSheetSelectableItem(1) { override fun toString() = App.context.getString(R.string.smart_band_8_label) },
        object: BottomSheetSelectableItem(2) { override fun toString() = App.context.getString(R.string.smart_band_9_label) }
    )

    var state by mutableStateOf(WearableKeyUpdateState())
        private set

    init {
        fetchNecessaryData()
    }

    fun addOrRemoveMac(mac: FetchXiaomiMacResponse) {
        if (selectedMacs.contains(mac)) selectedMacs.remove(mac)
        else selectedMacs.add(mac)
    }

    fun clearOrAddAllMacs() {
        if (selectedMacs.isEmpty()) selectedMacs.addAll(macs)
        else selectedMacs.clear()
    }

    fun fetchMacs() {
        viewModelScope.launch {
            try {
                macs.clear()

                state = state.copy(
                    isLoading = true,
                    status = AppStatusCodeEnum.FETCHING_XIAOMI_MACS
                )

                macs.addAll(wearableService.fetchXiaomiMacs(
                    state.credential?.id ?: 0,
                    state.credential?.password ?: "",
                    getType(state.type?.id),
                    state.credential?.username ?: "",
                ))

                state = if (macs.isEmpty()) state.copy(status = AppStatusCodeEnum.XIAOMI_MAC_LIST_EMPTY) else state.copy(isLoading = false)
            } catch (e: HttpConsumerException) {
                state = state.copy(status = e.getStatus())
            }
        }
    }

    fun fetchNecessaryData() {
        viewModelScope.launch {
            try {
                state = state.copy(isNecessaryDataFetching = true)
                credentials.addAll(wearableService.fetchXiaomiCredentials())
            } catch (e: Exception) {
                state = state.copy(hasNecessaryDataFetchingFailed = true)
            } finally {
                state = state.copy(isNecessaryDataFetching = false)
            }
        }
    }

    private fun getType(id: Int?) = when (id) {
        0 -> "OTROS"
        1 -> "BAND 8"
        2 -> "BAND 9"
        else -> null
    }

    fun stopProcessing() {
        state = state.copy(isLoading = false)
    }

    fun storeSelectedMacs() {
        viewModelScope.launch {
            try {
                macs.clear()

                state = state.copy(
                    isLoading = true,
                    status = AppStatusCodeEnum.STORING_XIAOMI_MACS
                )

                wearableService.storeXiaomiMacs(StoreXiaomiMacRequest(
                    devices = selectedMacs.map { XiaomiMac(it.mac, tenant?.id, it.token, it.type) }
                ))

                state = state.copy(status = AppStatusCodeEnum.SUCCESSFUL_XIAOMI_MACS_STORAGE)

                macs.clear()
                selectedMacs.clear()
            } catch (e: HttpConsumerException) {
                state = state.copy(status = e.getStatus())
            }
        }
    }

    fun updateCredential(credential: FetchXiaomiCredentialSelectableItem) {
        state = state.copy(credential = credential)
    }

    fun updateType(type: BottomSheetSelectableItem) {
        state = state.copy(type = type)
    }
}