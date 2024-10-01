package com.example.logifitappp.ui.screens.personalInformation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.personalInformation.PersonalInfoItem
import com.example.logifitappp.ui.components.personalInformation.ProfilePhoto
import com.example.logifitappp.ui.theme.LogifitApppTheme

data class PersonalInfoItem(
    val label: String,
    val value: String,
    val isValueSelected: Boolean = true
)

@Composable
fun PersonalInfoScreen(
    onBackClick: () -> Unit,
    personalInfo: List<PersonalInfoItem>,
    onPhotoClick: () -> Unit,
    onItemClick: (String) -> Unit,
    navigation: NavHostController
) {
    SimplePage(
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item { ProfilePhoto(onPhotoClick = onPhotoClick) }
                items(personalInfo) { item ->
                    PersonalInfoItem(
                        label = item.label,
                        value = item.value,
                        isValueSelected = item.isValueSelected,
                        onClick = { onItemClick(item.label) }
                    )
                }
            }
        },
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.personal_info),
            )
        }
    )
}

@Composable
@Preview
fun PersonalInfoScreenPreview() {
    val previewPersonalInfo = listOf(
        PersonalInfoItem(stringResource(R.string.names), "John"),
        PersonalInfoItem(stringResource(R.string.surnames), "Doe"),
        PersonalInfoItem(stringResource(R.string.email), "john.doe@example.com"),
        PersonalInfoItem(stringResource(R.string.document_type), stringResource(R.string.not_selected), false),
        PersonalInfoItem(stringResource(R.string.country), stringResource(R.string.not_selected), false),
        PersonalInfoItem(stringResource(R.string.birthdate), stringResource(R.string.not_selected), false),
        PersonalInfoItem(stringResource(R.string.phone), "-")
    )

    LogifitApppTheme {
        PersonalInfoScreen(
            onBackClick = {},
            personalInfo = previewPersonalInfo,
            onPhotoClick = {},
            onItemClick = {},
            navigation = rememberNavController()
        )
    }
}