package com.example.logifitappp.core.specs

import com.example.logifitappp.enums.CallSpecTypeEnum

class CallSpec(val command: CallSpecTypeEnum) {
    var dndSuppressed = 0
    var name: String? = null
    var number: String? = null
    var sourceAppId: String? = null
    var sourceName: String? = null
}