package com.example.logifitappp.ui.screens.all_in_one

import android.icu.util.GregorianCalendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Orange390
import com.example.logifitappp.viewmodel.views.AllInOneViewModel

@Composable
fun AllInOneWearableItem(
    allInOneViewModel: AllInOneViewModel,
    navigation: NavHostController,
    wearable: Wearable
) {
    val drowsiness = allInOneViewModel.fetchDrowsiness(wearable)
    val sleepCondition = allInOneViewModel.fetchSleepCondition(drowsiness)
    val shift = allInOneViewModel.fetchShift(wearable)

    val isSleepSynchronizationRequired = drowsiness == null
    val isSynchronizationWithLogifitRequired = drowsiness?.sentAt == null

    InformationCard(
        icon = Icons.Default.AccountCircle,
        iconAction = { allInOneViewModel.goToPairingWorkerPage(wearable) },
        label = wearable.getAliasOrName(),
        modifier = Modifier.clickable { navigation.navigate(MainRoutes.WearableProfile(wearable.getAddress()!!)) },
        suffixComponent = {
            IconButton(onClick = { allInOneViewModel.openUnpairWearableModal(wearable) }) {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    ) {
        Column(Modifier.padding(start = 50.dp).offset(y = (-24).dp)) {
            wearable.getAddress()?.let {
                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = it,
                    typography = MaterialTheme.typography.labelMedium
                )

                Chip(
                    label = stringResource(if (wearable.isConnected()) R.string.connected else R.string.disconnected),
                    status = if (wearable.isConnected()) ChipStatusEnum.INFO else ChipStatusEnum.NORMAL
                )

                Spacer(Modifier.height(32.dp))
            }

            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    if (drowsiness == null) {
                        IconText(
                            icon = Icons.Default.Circle,
                            iconColor = MaterialTheme.colorScheme.error,
                            iconSize = 6.dp,
                            label = stringResource(R.string.lack_of_sleep_extraction_message),
                            labelColor = MaterialTheme.colorScheme.error,
                            labelTypography = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        val now = GregorianCalendar.getInstance().timeInMillis / 1000
                        val createAtInMillis = DateTimeUtils.parse(drowsiness.createdAt, "yyyy-MM-dd HH:mm:ss")!!.time / 1000

                        IconText(
                            icon = Icons.Default.Circle,
                            iconColor = Orange390,
                            iconSize = 6.dp,
                            label = stringResource(
                                R.string.sleep_extraction_label,
                                DurationUtils.formatExtended(App.context, now - createAtInMillis)
                            ),
                            labelColor = Orange390,
                            labelTypography = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (drowsiness?.sentAt != null) {
                        val now = GregorianCalendar.getInstance().timeInMillis / 1000
                        val sentAtInMillis = DateTimeUtils.parse(drowsiness.sentAt, "yyyy-MM-dd HH:mm:ss")!!.time / 1000

                        IconText(
                            icon = Icons.Default.Circle,
                            iconColor = Green298,
                            iconSize = 6.dp,
                            label = stringResource(
                                R.string.sleep_synchronization_label,
                                DurationUtils.formatExtended(App.context, now - sentAtInMillis)
                            ),
                            labelColor = Green298,
                            labelTypography = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        IconText(
                            icon = Icons.Default.Circle,
                            iconColor = MaterialTheme.colorScheme.error,
                            iconSize = 6.dp,
                            label = stringResource(R.string.lack_of_sleep_synchronization_with_logifit_message),
                            labelColor = MaterialTheme.colorScheme.error,
                            labelTypography = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    sleepCondition?.let {
                        Chip(
                            label = it.name,
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = it.calculateStatus().chipStatus
                        )

                        Spacer(Modifier.height(4.dp))
                    }

                    Chip(
                        modifier = Modifier.clickable { allInOneViewModel.openWearableShiftModal(wearable) },
                        label = shift?.name?.uppercase() ?: stringResource(id = R.string.select_shift).uppercase(),
                        status = if (shift == null) ChipStatusEnum.DANGER else ChipStatusEnum.INFO
                    )
                }

                Spacer(Modifier.width(4.dp))

                Box(Modifier.align(Alignment.Bottom), contentAlignment = Alignment.BottomEnd) {
                    IconButton(
                        onClick = { if (!wearable.isConnected() && !wearable.isInitialized()) allInOneViewModel.connect(wearable) else allInOneViewModel.fetchActivities(wearable) },
                        modifier = Modifier.background(
                            when {
                                !wearable.isConnected() && !wearable.isInitialized() -> MaterialTheme.colorScheme.primary
                                isSleepSynchronizationRequired -> MaterialTheme.colorScheme.primary
                                isSynchronizationWithLogifitRequired -> Orange390
                                else -> Green298
                            },
                            CircleShape
                        )
                    ) {
                        Icon(
                            contentDescription = null,
                            imageVector = if (!wearable.isConnected() && !wearable.isInitialized()) Icons.Filled.Bluetooth else Icons.Filled.Sync,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}