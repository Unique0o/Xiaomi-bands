package com.example.logifitappp.ui.screens.terms_and_conditions

import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Loader
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.views.termsConditions.TermsAndConditionsViewModel

@Composable
fun TermsAndConditionsView(
    navigation: NavHostController,
) {
    val viewModel: TermsAndConditionsViewModel = hiltViewModel()
    val url by viewModel.url.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(initial = true)
    val backgroundColor = MaterialTheme.colorScheme.surface.toArgb()

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.terms_and_conditions)
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center)
        {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        viewModel.getWebViewSettings(this, backgroundColor)
                        loadUrl(url ?: "")
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (isLoading) {
                Loader()
            }
        }
    }
}
