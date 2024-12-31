package com.example.logifitappp.ui.screens.training_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.ProgressiveImage
import com.example.logifitappp.ui.components.ReadMoreText
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Slate450
import com.example.logifitappp.viewmodel.views.TrainingDetailViewModel

@Composable
fun TrainingDetailHeader(
    trainingDetailViewModel: TrainingDetailViewModel
) {
    ProgressiveImage(
        modifier = Modifier.fillMaxWidth().height(140.dp),
        url = trainingDetailViewModel.state.training?.image
    )

    Spacer(Modifier.height(16.dp))

    Column(Modifier.padding(horizontal = 16.dp)) {
        IconText(
            icon = Icons.AutoMirrored.Filled.Segment,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 24.dp,
            label = stringResource(R.string.description),
            labelTypography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(6.dp))

        ReadMoreText(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = trainingDetailViewModel.state.training?.description ?: "",
            typography = MaterialTheme.typography.labelMedium
        )

        Spacer(Modifier.height(8.dp))

        if (trainingDetailViewModel.state.training?.progressPercentage == 100f) {
            IconButton(
                backgroundColor = Green298,
                icon = Icons.Default.Download,
                modifier = Modifier.fillMaxWidth(),
                onClick = { trainingDetailViewModel.downloadCertificate() },
                text = stringResource(R.string.button_download_certificate)
            )
        } else {
            Text(
                color = MaterialTheme.colorScheme.primary,
                text = "${stringResource(R.string.training_progress_label, trainingDetailViewModel.state.training?.progressPercentage?.toString() ?: "0")}%",
                typography = MaterialTheme.typography.labelSmall
            )

            Box(Modifier.fillMaxHeight()) {
                LinearProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    progress = { (trainingDetailViewModel.state.training?.progressPercentage ?: 0f) / 100 },
                    trackColor = Slate450
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        IconText(
            icon = Icons.AutoMirrored.Filled.MenuBook,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 24.dp,
            label = stringResource(R.string.lessons),
            labelTypography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(6.dp))
    }
}