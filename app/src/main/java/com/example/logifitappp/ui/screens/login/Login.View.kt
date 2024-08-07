package com.example.logifitappp.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.LoginViewModel

@Composable
fun LoginView(
    navigation: NavHostController
) {
    val loginViewModel: LoginViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(32.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        LoginHeader(modifier = Modifier.padding(bottom = 24.dp))

        LoginForm(
            loginViewModel = loginViewModel,
            onSubmit = {}
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
                modifier = Modifier.padding(start = 4.dp).clickable {
                    navigation.navigate(MainRoutes.PasswordRecovery)
                },
                typography = MaterialTheme.typography.bodySmall,
                text = stringResource(id = R.string.link_recover_password)
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
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