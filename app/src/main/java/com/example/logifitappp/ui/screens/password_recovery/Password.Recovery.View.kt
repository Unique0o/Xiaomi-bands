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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.PasswordRecoveryViewModel

@Composable
fun PasswordRecoveryView(
    navigation: NavHostController
) {
    val passwordRecoveryViewModel: PasswordRecoveryViewModel = viewModel()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

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
                value = passwordRecoveryViewModel.username
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                icon = Icons.AutoMirrored.Rounded.Send,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.button_send)
            )
        },

        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.password_recovery_title)
            )
        }
    )
}

@Preview
@Composable
fun PasswordRecoveryPreview() {
    LogifitApppTheme {
        PasswordRecoveryView(rememberNavController())
    }
}

@Preview
@Composable
fun LoginViewDarkPreview() {
    LogifitApppTheme(darkTheme = true) {
        PasswordRecoveryView(rememberNavController())
    }
}