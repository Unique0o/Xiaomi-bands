package com.example.logifitappp.ui.components.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R

    @Composable
    fun OnboardingFragment(
        page: Int,
        isAdmin: Boolean,
        onActivateBluetooth: () -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            OnboardingText(page, isAdmin)
            OnboardingImage(page, isAdmin)
            OnboardingBottomComponent(page, isAdmin, onActivateBluetooth)
        }
    }

    @Composable
    private fun OnboardingText(page: Int, isAdmin: Boolean) {
        Text(
            text = stringResource(getTopMessageResourceId(page, isAdmin)),
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }

    @Composable
    private fun OnboardingImage(page: Int, isAdmin: Boolean) {
        Image(
            painter = painterResource(getImageResourceId(page, isAdmin)),
            contentDescription = null,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
    }

    @Composable
    private fun OnboardingBottomComponent(
        page: Int,
        isAdmin: Boolean,
        onActivateBluetooth: () -> Unit
    ) {
        when (page) {
            0, 1 -> Text(
                text = stringResource(getBottomMessageResourceId(page, isAdmin)),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp)
            )
            2 -> Button(
                onClick = onActivateBluetooth,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(stringResource(R.string.activate_bluetooth), color = Color.Blue)
            }
        }
    }

    private fun getTopMessageResourceId(page: Int, isAdmin: Boolean): Int = when (page) {
        0 -> if (isAdmin) R.string.first_administrator_onboarding_top_message else R.string.first_operator_onboarding_top_message
        1 -> if (isAdmin) R.string.second_administrator_onboarding_top_message else R.string.second_operator_onboarding_top_message
        else -> if (isAdmin) R.string.third_administrator_onboarding_top_message else R.string.third_operator_onboarding_top_message
    }

    private fun getBottomMessageResourceId(page: Int, isAdmin: Boolean): Int = when (page) {
        0 -> if (isAdmin) R.string.first_administrator_onboarding_bottom_message else R.string.first_operator_onboarding_bottom_message
        1 -> if (isAdmin) R.string.second_administrator_onboarding_bottom_message else R.string.second_operator_onboarding_bottom_message
        else -> R.string.activate_bluetooth
    }

    private fun getImageResourceId(page: Int, isAdmin: Boolean): Int = when (page) {
        0 -> if (isAdmin) R.drawable.ic_onboarding_1 else R.drawable.ic_onboarding_1
        1 -> R.drawable.ic_onboarding_2
        else -> R.drawable.ic_onboarding_3
    }