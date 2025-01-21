package com.example.logifitappp.ui.screens.all_in_one

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.viewmodel.views.AllInOneViewModel
import java.util.GregorianCalendar

@Composable
fun AllInOneWearableItem(
    allInOneViewModel: AllInOneViewModel,
    navigation: NavHostController,
    wearable: Wearable
) {
    val drowsiness = allInOneViewModel.fetchDrowsiness(wearable)
    val sleepCondition = allInOneViewModel.fetchSleepCondition(drowsiness)
    val shift = allInOneViewModel.fetchShift(wearable)

    InformationCard(
        icon = Icons.Default.AccountCircle,
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
        Column(Modifier.padding(start = 28.dp).offset(y = (-12).dp)) {
            wearable.getAddress()?.let {
                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = it,
                    typography = MaterialTheme.typography.labelMedium
                )

                Spacer(Modifier.height(4.dp))
            }

            if (drowsiness == null) {
                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = stringResource(R.string.lack_of_sleep_synchronization_message),
                    typography = MaterialTheme.typography.labelMedium
                )
            } else {
                val now = GregorianCalendar.getInstance().timeInMillis / 1000
                val createAtInMillis = DateTimeUtils.parse(drowsiness.createdAt, "yyyy-MM-dd HH:mm:ss")!!.time / 1000

                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = stringResource(
                        R.string.sleep_synchronization_message,
                        drowsiness.createdAt,
                        DurationUtils.formatExtended(App.context, now - createAtInMillis)
                    ),
                    typography = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(Modifier.height(20.dp))

            Chip(
                label = stringResource(if (wearable.isConnected()) R.string.connected else R.string.disconnected),
                status = if (wearable.isConnected()) ChipStatusEnum.INFO else ChipStatusEnum.NORMAL
            )

            Spacer(Modifier.height(4.dp))

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
    }
}