package com.example.logifitappp.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.viewmodel.views.LoginViewModel

@Composable
fun LoginForm(
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    onSubmit: () -> Unit,
) {
    val (passwordFocusRequester) = FocusRequester.createRefs()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {
        OutlinedTextField(
            keyboardActions = KeyboardActions(
                onDone = {
                    passwordFocusRequester.requestFocus()
                }
            ),
            leadingIcon = Icons.Rounded.AlternateEmail,
            onValueChange = {
                loginViewModel.updateUsername(it)
            },
            placeholder = stringResource(id = R.string.placeholder_user),
            value = loginViewModel.username
        )

        OutlinedTextField(
            asPassword = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onSubmit()
                }
            ),
            leadingIcon = Icons.Rounded.Lock,
            modifier = Modifier
                .padding(top = 12.dp)
                .focusRequester(passwordFocusRequester),
            onValueChange = { loginViewModel.updatePassword(it) },
            placeholder = stringResource(id = R.string.placeholder_password),
            value = loginViewModel.password
        )

        Row(modifier = Modifier.padding(top = 8.dp)) {
            Spacer(modifier = Modifier.weight(1f))
            
            IconButton(
                icon = Icons.AutoMirrored.Rounded.Login,
                onClick = onSubmit,
                text = stringResource(id = R.string.button_get_into)
            )
        }
    }
}