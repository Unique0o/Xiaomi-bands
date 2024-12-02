package com.example.logifitappp.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Link
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.LoginViewModel

@Composable
fun LoginView(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    val loginViewModel: LoginViewModel = hiltViewModel()

    MessageModal(
        onClose = { loginViewModel.stopProcessing() },
        onDismissRequest = { loginViewModel.stopProcessing() },
        status = loginViewModel.state.status,
        visible = loginViewModel.state.isLoggedIn || loginViewModel.state.hasLoginProcessFailed
    )

    SimplePage(backgroundColor = MaterialTheme.colorScheme.surface) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(
                id = if (isSystemInDarkTheme()) R.drawable.ic_dark_logo else R.drawable.ic_light_logo
            ),
            contentDescription = null
        )

        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = stringResource(id = R.string.subtitle_login),
            typography = MaterialTheme.typography.labelMedium
        )

        LoginForm(
            loginViewModel = loginViewModel,
            modifier = Modifier.padding(top = 24.dp),
            onSubmit = {
                loginViewModel.login {
                    appViewModel.updateUser(it)
                    navigation.navigate(MainRoutes.BottomTabsNavigation) {
                        popUpTo(0)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Row {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.message_recover_password),
                typography = MaterialTheme.typography.bodySmall,
            )

            Link(
                text = stringResource(id = R.string.link_recover_password)
            ) { navigation.navigate(MainRoutes.PasswordRecovery) }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}