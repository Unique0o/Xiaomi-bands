package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.BottomSheet
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.viewmodel.components.SelectableBottomSheetViewModel
import com.example.logifitappp.viewmodel.components.SelectableBottomSheetViewModelFactory
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

open class SelectableItem (
    open val id: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: SelectableItem> SelectableBottomSheet(
    modifier: Modifier = Modifier,
    elements: List<T>,
    error: String? = null,
    onChange: (T) -> Unit,
    placeholder: String,
    title: String,
    value: T? = null
) {
    val selectableBottomSheetViewModel: SelectableBottomSheetViewModel = viewModel(factory = SelectableBottomSheetViewModelFactory(
        flowOf(elements)
    ))

    val filteredElements = selectableBottomSheetViewModel.elements.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val toggleModalBottomSheet = {
        coroutineScope.launch {
            if (selectableBottomSheetViewModel.showBottomSheet) modalBottomSheetState.hide()
            else modalBottomSheetState.show()
        }.invokeOnCompletion {
            selectableBottomSheetViewModel.showBottomSheet =
                !selectableBottomSheetViewModel.showBottomSheet
        }
    }

    BottomSheet (
        coroutineScope = coroutineScope,
        isVisible = selectableBottomSheetViewModel.showBottomSheet,
        modalBottomSheetState = modalBottomSheetState,
        modifier = Modifier.fillMaxHeight(0.8f),
        onDismissRequest = {
            selectableBottomSheetViewModel.showBottomSheet = false
            selectableBottomSheetViewModel.searchText = TextFieldValue("")
        },
        title = title
    ) {
        OutlinedTextField(
            leadingIcon = Icons.Default.Search,
            onValueChange = {
                selectableBottomSheetViewModel.searchText = it
            },
            placeholder = stringResource(id = R.string.placeholder_search),
            value = selectableBottomSheetViewModel.searchText
        )

        LazyColumn {
            items(filteredElements.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            onChange(it as T)
                            toggleModalBottomSheet()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        contentDescription = null,
                        imageVector = Icons.Default.Circle,
                        modifier = Modifier.size(4.dp),
                        tint = if (it.id == value?.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                    )

                    Text(
                        color = if (it.id == value?.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, end = 16.dp),
                        overflow = TextOverflow.Ellipsis,
                        text = it.toString(),
                        typography = MaterialTheme.typography.headlineLarge
                    )

                    if (it.id == value?.id) {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Filled.Check,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 0.5.dp
                )
            }
        }
    }


    Box(
        modifier = modifier.clickable {
            toggleModalBottomSheet()
        },
    ) {
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
