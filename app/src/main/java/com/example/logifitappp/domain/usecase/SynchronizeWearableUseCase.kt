package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.core.RecordedDataTypesEnum
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.SynchronizationProcessingException

class SynchronizeWearableUseCase {
    operator fun invoke(shift: ShiftModel?, wearable: Wearable) {
        if (shift == null) throw SynchronizationProcessingException(AppStatusCodeEnum.UNSELECTED_SHIFT)

        App.getWearableServiceTo(wearable).onFetchRecordedData(RecordedDataTypesEnum.TYPE_SYNC)
    }
}