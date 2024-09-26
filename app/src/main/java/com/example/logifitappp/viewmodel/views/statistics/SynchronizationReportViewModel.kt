package com.example.logifitappp.viewmodel.views.statistics

import androidx.lifecycle.*
import com.example.logifitappp.R
import com.example.logifitappp.data.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SynchronizationReportViewModel @Inject constructor(): ViewModel() {
    private val _teamMembers = MutableLiveData<List<UserModel>>()
    val teamMembers: LiveData<List<UserModel>> get() = _teamMembers

    private val _filteredMembers = MutableLiveData<List<UserModel>>()
    val filteredMembers: LiveData<List<UserModel>> get() = _filteredMembers

    private val _selectedShift = MutableLiveData<Int?>()
    val selectedShift: LiveData<Int?> get() = _selectedShift

    private val _selectedGroup = MutableLiveData<Int?>()
    val selectedGroup: LiveData<Int?> get() = _selectedGroup

    private val _dateRange = MutableLiveData<ClosedRange<Date>?>()
    val dateRange: LiveData<ClosedRange<Date>?> get() = _dateRange

    val dropdownData = listOf(
        Pair(R.string.shift, listOf("Shift 1", "Shift 2", "Shift 3")),
        Pair(R.string.group, listOf("Group 1", "Group 2", "Group 3"))
    )

    init {
        setupBindings()
        loadMockData()
    }

    private fun setupBindings() {
        combine(
            _teamMembers.asFlow(),
            _selectedShift.asFlow(),
            _selectedGroup.asFlow()
        ) { members, shift, group ->
            applyFilters(members, shift, group)
        }.onEach { filteredList ->
            _filteredMembers.value = filteredList
        }.launchIn(viewModelScope)
    }

    private fun applyFilters(members: List<UserModel>, shift: Int?, group: Int?): List<UserModel> {
        return members.filter { member ->
            var include = true
            shift?.let { include = include && member.shiftId == it }
            group?.let { include = include && member.groupId == it }
            include
        }
    }

    fun loadMockData() {
        val mockUsers = listOf(
            UserModel(accessToken = "1", attentionValue = 2, firstName = "Mario", groupId = 1, hasLoggedIn = true, id = 1, isActive = true,
                lastName = "Perez Villafranca", profilePhoto = "profile_photo", role = 1, shiftId = 1, tenantId = 1
            ),
            UserModel(accessToken = "1", attentionValue = 1, firstName = "Hector", groupId = 1, hasLoggedIn = true, id = 1, isActive = true,
                lastName = "Hernandez", profilePhoto = "profile_photo", role = 1, shiftId = 1, tenantId = 1
            ),
            UserModel(accessToken = "1", attentionValue = 1, firstName = "Pamela", groupId = 1, hasLoggedIn = true, id = 1, isActive = true,
                lastName = "Hernandez", profilePhoto = "profile_photo", role = 1, shiftId = 2, tenantId = 1
            ),
            UserModel(accessToken = "1", attentionValue = 3, firstName = "Luis", groupId = 1, hasLoggedIn = true, id = 1, isActive = true,
                lastName = "Hernandez", profilePhoto = "profile_photo", role = 1, shiftId = 1, tenantId = 1
            )
        )
        _teamMembers.value = mockUsers
        _filteredMembers.value = applyFilters(mockUsers, _selectedShift.value, _selectedGroup.value)
    }

    fun setShift(shift: Int) {
        _selectedShift.value = shift
    }

    fun setGroup(group: Int) {
        _selectedGroup.value = group
    }

    fun setDateRange(range: ClosedRange<Date>) {
        _dateRange.value = range
    }

    val notFitCount: Int
        get() = _filteredMembers.value?.count { it.attentionValue == 1 } ?: 0

    val fitCount: Int
        get() = _filteredMembers.value?.count { it.attentionValue == 2 } ?: 0

    val sdCount: Int
        get() = _filteredMembers.value?.count { it.attentionValue == 3 } ?: 0

    val totalCount: Int
        get() = notFitCount + fitCount + sdCount
}
