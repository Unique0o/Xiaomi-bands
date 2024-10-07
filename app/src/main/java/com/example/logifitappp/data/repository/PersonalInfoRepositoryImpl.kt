package com.example.logifitappp.data.repository

import android.content.Context
import com.example.logifitappp.R
import com.example.logifitappp.data.models.PersonalInfoItem
import com.example.logifitappp.domain.repository.PersonalInfoRepository
import javax.inject.Inject

class PersonalInfoRepositoryImpl @Inject constructor(
    private val context: Context
) : PersonalInfoRepository {
    override suspend fun getPersonalInfo(): List<PersonalInfoItem> {
        return listOf(
                PersonalInfoItem(context.getString(R.string.names), "John"),
                PersonalInfoItem(context.getString(R.string.surnames), "Doe"),
                PersonalInfoItem(context.getString(R.string.email), "john.doe@example.com"),
                PersonalInfoItem(context.getString(R.string.document_type), context.getString(R.string.not_selected), false),
                PersonalInfoItem(context.getString(R.string.country), context.getString(R.string.not_selected), false),
                PersonalInfoItem(context.getString(R.string.birthdate), context.getString(R.string.not_selected), false),
                PersonalInfoItem(context.getString(R.string.phone), "-"),
        )
    }
}