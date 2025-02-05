package com.example.logifitappp.ui.components.modals

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableHelper
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.layouts.ModalLayout

@Composable
fun UnpairWearableModal(
    onClose: () -> Unit,
    onDismissRequest: () -> Unit,
    onUnpairedWearable: (Wearable) -> Unit,
    visible: Boolean,
    wearable: Wearable?
) {
    ModalLayout(
        onClose = onClose,
        onDismissRequest = onDismissRequest,
        visible = visible
    ) {
        Image(
            contentDescription = null,
            modifier = Modifier.height(120.dp),
            painter = painterResource(id = R.drawable.ic_unpair_smart_band)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(id = R.string.unpair_band_modal_title),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${wearable?.getName()} (${wearable?.getAddress()})",
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.titleMedium
        )

        Text(
            text = stringResource(id = R.string.unpair_band_modal_message),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                wearable?.let {
                    it.getWearableCoordinator().deleteWearable(it)
                    WearableHelper.getInstance().removeBond(it)
                    onUnpairedWearable(it)
                }

                onClose()
            },
            text = stringResource(id = R.string.button_ok)
        )
    }
}