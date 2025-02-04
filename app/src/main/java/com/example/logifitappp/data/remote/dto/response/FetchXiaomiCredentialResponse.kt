package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.ui.components.BottomSheetSelectableItem

data class FetchXiaomiCredentialResponse(
    override val id: Int,
    val mote: String,
    val password: String,
    val username: String
): BottomSheetSelectableItem(id) {
    override fun toString() = "$username - $mote".trim()
}
