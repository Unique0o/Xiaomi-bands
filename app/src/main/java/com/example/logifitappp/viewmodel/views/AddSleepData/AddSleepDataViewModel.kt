package com.example.logifitappp.viewmodel.views.AddSleepData

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.DurationUtils
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

    private val _state = MutableStateFlow(AddSleepDataState(sleepEntries = listOf(SleepEntry())))
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
            is AddSleepDataEvent.AddSleepEntry -> {
                _state.update { currentState ->
                    currentState.copy(
                        sleepEntries = currentState.sleepEntries + SleepEntry()
                    )
                }
            }
            is AddSleepDataEvent.RemoveSleepEntry -> {
                _state.update { currentState ->
                    if (currentState.sleepEntries.size > 1) {
                        currentState.copy(
                            sleepEntries = currentState.sleepEntries.filterIndexed { index, _ ->
                                index != event.index
                            }
                        )
                    } else currentState
                }
            }
            is AddSleepDataEvent.SetFellAsleepTime -> updateSleepEntry(event.index) { entry ->
                entry.copy(fellAsleepTime = event.time)
            }
            is AddSleepDataEvent.SetWokeUpTime -> updateSleepEntry(event.index) { entry ->
                entry.copy(wokeUpTime = event.time)
            }
            is AddSleepDataEvent.SetDuration -> updateSleepEntry(event.index) { entry ->
                entry.copy(duration = event.duration)
            }
            is AddSleepDataEvent.AttachMedia -> handleMediaResult(event.uri)
            AddSleepDataEvent.RemoveMedia -> removeMedia()
            AddSleepDataEvent.SaveSleepData -> saveSleepData()
        }
        validateState()
    }

    private fun updateSleepEntry(index: Int, update: (SleepEntry) -> SleepEntry) {
        _state.update { currentState ->
            val updatedEntries = currentState.sleepEntries.toMutableList()
            updatedEntries[index] = update(updatedEntries[index])
            currentState.copy(sleepEntries = updatedEntries)
        }
    }

    private fun handleMediaResult(uri: Uri) {
        _state.update { currentState ->
            currentState.copy(
                photoUri = uri,
                errorMessage = null,
                isValid = validateEntries(currentState.sleepEntries, uri)
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
            val entriesValid = validateEntries(currentState.sleepEntries, currentState.photoUri)
            val errorMessage = getValidationError(currentState.sleepEntries, currentState.photoUri)
            currentState.copy(isValid = entriesValid, errorMessage = errorMessage)
        }
    }

    private fun validateEntries(entries: List<SleepEntry>, photoUri: Uri?): Boolean {
        return photoUri != null && entries.all { entry ->
            entry.wokeUpTime.isAfter(entry.fellAsleepTime) &&
                    isDurationValid(entry)
        }
    }

    private fun getValidationError(entries: List<SleepEntry>, photoUri: Uri?): String? {
        return when {
            entries.any { it.wokeUpTime <= it.fellAsleepTime } ->
                "La hora de despertar debe ser posterior a la hora de dormir"
            entries.any { it.duration.isNullOrEmpty() } ->
                "Debe ingresar la duración del sueño para todas las entradas"
            entries.any { !isDurationValid(it) } ->
                "La duración no puede ser mayor al tiempo entre dormir y despertar"
            photoUri == null -> "Se requiere una foto"
            else -> null
        }
    }
    private fun isDurationValid(entry: SleepEntry): Boolean {
        val duration = entry.duration ?: return false
        val durationPattern = "(\\d+)h\\s*(\\d+)m".toRegex()
        val matchResult = durationPattern.find(duration) ?: return false

        val (hours, minutes) = matchResult.destructured
        val durationMinutes = hours.toInt() * 60 + minutes.toInt()

        val actualDurationMinutes = ChronoUnit.MINUTES.between(
            entry.fellAsleepTime,
            entry.wokeUpTime
        )

        return durationMinutes <= actualDurationMinutes
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


                val sleepsList = currentState.sleepEntries.map { entry ->
                    val duration = entry.duration ?: throw Exception("Duración no ingresada")
                    val durationPattern = "(\\d+)h\\s*(\\d+)m".toRegex()
                    val matchResult = durationPattern.find(duration)
                        ?: throw Exception("Formato de duración inválido")
                    val (hours, minutes) = matchResult.destructured
                    val durationValue = hours.toInt() * 60 * 60 + minutes.toInt() * 60L

                    Sleep(
                        sleepIni = entry.fellAsleepTime.format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        ),
                        sleepEnd = entry.wokeUpTime.format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        ),
                        totalSleepText = DurationUtils.format(durationValue),
                        totalSleepValue = durationValue.toString()
                    )
                }

                val totalDurationMinutes = sleepsList.sumOf {
                    it.totalSleepValue.toInt()
                }
                val totalHours = totalDurationMinutes / 60
                val totalMinutes = totalDurationMinutes % 60
                val totalSeconds = totalHours * 60 * 60 + totalMinutes * 60L

                val request = SleepWrittenDataRequest(
                    realSleep = RealSleep(
                        intervalText = DurationUtils.format(totalSeconds),
                        intervalValue = totalSeconds.toString(),
                        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        version = Build.VERSION.RELEASE
                    ),
                    sleeps = sleepsList,
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
}
