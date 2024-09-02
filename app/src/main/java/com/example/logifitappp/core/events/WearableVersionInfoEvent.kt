package com.example.logifitappp.core.events

class WearableVersionInfoEvent: AbstractWearableEvent() {
    var firmwareVersion = "N/A"
    var model = "N/A"

    override fun toString() = "${super.toString()}fw: $firmwareVersion; hw: $model"
}