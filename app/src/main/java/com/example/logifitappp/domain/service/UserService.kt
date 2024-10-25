package com.example.logifitappp.domain.service


import android.content.Context
import android.net.Uri
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.domain.repository.UserRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class UserService @Inject constructor(
    private val userRepository: UserRepository,
    private val context: Context
) {
    suspend fun storePersonalInformation(
        userId: Int,
        request: StorePersonalInformationRequest
    ) {
        try {
            val response = userRepository.store(userId, request)

            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }

            response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

        } catch (e: Exception) {
            when (e) {
                is HttpConsumerException -> throw e
                else -> throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
            }
        }
    }

    suspend fun updateProfilePhoto(userId: String, uri: Uri): Response<Unit> {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_profile_photo.jpg")

        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return userRepository.uploadProfilePhoto(userId, imagePart)
    }
}