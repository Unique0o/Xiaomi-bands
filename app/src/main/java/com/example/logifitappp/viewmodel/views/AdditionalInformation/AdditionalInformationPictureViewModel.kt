package com.example.logifitappp.viewmodel.views.AdditionalInformation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.domain.repository.UserRepository
import com.example.logifitappp.domain.service.UserService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import android.util.Base64


@HiltViewModel
class AdditionalInformationPictureViewModel @Inject constructor(
    private val userService: UserService,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(PhotoUploadState())
    val state: StateFlow<PhotoUploadState> = _state.asStateFlow()

    fun updatePhoto(uri: Uri) {
        viewModelScope.launch {
            try {
                if (!isValidImageSize(uri)) {
                    _state.update { it.copy(error = "La imagen es demasiado grande") }
                    return@launch
                }
                val base64 = convertImageToBase64(uri)
                _state.update {
                    it.copy(
                        photoUri = uri,
                        photoBase64 = base64,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al seleccionar la imagen") }
            }
        }
    }

    fun uploadPhoto(onSuccess: () -> Unit) {
        if (!validatePhoto()) return

        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val currentUser = userRepository.getLoggedIn()
                    ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

                currentUser.external_identifier?.let { userId ->
                    state.value.photoUri?.let { uri ->
                        val response = withContext(Dispatchers.IO) {
                            userService.updateProfilePhoto(userId.toString(), uri)
                        }

                        if (response.isSuccessful) {
                            userRepository.createOrUpdate(
                                currentUser.copy(
                                    profilePhoto = state.value.photoBase64 ?: uri.toString()
                                )
                            )
                            _state.update { it.copy(isLoading = false) }
                            onSuccess()
                        } else {
                            throw HttpConsumerException(AppStatusCodeEnum.UNKNOWN_ERROR)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = when (e) {
                            is HttpConsumerException -> e.message
                            else -> "Error al subir la foto"
                        }
                    )
                }
            }
        }
    }

    private fun validatePhoto(): Boolean {
        if (state.value.photoUri == null) {
            _state.update {
                it.copy(error = "Debes seleccionar una foto de perfil")
            }
            return false
        }
        return true
    }

    private suspend fun isValidImageSize(uri: Uri): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val size = inputStream?.available() ?: 0
                inputStream?.close()
                size <= MAX_IMAGE_SIZE
            } catch (e: Exception) {
                false
            }
        }
    }

    private suspend fun convertImageToBase64(uri: Uri): String {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                Base64.encodeToString(bytes, Base64.DEFAULT)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    companion object {
        private const val MAX_IMAGE_SIZE = 5 * 1024 * 1024 // 5MB
    }
}

