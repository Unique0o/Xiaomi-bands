package com.example.logifitappp.ui.screens.synchronization_report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.logifitappp.data.remote.dto.response.ConditionResponse
import com.example.logifitappp.ui.components.Text

@Composable
fun SynchronizationReportConditionList(
    conditions: List<ConditionResponse>,
    currentTabIndex: Int,
    onSelectConditionIndex: (Int) -> Unit
) {
    val sizes = remember { mutableStateMapOf<Int, Pair<Int, Int>>() }

    ScrollableTabRow(
        containerColor = MaterialTheme.colorScheme.primary,
        divider = {},
        indicator = { tabPositions ->
            sizes[currentTabIndex]?.let {
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[currentTabIndex])
                        .size(it.first.dp, it.second.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                )
            }
        },
        selectedTabIndex = currentTabIndex
    ) {
        conditions.forEachIndexed { index, condition ->
            val isSelected = index == currentTabIndex

            Tab(
                modifier = Modifier.onSizeChanged { sizes[index] = Pair(it.width, it.height) }.zIndex(1f),
                onClick = { onSelectConditionIndex(index) },
                selected = isSelected,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = Color.White,
                text = {
                    Text(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                        text = condition.label,
                        typography = if (isSelected) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    }
}