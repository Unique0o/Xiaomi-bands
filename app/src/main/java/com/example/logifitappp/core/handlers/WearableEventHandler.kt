package com.example.logifitappp.core.handlers

import com.example.logifitappp.core.CallSpec

interface WearableEventHandler {
    fun onFetchRecordedData(dataTypes: Int)

    fun onSetCallState(callSpec: CallSpec)
}