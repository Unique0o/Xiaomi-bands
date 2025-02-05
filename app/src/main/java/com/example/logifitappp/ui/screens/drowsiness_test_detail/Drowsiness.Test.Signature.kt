package com.example.logifitappp.ui.screens.drowsiness_test_detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.logifitappp.ui.components.SignaturePad
import com.example.logifitappp.ui.components.pages.SimplePage

@Composable
fun DrowsinessTestSignature(
    onBack: () -> Unit,
    onSigned: (String) -> Unit
) {
    SimplePage {
        SignaturePad(Modifier.fillMaxSize(), onBack, onSigned)
    }
}