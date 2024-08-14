package com.example.logifitappp.data.device.xiaomi

class XiaomiActivityFileId(val type: Type, val subtype: Subtype, val detailType: DetailType) {


    enum class Type { ACTIVITY, SPORTS }
    enum class Subtype { ACTIVITY_DAILY, ACTIVITY_SLEEP_STAGES, ACTIVITY_MANUAL_SAMPLES, ACTIVITY_SLEEP }
    enum class DetailType { DETAILS, SUMMARY }

}