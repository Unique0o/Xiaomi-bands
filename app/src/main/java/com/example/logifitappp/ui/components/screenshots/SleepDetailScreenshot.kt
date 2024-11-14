package com.example.logifitappp.ui.components.screenshots

import android.icu.util.GregorianCalendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.DrowsinessModel
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.SleepConditionModel
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.SleepProcessingCard
import com.example.logifitappp.ui.components.modals.DrowsinessDetailScheme
import com.example.logifitappp.ui.components.modals.FatigueDetailScheme

@Composable
fun SleepDetailScreenshot(
    drowsiness: DrowsinessModel?,
    drowsinessCondition: SleepConditionModel?,
    fatigue: FatigueModel?,
    shift: ShiftModel?,
    tenant: TenantModel?,
    user: UserModel
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Image(
                contentDescription = null,
                modifier = Modifier.size(63.dp, 20.dp),
                painter = painterResource(id = R.drawable.ic_dark_logo)
            )
        }

        Spacer(Modifier.height(16.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.sleep_detail_screenshot_title),
                textAlign = TextAlign.Center,
                typography = MaterialTheme.typography.displayMedium
            )

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconText(
                    icon = Icons.Default.CalendarMonth,
                    label = stringResource(R.string.date),
                    modifier = Modifier.weight(1f),
                    labelColor = MaterialTheme.colorScheme.primary,
                    labelTypography = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1.5f),
                    text = DateTimeUtils.format(GregorianCalendar.getInstance().time, "dd.MM.yyyy"),
                    typography = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconText(
                    icon = Icons.Default.WatchLater,
                    label = stringResource(R.string.last_extraction_data),
                    modifier = Modifier.weight(1f),
                    labelColor = MaterialTheme.colorScheme.primary,
                    labelTypography = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1.5f),
                    text = drowsiness?.let {
                        DateTimeUtils.parse(it.createdAt, "yyyy-MM-dd HH:mm:ss", "dd.MM.yyyy h:mm a")
                    } ?: "-",
                    typography = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconText(
                    icon = Icons.Default.WatchLater,
                    label = stringResource(R.string.last_sending_to_logifit),
                    modifier = Modifier.weight(1f),
                    labelColor = MaterialTheme.colorScheme.primary,
                    labelTypography = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1.5f),
                    text = drowsiness?.let {
                        if (it.sentAt == null) return@let "-"

                        DateTimeUtils.parse(it.sentAt, "yyyy-MM-dd HH:mm:ss", "dd.MM.yyyy h:mm a")
                    } ?: "-",
                    typography = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconText(
                    icon = Icons.Default.Person,
                    label = stringResource(R.string.full_name),
                    modifier = Modifier.weight(1f),
                    labelColor = MaterialTheme.colorScheme.primary,
                    labelTypography = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1.5f),
                    text = user.getFullName(),
                    typography = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconText(
                    icon = Icons.Default.SyncAlt,
                    label = stringResource(R.string.shift),
                    modifier = Modifier.weight(1f),
                    labelColor = MaterialTheme.colorScheme.primary,
                    labelTypography = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1.5f),
                    text = shift?.name ?: "-",
                    typography = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.sleep_detail_screenshot_result_title),
                typography = MaterialTheme.typography.displayMedium
            )

            SleepProcessingCard(
                drowsiness = drowsiness,
                drowsinessCondition = drowsinessCondition,
                fatigue = fatigue,
                tenant = tenant
            )

            Spacer(Modifier.height(16.dp))

            Text(
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.sleep_detail_screenshot_drowsiness_result_title),
                typography = MaterialTheme.typography.displayMedium
            )

            Spacer(Modifier.height(12.dp))

            DrowsinessDetailScheme(
                drowsiness = drowsiness,
                drowsinessCondition = drowsinessCondition,
                tenant = tenant
            )

            Spacer(Modifier.height(24.dp))

            Text(
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.sleep_detail_screenshot_fatigue_result_title),
                typography = MaterialTheme.typography.displayMedium
            )

            Spacer(Modifier.height(12.dp))
            FatigueDetailScheme(fatigue = fatigue)
        }

        Spacer(Modifier.height(32.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = stringResource(R.string.sleep_detail_screenshot_copyright_message),
                typography = MaterialTheme.typography.bodySmall
            )
        }
    }
}