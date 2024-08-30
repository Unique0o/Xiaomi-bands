package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.OutlinedTextField as MaterialOutlinedTextField
import androidx.compose.ui.unit.dp

@Composable
fun <T : SelectableItem> SelectableBottomSheetList(
    items: List<T>,
    value: T?,
    onSelectItem: (T) -> Unit,
    placeholder: String,
    title: String,
    modifier: Modifier = Modifier,
    errorLabel: String? = null
) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        MaterialOutlinedTextField(
            value = value?.name ?: "",
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isBottomSheetVisible = true },
            colors = TextFieldDefaults.colors().copy(
                cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorContainerColor = Color.Transparent,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
                errorSupportingTextColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedLabelColor = MaterialTheme.colorScheme.surfaceTint,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.surfaceTint
            ),
            label = {
                Text(
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
                    text = placeholder,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Open selector",
                    modifier = Modifier.clickable { isBottomSheetVisible = true }
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            isError = errorLabel != null,
            supportingText = errorLabel?.let {
                {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        )
    }

    if (isBottomSheetVisible) {
        BottomSheetSelector(
            items = items,
            title = title,
            onItemSelected = {
                onSelectItem(it)
                isBottomSheetVisible = false
            },
            onDismiss = { isBottomSheetVisible = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : SelectableItem> BottomSheetSelector(
    items: List<T>,
    title: String,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            items.forEach { item ->
                ListItem(
                    headlineContent = { Text(item.name) },
                    modifier = Modifier
                        .clickable { onItemSelected(item) }
                        .fillMaxWidth()
                )
            }
        }
    }
}


interface SelectableItem {
    val name: String
}