package com.example.logifitappp.ui.screens.additionalInformation

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.AdditionalInformation.AdditionalInformationPictureViewModel
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditionalInformationPicture(
    navigation: NavHostController,
    viewModel: AdditionalInformationPictureViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updatePhoto(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempCameraUri?.let { uri ->
                viewModel.updatePhoto(uri)
            }
        }
    }

    fun createTempImageUri(): Uri {
        val tempFile = File.createTempFile(
            "temp_photo_",
            ".jpg",
            context.cacheDir
        ).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            tempFile
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(Modifier.padding(16.dp)) {
                ListItem(
                    headlineContent = { Text("Tomar foto") },
                    leadingContent = {
                        Icon(Icons.Default.Camera, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        tempCameraUri = createTempImageUri()
                        cameraLauncher.launch(tempCameraUri!!)
                        showBottomSheet = false
                    }
                )
                ListItem(
                    headlineContent = { Text("Elegir de galería") },
                    leadingContent = {
                        Icon(Icons.Default.Image, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        galleryLauncher.launch("image/*")
                        showBottomSheet = false
                    }
                )
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    SimplePage(
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = stringResource(id = R.string.second_additional_information_title),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.second_additional_information_message),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(210.dp)
                            .clip(CircleShape)
                            .background(
                                if (state.photoUri == null)
                                    MaterialTheme.colorScheme.surfaceTint
                                else Color.Transparent
                            )
                            .clickable { showBottomSheet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.photoUri != null) {
                            AsyncImage(
                                model = state.photoUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Cámara",
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(120.dp)
                            )
                        }
                    }

                    IconButton(
                        icon = Icons.AutoMirrored.Rounded.Send,
                        onClick = {
                            viewModel.uploadPhoto {
                                navigation.navigate(MainRoutes.Home)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.button_continue),
                        enabled = state.photoUri != null && !state.isLoading
                    )
                }

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun AdditionalInformationPicturePreview() {
    LogifitApppTheme {
        AdditionalInformationPicture(rememberNavController())
    }

}

@Composable
@Preview
fun AdditionalInformationPictureDarkPreview() {
    LogifitApppTheme(darkTheme = true) {
        AdditionalInformationPicture(rememberNavController())
    }

}
