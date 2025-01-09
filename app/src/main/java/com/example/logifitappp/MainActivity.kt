package com.example.logifitappp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.navigation.MainNavigation
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.AppViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private val appViewModel: AppViewModel by viewModels()

    private val broadCasterReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                App.RELOAD_AUTHENTICATED_USER -> appViewModel.reloadAuthenticatedUser()

                App.REQUEST_ADDITIONAL_INFORMATION -> {
                    val shouldRequest = intent.getBooleanExtra(App.EXTRA_SHOULD_REQUEST_ADDITIONAL_INFORMATION, false)
                    appViewModel.updateShouldItOmitAdditionalInformation(!shouldRequest)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().setKeepOnScreenCondition {
            appViewModel.isLoading
        }

        setContent {
            LogifitApppTheme {
                StatusBarTheme()
                MainNavigation(appViewModel)
            }
        }

        retrieveFCMToken()
    }

    override fun onStart() {
        super.onStart()

        val filter = IntentFilter()
        filter.addAction(App.RELOAD_AUTHENTICATED_USER)
        filter.addAction(App.REQUEST_ADDITIONAL_INFORMATION)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCasterReceiver, filter)
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCasterReceiver)
        super.onStop()
    }

    private fun retrieveFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val token = it.result

                    App.preferences.getPreferences()
                        .edit()
                        .putString(AppPreferences.FIREBASE_NOTIFICATION_TOKEN, token)
                        .apply()
                }
            }
    }

    @Composable
    private fun StatusBarTheme() {
        val color = MaterialTheme.colorScheme.primary.toArgb()

        LaunchedEffect(Unit) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(color)
            )
        }
    }
}