package com.example.logifitappp.core.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection

fun PaddingValues.avoidBottom() = PaddingValues(
    end = this.calculateEndPadding(LayoutDirection.Ltr),
    start = this.calculateStartPadding(LayoutDirection.Ltr),
    top = this.calculateTopPadding()
)

fun PaddingValues.avoidTop() = PaddingValues(
    bottom = this.calculateBottomPadding(),
    end = this.calculateEndPadding(LayoutDirection.Ltr),
    start = this.calculateStartPadding(LayoutDirection.Ltr)
)

operator fun PaddingValues.plus(other: PaddingValues?): PaddingValues {
    if (other == null) return this

    return PaddingValues(
        bottom = this.calculateBottomPadding() + other.calculateBottomPadding(),
        end = this.calculateEndPadding(LayoutDirection.Ltr) + other.calculateEndPadding(
            LayoutDirection.Ltr),
        start = this.calculateStartPadding(LayoutDirection.Ltr) + other.calculateStartPadding(
            LayoutDirection.Ltr),
        top = this.calculateTopPadding() + other.calculateTopPadding()
    )
}