package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.ui.components.Text

open class AutocompleteDropdownItem (
    open val id: Int,
    open val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: AutocompleteDropdownItem> AutocompleteDropdown(
    modifier: Modifier = Modifier,
    onSuggestionSelected: (T) -> Unit,
    placeholder: String,
    suggestions: List<T>
) {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    var isDropdownVisible by remember { mutableStateOf(false) }

    val filteredSuggestions = remember(query) { suggestions.filter { it.label.contains(query.text, true) } }

    ExposedDropdownMenuBox(
        expanded = isDropdownVisible,
        modifier = modifier,
        onExpandedChange = { isDropdownVisible = it }
    ) {
        Box {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                onValueChange = {
                    query = it
                    isDropdownVisible = it.text.isNotEmpty() && filteredSuggestions.isNotEmpty()
                },
                placeholder = placeholder,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownVisible) },
                value = query
            )

            if (filteredSuggestions.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = isDropdownVisible,
                    onDismissRequest = { isDropdownVisible = false },
                ) {
                    filteredSuggestions.forEach {
                        DropdownMenuItem(
                            onClick = {
                                query = TextFieldValue(it.label)
                                isDropdownVisible = false
                                onSuggestionSelected(it)
                            },
                            text = {
                                Text(
                                    text = it.label,
                                    typography = MaterialTheme.typography.bodyMedium
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}