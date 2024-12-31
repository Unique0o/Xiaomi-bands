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

sealed class SleepProcessingStatusEnum(
    val chipStatus: ChipStatusEnum,
    val color: Color,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    class CUSTOM(chipStatus: ChipStatusEnum, color: Color, icon: ImageVector, @StringRes label: Int): SleepProcessingStatusEnum(chipStatus, color, icon, label)
    data object PENDING: SleepProcessingStatusEnum(ChipStatusEnum.NORMAL, Stone470, Icons.Default.SentimentNeutral, R.string.pending)
    data object SUITABLE: SleepProcessingStatusEnum(ChipStatusEnum.SUCCESS, Green298, Icons.Default.SentimentVerySatisfied, R.string.fit)
    data object UNSUITABLE: SleepProcessingStatusEnum(ChipStatusEnum.DANGER, Rose120, Icons.Default.SentimentVeryDissatisfied, R.string.unfit)
    data object WITH_OBSERVATIONS: SleepProcessingStatusEnum(ChipStatusEnum.WARNING, Orange390, Icons.Default.Sick, R.string.fit_with_observations)
}