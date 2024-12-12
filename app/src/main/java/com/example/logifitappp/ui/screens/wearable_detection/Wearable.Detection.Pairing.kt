package com.example.logifitappp.ui.screens.wearable_detection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.lottie.AnimatedBluetoothConnecting
import com.example.logifitappp.ui.components.pages.SimplePage

@Composable
fun WearableDetectionPairing(
    candidate: WearableCandidate?
) {
    SimplePage {
        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.wearable_pairing_title, candidate?.getName() ?: "N/A"),
            typography = MaterialTheme.typography.headlineLarge
        )

        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = "${candidate?.getMacAddress()}",
            typography = MaterialTheme.typography.labelMedium
        )

        Spacer(Modifier.weight(1f))

        Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedBluetoothConnecting()
        }

        Spacer(Modifier.weight(1f))

        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = stringResource(R.string.wearable_pairing_message),
            typography = MaterialTheme.typography.labelMedium
        )
    }
}