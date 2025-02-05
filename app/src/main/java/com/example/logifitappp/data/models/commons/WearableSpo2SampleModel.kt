package com.example.logifitappp.data.models.commons

open class WearableSpo2SampleModel(
    open var modeName: String,
    open var spo2: Int,
    override var timestamp: Long,
    override var wearableId: Int
): WearableSampleModel(timestamp, wearableId)
