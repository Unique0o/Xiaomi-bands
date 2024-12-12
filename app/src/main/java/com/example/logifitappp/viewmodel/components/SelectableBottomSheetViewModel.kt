package com.example.logifitappp.viewmodel.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.ui.components.forms.SelectableItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SelectableBottomSheetViewModelFactory(private val items: Flow<List<SelectableItem>>): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass: Class<T>): T = SelectableBottomSheetViewModel(items) as T
}

class SelectableBottomSheetViewModel(
    items: Flow<List<SelectableItem>>
): ViewModel() {
    var searchText by mutableStateOf(TextFieldValue(""))
    var showBottomSheet by mutableStateOf(false)

    val elements: StateFlow<List<SelectableItem>> = snapshotFlow { searchText }
        .combine(items) { query, selectables ->
            when {
                query.text.isNotEmpty() -> selectables.filter { item ->
                    item.toString().contains(query.text, ignoreCase = true)
                }
                else -> selectables
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(5_000)
        )
}