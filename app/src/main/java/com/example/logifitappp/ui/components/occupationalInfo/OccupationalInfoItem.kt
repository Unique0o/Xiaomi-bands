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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.data.models.OccupationalInfoItemModel
import com.example.logifitappp.ui.components.BottomSheetSearchable
import com.example.logifitappp.ui.components.TimePickerDialogComponent
import com.example.logifitappp.ui.theme.Rose120
import com.example.logifitappp.viewmodel.views.OccupationItem
import kotlinx.coroutines.launch
@Composable
fun OccupationalInfoItem(
    data: OccupationalInfoItemModel,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
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
                    text = data.value,
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
            }
        }
    }
}