package com.example.logifitappp.ui.screens.statistics


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.statistics.CardStatistics
import com.example.logifitappp.ui.components.statistics.Dropdown
import com.example.logifitappp.ui.components.statistics.MyTeamCard
import com.example.logifitappp.ui.components.statistics.RangeDateSelect
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120
import com.example.logifitappp.ui.theme.Stone240
import com.example.logifitappp.ui.theme.Stone470
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.theme.Blue690
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.logifitappp.ui.components.titles.IconPosition
import com.example.logifitappp.ui.components.titles.IconTitle
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.viewmodel.views.statistics.SynchronizationReportViewModel


@Composable
fun SynchronizationReportScreen(viewModel: SynchronizationReportViewModel = hiltViewModel()) {

    val filteredMembers by viewModel.filteredMembers.observeAsState(emptyList())
    val dropdownOptions = viewModel.dropdownData

    var showNoFitTeamCards by remember { mutableStateOf(true) }
    var showFitTeamCards by remember { mutableStateOf(false) }
    var showSDTeamCards by remember { mutableStateOf(false) }


    SimplePage(
        content = {
            Text(
                stringResource(id = R.string.my_statistics),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            CardStatistics(
                total = viewModel.totalCount.toString(),
                notFitTotal = viewModel.notFitCount.toString(),
                fitTotal = viewModel.fitCount.toString(),
                sdTotal = viewModel.sdCount.toString()
            )

            IconTitle(icon = Icons.Outlined.CheckCircle, text = stringResource(id = R.string.updated_information) + " " + "24/03/2023, 8.25AM",
                iconSize = 16, textTypography = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(15.dp))
            RangeDateSelect(
                label = "27/07/2023",
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                dropdownOptions.forEachIndexed { index, (title, options) ->
                    Dropdown(
                        modifier = Modifier.weight(1f),
                        title = stringResource(id = title),
                        options = options
                    )
                    if (index < dropdownOptions.size - 1) {
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .padding(3.dp),
                        onClick = {
                            showNoFitTeamCards = true
                            showFitTeamCards = false
                            showSDTeamCards = false
                        },
                        text = stringResource(id = R.string.status_no_apto),
                        containerColors = if (showNoFitTeamCards) Blue690 else Color.Transparent,
                        colorText = if (showNoFitTeamCards) MaterialTheme.colorScheme.onPrimary else Blue690
                    )
                }

                item {
                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .padding(3.dp),
                        onClick = {
                            showNoFitTeamCards = false
                            showFitTeamCards = true
                            showSDTeamCards = false
                        },
                        text = stringResource(id = R.string.status_person),
                        containerColors = if (showFitTeamCards) Blue690 else Color.Transparent,
                        colorText = if (showFitTeamCards) MaterialTheme.colorScheme.onPrimary else Blue690
                    )
                }
                item {
                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .padding(3.dp),
                        onClick = {
                            showNoFitTeamCards = false
                            showFitTeamCards = false
                            showSDTeamCards = true
                        },
                        text = "S/D",
                        containerColors = if (showSDTeamCards) Blue690 else Color.Transparent,
                        colorText = if (showSDTeamCards) MaterialTheme.colorScheme.onPrimary else Blue690
                    )
                }

            }
            IconTitle(icon = Icons.Default.Share,
                text = stringResource(id = R.string.my_team),
                iconPosition= IconPosition.TRAILING,
                iconColor = Blue690,
                )
            if (showNoFitTeamCards) {
                filteredMembers.forEach { member ->
                    if (member.attentionValue == 1) {
                        MyTeamCard(
                            icon = R.mipmap.ic_user_profile_foreground,
                            name = "${member.firstName} ${member.lastName}",
                            subtitleAlternateText = member.shiftDescription,
                            statusIndicator = stringResource(id = R.string.status_no_apto),
                            textIndicatorColor = Rose120,
                            backgroundIndicatorColor = Orange170,
                            pointIndicatorColor = Rose120
                        )
                    }
                }
            }
            if (showFitTeamCards) {
                filteredMembers.forEach { member ->
                    if (member.attentionValue == 2) {
                        MyTeamCard(
                            icon = R.mipmap.ic_user_profile_foreground,
                            name = "${member.firstName} ${member.lastName}",
                            subtitleAlternateText = member.shiftDescription,
                            statusIndicator = stringResource(id = R.string.status_person),
                            textIndicatorColor = Green298,
                            backgroundIndicatorColor = Lime70,
                            pointIndicatorColor = Green298
                        )
                    }
                }
            }
            if (showSDTeamCards) {
                filteredMembers.forEach { member ->
                    if (member.attentionValue == 3) {
                        MyTeamCard(
                            icon = R.mipmap.ic_user_profile_foreground,
                            name = "${member.firstName} ${member.lastName}",
                            subtitleAlternateText = member.shiftDescription,
                            statusIndicator = stringResource(id = R.string.without_data),
                            textIndicatorColor = Stone470,
                            backgroundIndicatorColor = Stone240,
                            pointIndicatorColor = Stone470
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SynchronizationReportScreenPreview() {
    LogifitApppTheme {
        SynchronizationReportScreen(SynchronizationReportViewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun SynchronizationReportScreenDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        SynchronizationReportScreen(SynchronizationReportViewModel())
    }
}