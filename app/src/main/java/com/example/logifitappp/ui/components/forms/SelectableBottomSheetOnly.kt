package com.example.logifitappp.ui.components.forms

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.example.logifitappp.ui.components.BottomSheetSearchable
import com.example.logifitappp.ui.components.BottomSheetSelectableItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: BottomSheetSelectableItem> SelectableBottomSheetOnly(
    elements: List<T>,
    onChange: (T) -> Unit,
    title: String,
    value: T? = null
) {
    var isVisibleBottomSheetModal by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val updatedElements by rememberUpdatedState(elements)

    val toggleModalBottomSheet = {
        coroutineScope.launch {
            if (isVisibleBottomSheetModal) modalBottomSheetState.hide()
            else modalBottomSheetState.show()
        }.invokeOnCompletion {
            isVisibleBottomSheetModal = !isVisibleBottomSheetModal
        }
    }

    BottomSheetSearchable(
        coroutineScope = coroutineScope,
        elements = updatedElements,
        isVisible = isVisibleBottomSheetModal,
        modalBottomSheetState = modalBottomSheetState,
        onChange = onChange,
        onDismissRequest = { isVisibleBottomSheetModal = false },
        title = title,
        toggleModalBottomSheet = toggleModalBottomSheet,
        value = value
    )
}
