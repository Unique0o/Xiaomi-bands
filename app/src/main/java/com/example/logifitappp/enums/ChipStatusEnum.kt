package com.example.logifitappp.enums

import androidx.compose.ui.graphics.Color
import com.example.logifitappp.ui.theme.Amber60
import com.example.logifitappp.ui.theme.Blue130
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime30
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Orange390
import com.example.logifitappp.ui.theme.Rose120
import com.example.logifitappp.ui.theme.Zinc680

sealed class ChipStatusEnum(val color: Color, val backgroundColor: Color) {
    class CUSTOM(color: Color, backgroundColor: Color): ChipStatusEnum(color, backgroundColor)
    data object DANGER: ChipStatusEnum(Rose120, Orange170)
    data object INFO: ChipStatusEnum(Blue690, Blue130)
    data object NORMAL: ChipStatusEnum(Zinc680, Lime30)
    data object SUCCESS: ChipStatusEnum(Green298, Lime70)
    data object WARNING: ChipStatusEnum(Orange390, Amber60)
}