package com.example.logifitappp.ui.screens.AddSleepData


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.addSleepData.PhotoSelectionCard
import com.example.logifitappp.ui.components.addSleepData.SleepEntryCard
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.viewmodel.views.AddSleepData.AddSleepDataEvent
import com.example.logifitappp.viewmodel.views.AddSleepData.AddSleepDataViewModel
import kotlinx.coroutines.delay

@Composable
fun AddSleepDataView(
    navigation: NavHostController,
) {
    val viewModel: AddSleepDataViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val scrollState = rememberLazyListState()
    var showSuccess by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
            title = { Text(stringResource(id = R.string.success)) },
            text = { Text(stringResource(id = R.string.successful_wearable_information_transferring_message)) },
            confirmButton = {
                TextButton(onClick = { showSuccess = false }) {
                    Text("OK")
                }
            }
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.tempPhotoUri?.let { uri ->
                viewModel.onEvent(AddSleepDataEvent.AttachMedia(uri))
            }
        }
    }

    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.register_your_sleep)
            )
        }

        items(state.sleepEntries.size) { index ->
            SleepEntryCard(
                entry = state.sleepEntries[index],
                index = index,
                onFellAsleepTimeSelected = { time ->
                    viewModel.onEvent(AddSleepDataEvent.SetFellAsleepTime(index, time))
                },
                onWokeUpTimeSelected = { time ->
                    viewModel.onEvent(AddSleepDataEvent.SetWokeUpTime(index, time))
                },
                onDurationSelected = { duration ->
                    viewModel.onEvent(AddSleepDataEvent.SetDuration(index, duration))
                },
                onRemove = if (index > 0) {
                    { viewModel.onEvent(AddSleepDataEvent.RemoveSleepEntry(index)) }
                } else null
            )
        }
        item {

            OutlinedButton(
                onClick = { viewModel.onEvent(AddSleepDataEvent.AddSleepEntry) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.add_period))
            }

            Spacer(modifier = Modifier.height(16.dp))

            PhotoSelectionCard(
                photoUri = state.photoUri,
                onTakePhoto = {
                    viewModel.createTempPhotoUri(context)?.let { uri ->
                        cameraLauncher.launch(uri)
                    }
                },
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