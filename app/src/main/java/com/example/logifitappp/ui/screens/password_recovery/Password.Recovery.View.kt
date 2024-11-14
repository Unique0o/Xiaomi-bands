package com.example.logifitappp.ui.screens.password_recovery

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.views.PasswordRecoveryViewModel

@Composable
fun PasswordRecoveryView(
    navigation: NavHostController
) {
    val passwordRecoveryViewModel: PasswordRecoveryViewModel = hiltViewModel()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    MessageModal(
        onClose = {
            if (passwordRecoveryViewModel.state.hasPasswordRecoveryBeenSuccessful) navigation.popBackStack()

            passwordRecoveryViewModel.stopProcessing()
        },
        onDismissRequest = { passwordRecoveryViewModel.stopProcessing() },
        status = passwordRecoveryViewModel.state.status,
        visible = passwordRecoveryViewModel.state.isPasswordRecovering
                || passwordRecoveryViewModel.state.hasPasswordRecoveryFailed
                || passwordRecoveryViewModel.state.hasPasswordRecoveryBeenSuccessful
    )

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.password_recovery_title)
            )
        }
    ) {
        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = stringResource(id = R.string.message_password_recovery),
            typography = MaterialTheme.typography.labelMedium
        )

        OutlinedTextField(
            error = passwordRecoveryViewModel.state.usernameError,
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
            value = passwordRecoveryViewModel.state.username
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            icon = Icons.AutoMirrored.Rounded.Send,
            onClick = { passwordRecoveryViewModel.recoverPassword() },
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.button_send)
        )
    }
}