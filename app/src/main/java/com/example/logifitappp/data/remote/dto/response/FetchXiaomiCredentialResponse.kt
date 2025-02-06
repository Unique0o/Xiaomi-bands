package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.ui.components.BottomSheetSelectableItem

data class FetchXiaomiCredentialResponse(
    val id: Int,
    val mote: String,
    val password: String,
    val username: String
)

data class FetchXiaomiCredentialSelectableItem(
    override val id: Int,
    val mote: String,
    val password: String,
    val username: String
): BottomSheetSelectableItem(id) {
    override fun toString() = "$username - $mote".trim()
}

fun FetchXiaomiCredentialResponse.toSelectableItem() = FetchXiaomiCredentialSelectableItem(id, mote, password, username)
