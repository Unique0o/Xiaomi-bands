package com.example.logifitappp.ui.components.personalInformation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.ui.components.forms.ClickableOutlinedTextWithCustomPlaceholder
import com.example.logifitappp.ui.components.forms.OutlinedTextField


@Composable
fun PersonalInfoItem(
    label: String,
    value: String,
    isValueSelected: Boolean = true,
    onValueChange: (TextFieldValue) -> Unit,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (label in listOf("Names", "Surnames", "Identification Document", "Email", "Phone")) {
                var textValue by remember { mutableStateOf(value) } // Mutable state for the text field

                OutlinedTextField(
                    keyboardActions = KeyboardActions(
                        onDone = {

                        }
                    ),
                    onValueChange = {
                      onValueChange(TextFieldValue(it.toString()))
                    },
                    placeholder = label,
                    value = TextFieldValue(value),
                    error = "",
                )

            } else if (label == "Birthdate") {
                ClickableOutlinedTextWithCustomPlaceholder(value = value, onClick = onClick)
            }
        }
    }
}