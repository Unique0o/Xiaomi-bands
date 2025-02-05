package com.example.logifitappp.core.events

class WearableVersionInfoEvent: AbstractWearableEvent() {
    var firmwareVersion: String? = "N/A"
    var model: String? = "N/A"

    override fun toString() = "${super.toString()}fw: $firmwareVersion; hw: $model"
}