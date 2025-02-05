package com.example.logifitappp.ui.screens.wearable_detection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.BottomSheet
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.viewmodel.views.WearableDetectionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WearableDetectionAuthenticationBottomSheet(
    authenticate: () -> Unit,
    wearableDetectionViewModel: WearableDetectionViewModel
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val doAuthentication = {
        coroutineScope.launch {
            authenticate()
            bottomSheetState.hide()
        }.invokeOnCompletion { wearableDetectionViewModel.closeBottomSheet() }
    }

    BottomSheet (
        coroutineScope = coroutineScope,
        isVisible = wearableDetectionViewModel.state.isBottomSheetVisible,
        modalBottomSheetState = bottomSheetState,
        onDismissRequest = { wearableDetectionViewModel.closeBottomSheet() },
        title = stringResource(id = R.string.authentication_key_bottom_sheet_title)
    ) {
        Text(
            text = stringResource(id = R.string.authentication_key_bottom_sheet_message),
            typography = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row (
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 14.dp, top = 8.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxHeight(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp).fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.onSurface,
                            text = "0x",
                            typography = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedTextField(
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        doAuthentication()
                    }
                ),
                onValueChange = { wearableDetectionViewModel.updateAuthenticationKey(it) },
                placeholder =  stringResource(id = R.string.enter_key),
                value = wearableDetectionViewModel.state.authenticationKey
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { doAuthentication() },
            text = stringResource(id = R.string.button_authenticate_band),
            modifier = Modifier.fillMaxWidth()
        )
    }
}