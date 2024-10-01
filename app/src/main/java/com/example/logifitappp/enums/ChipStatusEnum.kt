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

enum class ChipStatusEnum(val color: Color, val backgroundColor: Color) {
    DANGER(Rose120, Orange170),
    INFO(Blue690, Blue130),
    NORMAL(Zinc680, Lime30),
    SUCCESS(Green298, Lime70),
    WARNING(Orange390, Amber60);
}