package com.example.logifitappp.ui.screens.password_recovery

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.views.PasswordRecoveryViewModel

@Composable
fun PasswordRecoveryView(
    navigation: NavHostController
) {
    val passwordRecoveryViewModel: PasswordRecoveryViewModel = hiltViewModel()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState = passwordRecoveryViewModel.uiState

    SimplePage(
        content = {
            Text(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = stringResource(id = R.string.message_password_recovery),
                typography = MaterialTheme.typography.labelMedium
            )

            OutlinedTextField(
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                leadingIcon = Icons.Rounded.AlternateEmail,
                onValueChange = {
                    passwordRecoveryViewModel.updateUsername(it)
                },
                placeholder = stringResource(id = R.string.placeholder_user),
                value = passwordRecoveryViewModel.username,
                error = uiState.error
            )
            if (uiState.isSuccess) {
                Text(
                    text = uiState.successMessage ?: stringResource(id = R.string.password_recovery_success),
                    color = MaterialTheme.colorScheme.primary,
                    typography = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                icon = Icons.AutoMirrored.Rounded.Send,
                onClick = { passwordRecoveryViewModel.recoverPassword() },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.button_send)
            )
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterHorizontally)
                        .height(24.dp)
                        .padding(top = 16.dp)
                )
            }
        },

        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.password_recovery_title)
            )
        }
    )
}