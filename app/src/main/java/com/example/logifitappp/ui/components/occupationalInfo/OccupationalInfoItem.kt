package com.example.logifitappp.ui.components.occupationalInfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.TimePickerDialogComponent
import com.example.logifitappp.ui.screens.occupational_information.OccupationItem
import com.example.logifitappp.ui.theme.Rose120

data class OccupationalInfoItemData(
    val label: String?,
    val value: String?,
    val suggestions: List<OccupationItem>? = null,
    val timerField: Boolean = false,
)
@Composable
fun OccupationalInfoItem(
    data: OccupationalInfoItemData,
    onClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    var selectedWorkLoad by remember { mutableStateOf(data.suggestions?.let { it[0] }) }
    var textFieldValue by remember { mutableStateOf(data.value) }
    var showTimerDialog by remember { mutableStateOf(false) }
    selectedWorkLoad?.apply { textFieldValue = label}
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        data.suggestions?.let {
                            expanded = true
                            return@clickable
                        }
                        if(data.timerField){
                            showTimerDialog = true
                            return@clickable
                        }
                        onClick()
                    }
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                data.label?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = selectedWorkLoad?.label ?: (textFieldValue ?: ""),
                    style = MaterialTheme.typography.bodyLarge,
                    color = when (data.value) {
                        "Not selected", "Not assigned" -> Rose120
                        else -> Color.Black
                    },
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Box(
                modifier = Modifier.wrapContentSize()
            ) {
                HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                selectedWorkLoad?.let {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        data.suggestions?.forEach { suggestion ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedWorkLoad = suggestion
                                    expanded = false
                                },
                                text = {
                                    com.example.logifitappp.ui.components.Text(
                                        text = suggestion.label ?: "",
                                        typography = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        if (data.timerField && showTimerDialog){
            TimePickerDialogComponent(
                onDismiss = { showTimerDialog = false },
                onConfirm = { formattedTime ->
                    textFieldValue = formattedTime
                    showTimerDialog = false
                }
            )
        }
    }
}