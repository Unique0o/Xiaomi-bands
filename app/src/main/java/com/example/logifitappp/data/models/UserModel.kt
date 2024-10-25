package com.example.logifitappp.data.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.R

@Entity(tableName = "users")
data class UserModel(
    @ColumnInfo(name = "access_token") val accessToken: String,
    @ColumnInfo(name = "attention_value") val attentionValue: Int? = null,
    @ColumnInfo(name = "band_type") val bandType: String = "MIBAND",
    @ColumnInfo(name = "birth_date") val birthDate: String? = null,
    @ColumnInfo(name = "blood_type") val bloodType: String? = null,
    @ColumnInfo(name = "break_after_seconds") val breakAfterSeconds: Int? = null,
    @ColumnInfo(name = "break_average_seconds") val breakAverageSeconds: Int? = null,
    @ColumnInfo(name = "break_frequency_seconds") val breakFrequencySeconds: Int? = null,
    @ColumnInfo(name = "commuting_seconds") val commutingSeconds: Int? = null,
    @ColumnInfo(name = "continuous_work_seconds") val continuousWorkSeconds: Int? = null,
    @ColumnInfo(name = "country_id") val countryId: Int? = null,
    @ColumnInfo(name = "department_id") val departmentId: Int? = null,
    @ColumnInfo(name = "document_type_id") val documentId: Int? = null,
    val email: String? = null,
    @ColumnInfo(name = "first_name") val firstName: String? = null,
    @ColumnInfo(name = "function_name") val functionName: String? = null,
    val gender: String? = null,
    @ColumnInfo(name = "group_id") val groupId: Int? = null,
    @ColumnInfo(name = "has_logged_in") val hasLoggedIn: Boolean = false,
    val height: Float? = null,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "identification_document") val identificationDocument: String? = null,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "last_name") val lastName: String? = null,
    val license: String? = null,
    @ColumnInfo(name = "location_id") val locationId: Int? = null,
    val phone: String? = null,
    @ColumnInfo(name = "profile_photo") val profilePhoto: String? = null,
    @ColumnInfo(name = "province_id") val provinceId: Int? = null,
    val role: Int,
    @ColumnInfo(name = "shift_id") val shiftId: Int? = null,
    @ColumnInfo(name = "tenant_id") val tenantId: Int,
    val weight: Float? = null,
    @ColumnInfo(name = "work_position") val workPosition: String? = null,
    @ColumnInfo(name = "workload_value") val workloadValue: Int? = null
) {
    fun isAdmin() = role == 1 || role == 3

    @Composable
    fun getRole() = when (role) {
        1 -> stringResource(R.string.administrator)
        2 -> stringResource(R.string.operator)
        3 -> stringResource(R.string.main_administrator)
        else -> stringResource(R.string.without_role)
    }

    val shiftDescription: String
        @Composable
        get() = when (shiftId) {
            1 -> stringResource(R.string.status)
            2 -> stringResource(R.string.night)
            else -> "without shift"
        }
}