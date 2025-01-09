package com.example.logifitappp.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.logifitappp.BuildConfig
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.createImageFile
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun BottomSheetSelectableImageSource(
    modifier: Modifier = Modifier,
    onImageObtained: (Uri) -> Unit,
    trigger: @Composable () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isBottomSheetVisible by remember { mutableStateOf(false) }

    val file = context.createImageFile()
    val photoUri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
        if (isSaved) {
            onImageObtained(photoUri)
            isBottomSheetVisible = false
        }
    }

    val pickMediaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            onImageObtained(uri)
            isBottomSheetVisible = false
        }
    }

    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA) {
        if (it) cameraLauncher.launch(photoUri)
    }

    BottomSheet(
        coroutineScope = coroutineScope,
        isVisible = isBottomSheetVisible,
        modalBottomSheetState = bottomSheetState,
        onDismissRequest = { isBottomSheetVisible = false },
        title = stringResource(R.string.image_source_bottom_sheet_title)
    ) {
        IconText(
            modifier = Modifier
                .clickable {
                    if (ContextCompat.checkSelfPermission(
                            App.context.applicationContext,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        cameraPermission.launchPermissionRequest()
                    } else cameraLauncher.launch(photoUri)
                }
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            icon = Icons.Default.PhotoCamera,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 18.dp,
            label = stringResource(R.string.take_photo),
            labelTypography = MaterialTheme.typography.labelLarge,
            spaceBetween = 8.dp
        )

        IconText(
            modifier = Modifier
                .clickable { pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            icon = Icons.Default.ImageSearch,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 18.dp,
            label = stringResource(R.string.upload_image),
            labelTypography = MaterialTheme.typography.labelLarge,
            spaceBetween = 8.dp
        )
    }

    Box(modifier.clickable { isBottomSheetVisible = true }) {
        trigger()
    }
}