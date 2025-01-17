package com.example.logifitappp.data.models.commons

open class WearableStressSampleModel(
    open var stress: Int,
    override var timestamp: Long,
    override var wearableId: Int
): WearableSampleModel(timestamp, wearableId)
