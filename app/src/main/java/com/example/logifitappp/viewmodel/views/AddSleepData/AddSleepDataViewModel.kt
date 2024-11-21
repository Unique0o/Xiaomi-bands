package com.example.logifitappp.viewmodel.views.AddSleepData

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class AddSleepDataViewModel @Inject constructor(
    private val saveSleepDataUseCase: SaveSleepDataUseCase,
    @ApplicationContext private val  context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(AddSleepDataState())
    val state: StateFlow<AddSleepDataState> = _state

    fun onEvent(event: AddSleepDataEvent) {
        when (event) {
            is AddSleepDataEvent.SetFellAsleepTime -> updateSleepEntry(event.index) { entry ->
                entry.copy(fellAsleepTime = event.time)
            }
            is AddSleepDataEvent.SetWokeUpTime -> updateSleepEntry(event.index) { entry ->
                entry.copy(wokeUpTime = event.time)
            }
            AddSleepDataEvent.AddSleepEntry -> addSleepEntry()
            is AddSleepDataEvent.RemoveSleepEntry -> removeSleepEntry(event.index)
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

    private fun updateSleepEntry(index: Int, update: (SleepEntry) -> SleepEntry) {
        _state.update { currentState ->
            val updatedEntries = currentState.sleepEntries.toMutableList()
            val entryIndex = updatedEntries.indexOfFirst { it.id == index }
            if (entryIndex != -1) {
                updatedEntries[entryIndex] = update(updatedEntries[entryIndex])
            }
            currentState.copy(sleepEntries = updatedEntries)
        }
    }

    private fun addSleepEntry() {
        _state.update { currentState ->
            val newId = (currentState.sleepEntries.maxOfOrNull { it.id } ?: -1) + 1
            val lastEntry = currentState.sleepEntries.maxByOrNull { it.wokeUpTime }
            val startTime = lastEntry?.wokeUpTime ?: LocalDateTime.now()
            val newEntry = SleepEntry(newId, startTime, startTime.plusHours(8))
            currentState.copy(sleepEntries = currentState.sleepEntries + newEntry)
        }
    }

    private fun removeSleepEntry(index: Int) {
        _state.update { currentState ->
            if (currentState.sleepEntries.size > 1) {
                currentState.copy(sleepEntries = currentState.sleepEntries.filterNot { it.id == index })
            } else currentState
        }
    }

    private fun handleMediaResult(uri: Uri) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    photoUri = uri,
                    errorMessage = null,
                    isValid = validateAllEntries(currentState.sleepEntries, uri)
                )
            }
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
    }

    private fun validateState() {
        _state.update { currentState ->
            val entriesValid = validateAllEntries(currentState.sleepEntries, currentState.photoUri)
            val errorMessage = getValidationError(currentState.sleepEntries, currentState.photoUri)
            currentState.copy(isValid = entriesValid, errorMessage = errorMessage)
        }
    }

    private fun validateAllEntries(entries: List<SleepEntry>, photoUri: Uri?): Boolean {
        return entries.isNotEmpty() &&
                photoUri != null &&
                entries.all { entry ->
                    entry.wokeUpTime.isAfter(entry.fellAsleepTime)
                } &&
                validateSequentialTimes(entries)
    }

    private fun validateSequentialTimes(entries: List<SleepEntry>): Boolean {
        return entries.sortedBy { it.fellAsleepTime }
            .zipWithNext()
            .all { (current, next) ->
                next.fellAsleepTime.isAfter(current.wokeUpTime)
            }
    }

    private fun getValidationError(entries: List<SleepEntry>, photoUri: Uri?): String? {
        return when {
            entries.isEmpty() -> "Debe agregar al menos un registro de sueño"
            entries.any { it.wokeUpTime <= it.fellAsleepTime } ->
                "La hora de despertar debe ser posterior a la hora de dormir"
            !validateSequentialTimes(entries) ->
                "Los períodos de sueño no pueden superponerse"
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

                val totalMinutes = currentState.sleepEntries.sumOf { entry ->
                    ChronoUnit.MINUTES.between(entry.fellAsleepTime, entry.wokeUpTime)
                }

                val request = SleepWrittenDataRequest(
                    realSleep = RealSleep(
                        intervalText = "${totalMinutes / 60}h ${totalMinutes % 60}min",
                        intervalValue = totalMinutes.toString(),
                        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        version = "4.6"
                    ),
                    sleeps = currentState.sleepEntries.map { entry ->
                        val sleepMinutes = ChronoUnit.MINUTES.between(
                            entry.fellAsleepTime,
                            entry.wokeUpTime
                        )
                        Sleep(
                            sleepIni = entry.fellAsleepTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            sleepEnd = entry.wokeUpTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            totalSleepText = "${sleepMinutes / 60}h ${sleepMinutes % 60}min",
                            totalSleepValue = sleepMinutes.toString()
                        )
                    },
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