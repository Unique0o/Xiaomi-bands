package com.example.logifitappp.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.LoginViewModel

@Composable
fun LoginView(
    navigation: NavHostController
) {
    val loginViewModel: LoginViewModel = viewModel()

    SimplePage(
        content = {
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
                onSubmit = { navigation.navigate(MainRoutes.WearableDetection) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(id = R.string.message_recover_password),
                    typography = MaterialTheme.typography.bodySmall,
                )

                Text(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            navigation.navigate(MainRoutes.PasswordRecovery)
                        },
                    typography = MaterialTheme.typography.bodySmall,
                    text = stringResource(id = R.string.link_recover_password)
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    )
}

@Preview
@Composable
fun LoginViewPreview() {
    LogifitApppTheme {
        LoginView(rememberNavController())
    }
}

@Preview
@Composable
fun LoginViewDarkPreview() {
    LogifitApppTheme(darkTheme = true) {
        LoginView(rememberNavController())
    }
}