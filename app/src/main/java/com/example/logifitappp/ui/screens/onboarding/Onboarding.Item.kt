package com.example.logifitappp.ui.screens.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.MarkdownText

@Composable
fun OnboardingItem(
    @DrawableRes image: Int,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MarkdownText(
            boldTextTypography = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
            normalTextTypography = MaterialTheme.typography.labelLarge.copy(color = Color.White),
            text = title,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Image(
            contentDescription = null,
            modifier = Modifier.weight(1f),
            painter = painterResource(image)
        )

        Spacer(Modifier.height(24.dp))

        content()
    }
}