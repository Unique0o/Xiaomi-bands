package com.example.logifitappp.ui.screens.sleep_data_recording


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.addSleepData.PhotoSelectionCard
import com.example.logifitappp.ui.components.addSleepData.SleepEntryCard
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.views.AddSleepData.AddSleepDataEvent
import com.example.logifitappp.viewmodel.views.AddSleepData.AddSleepDataViewModel
import kotlinx.coroutines.delay

@Composable
fun SleepDataRecordingView(
    navigation: NavHostController,
) {
    val viewModel: AddSleepDataViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            showSuccess = true
            delay(1500)
            navigation.popBackStack()
        }
    }

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { showSuccess = false },
            title = { Text(stringResource(id = R.string.successful_wearable_information_transferring_message)) },
            confirmButton = {
                TextButton(onClick = { showSuccess = false }) {
                    Text("OK")
                }
            }
        )
    }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { viewModel.onEvent(AddSleepDataEvent.AttachMedia(it)) } }

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.register_your_sleep)
            )
        }
    ) {
        items(state.sleepEntries) { entry ->
            SleepEntryCard(
                entry = entry,
                onRemove = { viewModel.onEvent(AddSleepDataEvent.RemoveSleepEntry(entry.id)) },
                onFellAsleepTimeSelected = { time ->
                    viewModel.onEvent(AddSleepDataEvent.SetFellAsleepTime(entry.id, time))
                },
                onWokeUpTimeSelected = { time ->
                    viewModel.onEvent(AddSleepDataEvent.SetWokeUpTime(entry.id, time))
                }
            )
        }

        item {
            Button(
                onClick = { viewModel.onEvent(AddSleepDataEvent.AddSleepEntry) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.add_period))
            }

            Spacer(modifier = Modifier.height(24.dp))

            PhotoSelectionCard(
                photoUri = state.photoUri,
                onPickImage = { pickImage.launch("image/*") },
                onRemovePhoto = { viewModel.onEvent(AddSleepDataEvent.RemoveMedia) }
            )

            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.onEvent(AddSleepDataEvent.SaveSleepData) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, end = 16.dp, bottom = 35.dp, start = 16.dp),
                enabled = state.isValid && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(id = R.string.save))
                }
            }
        }
    }
}


