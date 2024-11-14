package com.example.logifitappp.enums

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Sick
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Orange390
import com.example.logifitappp.ui.theme.Rose120
import com.example.logifitappp.ui.theme.Stone470

enum class SleepProcessingStatusEnum(
    val chipStatus: ChipStatusEnum,
    val color: Color,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    PENDING(ChipStatusEnum.NORMAL, Stone470, Icons.Default.SentimentNeutral, R.string.pending),
    SUITABLE(ChipStatusEnum.SUCCESS, Green298, Icons.Default.SentimentVerySatisfied, R.string.fit),
    UNSUITABLE(ChipStatusEnum.DANGER, Rose120, Icons.Default.SentimentVeryDissatisfied, R.string.unfit),
    WITH_OBSERVATIONS(ChipStatusEnum.WARNING, Orange390, Icons.Default.Sick, R.string.fit_with_observations);
}