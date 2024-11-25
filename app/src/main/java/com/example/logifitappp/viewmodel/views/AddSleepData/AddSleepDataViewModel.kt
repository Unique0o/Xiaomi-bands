package com.example.logifitappp.viewmodel.views.AddSleepData

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.SleepEntry
import com.example.logifitappp.data.remote.dto.requests.RealSleep
import com.example.logifitappp.data.remote.dto.requests.Sleep
import com.example.logifitappp.data.remote.dto.requests.SleepWrittenDataRequest
import com.example.logifitappp.domain.usecase.SaveSleepDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class AddSleepDataViewModel @Inject constructor(
    private val saveSleepDataUseCase: SaveSleepDataUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(AddSleepDataState())
    val state: StateFlow<AddSleepDataState> = _state

    var tempPhotoUri: Uri? = null
        private set

    fun createTempPhotoUri(context: Context): Uri? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val photoFile = File(
                this.context.cacheDir,
                "JPEG_${timeStamp}.jpg"
            )
            FileProvider.getUriForFile(
                this.context,
                "${this.context.packageName}.provider",
                photoFile
            ).also { uri ->
                tempPhotoUri = uri
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun onEvent(event: AddSleepDataEvent) {
        when (event) {
            is AddSleepDataEvent.SetFellAsleepTime -> updateSleepEntry { entry ->
                entry.copy(fellAsleepTime = event.time)
            }
            is AddSleepDataEvent.SetWokeUpTime -> updateSleepEntry { entry ->
                entry.copy(wokeUpTime = event.time)
            }
            is AddSleepDataEvent.SetDuration -> updateSleepEntry { entry ->
                entry.copy(duration = event.duration)
            }
            is AddSleepDataEvent.AttachMedia -> handleMediaResult(event.uri)
            AddSleepDataEvent.RemoveMedia -> removeMedia()
            AddSleepDataEvent.SaveSleepData -> saveSleepData()
        }
        validateState()
    }

    private fun uriToBase64(uri: Uri): String {
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val resizedBitmap = resizeBitmap(bitmap, 800)
            val outputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
            "data:image/jpeg;base64,$base64String"
        } catch (e: Exception) {
            Log.e("ImageConversion", "Error converting image: ${e.message}")
            throw e
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val ratio = maxSize.toFloat() / max(width, height)
        return if (ratio < 1) {
            Bitmap.createScaledBitmap(
                bitmap,
                (width * ratio).toInt(),
                (height * ratio).toInt(),
                true
            )
        } else bitmap
    }

    private fun updateSleepEntry(update: (SleepEntry) -> SleepEntry) {
        _state.update { currentState ->
            currentState.copy(sleepEntry = update(currentState.sleepEntry))
        }
    }

    private fun handleMediaResult(uri: Uri) {
        _state.update { currentState ->
            currentState.copy(
                photoUri = uri,
                errorMessage = null,
                isValid = validateEntry(currentState.sleepEntry, uri)
            )
        }
    }

    private fun removeMedia() {
        _state.update { currentState ->
            currentState.copy(
                photoUri = null,
                errorMessage = null,
                isValid = false
            )
        }
        tempPhotoUri = null
    }

    private fun validateState() {
        _state.update { currentState ->
            val entryValid = validateEntry(currentState.sleepEntry, currentState.photoUri)
            val errorMessage = getValidationError(currentState.sleepEntry, currentState.photoUri)
            currentState.copy(isValid = entryValid, errorMessage = errorMessage)
        }
    }

    private fun validateEntry(entry: SleepEntry, photoUri: Uri?): Boolean {
        return photoUri != null && entry.wokeUpTime.isAfter(entry.fellAsleepTime)
    }

    private fun getValidationError(entry: SleepEntry, photoUri: Uri?): String? {
        return when {
            entry.wokeUpTime <= entry.fellAsleepTime ->
                "La hora de despertar debe ser posterior a la hora de dormir"
            entry.duration.isNullOrEmpty() ->
                "Debe ingresar la duración del sueño"
            photoUri == null -> "Se requiere una foto"
            else -> null
        }
    }

    private fun saveSleepData() {
        val currentState = _state.value
        if (!currentState.isValid) return

        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                val user = withContext(Dispatchers.IO) {
                    App.database.userDao().getLoggedIn()
                } ?: throw Exception("Usuario no encontrado")

                val photoBase64 = withContext(Dispatchers.IO) {
                    currentState.photoUri?.let { uriToBase64(it) }
                } ?: return@launch

                val entry = currentState.sleepEntry
                val duration = entry.duration ?: throw Exception("Duración no ingresada")

                val durationPattern = "(\\d+)h\\s*(\\d+)m".toRegex()
                val matchResult = durationPattern.find(duration) ?: throw Exception("Formato de duración inválido")
                val (hours, minutes) = matchResult.destructured
                val durationValue = (hours.toInt() * 60 + minutes.toInt()).toString()

                val request = SleepWrittenDataRequest(
                    realSleep = RealSleep(
                        intervalText = duration,
                        intervalValue = durationValue,
                        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        version = "4.6"
                    ),
                    sleeps = listOf(
                        Sleep(
                            sleepIni = entry.fellAsleepTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            sleepEnd = entry.wokeUpTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            totalSleepText = duration,
                            totalSleepValue = durationValue
                        )
                    ),
                    userId = user.id,
                    shiftId = user.shiftId,
                    evidence = photoBase64
                )

                saveSleepDataUseCase(request).onSuccess {
                    _state.update { it.copy(
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    ) }
                }.onFailure { error ->
                    _state.update { it.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = "Error al guardar: ${error.message}"
                    ) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    isSuccess = false,
                    errorMessage = "Error al guardar: ${e.message}"
                ) }
            }
        }
    }
}