package com.example.logifitappp.ui.screens.additional_information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.BottomSheetSelectableImageSource
import com.example.logifitappp.ui.components.ProgressiveImage
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.views.AdditionalInformationViewModel

@Composable
fun AdditionalInformationProfilePhoto(
    additionalInformationViewModel: AdditionalInformationViewModel
) {
    SimplePage {
        Text(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.second_additional_information_title),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayLarge
        )

        Spacer(Modifier.height(2.dp))

        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.first_additional_information_message),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.labelMedium
        )

        Spacer(Modifier.weight(1f))

        BottomSheetSelectableImageSource(
            Modifier.align(Alignment.CenterHorizontally),
            onImageObtained = { additionalInformationViewModel.updateProfilePhoto(it) }
        ) {
            if (additionalInformationViewModel.state.photo == null) {
                Box(
                    Modifier
                        .size(300.dp)
                        .background(MaterialTheme.colorScheme.onTertiaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        contentDescription = "take photo",
                        imageVector = Icons.Default.PhotoCamera,
                        modifier = Modifier.size(120.dp),
                        tint = Color.White
                    )
                }
            } else {
                ProgressiveImage(
                    default = R.drawable.ic_default_profile_photo,
                    modifier = Modifier.size(300.dp).clip(CircleShape),
                    url = additionalInformationViewModel.state.photo
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { additionalInformationViewModel.storeProfilePhoto() },
            text = stringResource(R.string.button_continue)
        )
    }
}