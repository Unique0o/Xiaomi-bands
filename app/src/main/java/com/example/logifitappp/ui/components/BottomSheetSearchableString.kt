package com.example.logifitappp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.BottomSheetSelectableItem
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DisposableHandle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : BottomSheetSelectableItem> BottomSheetSearchableString(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    elements: List<T>,
    isVisible: Boolean,
    modalBottomSheetState: SheetState,
    onChange: (T) -> Unit,
    onDismissRequest: () -> Unit,
    renderItem: @Composable ((T) -> Unit)? = null,
    title: String,
    toggleModalBottomSheet: () -> DisposableHandle,
    value: T? = null
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var filteredElements by remember { mutableStateOf(elements) }

    BottomSheet(
        coroutineScope = coroutineScope,
        isVisible = isVisible,
        modalBottomSheetState = modalBottomSheetState,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 600.dp),
        onDismissRequest = {
            onDismissRequest()

            searchText = TextFieldValue("")
            filteredElements = elements
        },
        title = title
    ) {
        LazyColumn {
            items(filteredElements) {
                if (renderItem != null) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .clickable {
                                onChange(it)
                                toggleModalBottomSheet()
                            }
                    ) {
                        renderItem(it)
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .clickable {
                                onChange(it)
                                toggleModalBottomSheet()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Default.Circle,
                            modifier = Modifier.size(4.dp),
                            tint = if (it.id == value?.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            color = if (it.id == value?.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp, end = 16.dp),
                            overflow = TextOverflow.Ellipsis,
                            text = it.label,
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
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f),
                    thickness = 0.5.dp
                )
            }
        }
    }
}