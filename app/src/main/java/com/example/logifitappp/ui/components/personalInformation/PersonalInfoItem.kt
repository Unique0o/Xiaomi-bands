package com.example.logifitappp.ui.components.personalInformation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.theme.Rose120
import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.ui.components.forms.OutlinedTextField



@Composable
fun PersonalInfoItem(
    label: String,
    value: String,
    isValueSelected: Boolean = true,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            if (label in listOf("Names", "Surnames", "Identification Document","Email", "Phone")) {
                var textValue by remember { mutableStateOf(value) } // Mutable state for the text field

                OutlinedTextField(
                    keyboardActions = KeyboardActions(
                        onDone = {

                        }
                    ),
                    onValueChange = {
//                        loginViewModel.updateUsername(it)
                    },
                    placeholder = label,
                    value = TextFieldValue(value),
                    error = "",
                )

//            }else {
//                Text(
//                    text = value,
//                    textAlign = TextAlign.End,
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = if (isValueSelected) Color.Black else Rose120,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = Modifier
//                        .weight(1f)
//                        .clickable(onClick = onClick)
//                )
//
//            }
        }
//        HorizontalDivider(color = Color.LightGray)
    }
}