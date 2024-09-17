package com.example.logifitappp.data.models.commons

import com.example.logifitappp.core.wearebles.WearableActivityProvider

open class WearableRawActivityModel(
    open var heartRate: Int,
    open var intensity: Int,
    open var steps: Int,
    open var timestamp: Long,
    open var type: Int,
    open var wearableId: Int,

    var provider: WearableActivityProvider<out WearableRawActivityModel>? = null
)
