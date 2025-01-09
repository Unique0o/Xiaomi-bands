package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.BottomSheetSearchable
import com.example.logifitappp.ui.components.BottomSheetSelectableItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: BottomSheetSelectableItem> SelectableBottomSheet(
    modifier: Modifier = Modifier,
    elements: List<T>,
    error: String? = null,
    onChange: (T) -> Unit,
    placeholder: String,
    title: String,
    value: T? = null
) {
    var isVisibleBottomSheetModal by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
        elements = elements,
        isVisible = isVisibleBottomSheetModal,
        modalBottomSheetState = modalBottomSheetState,
        onChange = onChange,
        onDismissRequest = { isVisibleBottomSheetModal = false },
        title = title,
        toggleModalBottomSheet = toggleModalBottomSheet,
        value = value
    )

    Box(modifier = modifier.clickable { toggleModalBottomSheet() }) {
        OutlinedTextField(
            enabled = false,
            error = error,
            onValueChange = {},
            placeholder = placeholder,
            readOnly = true,
            trailingIcon = {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            },
            value = TextFieldValue(value?.toString() ?: "")
        )
    }
}
