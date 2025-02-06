package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.AssociateWearableRequest
import com.example.logifitappp.data.remote.dto.requests.StoreXiaomiMacRequest
import com.example.logifitappp.data.remote.dto.response.FetchXiaomiCredentialResponse
import com.example.logifitappp.data.remote.dto.response.FetchXiaomiMacResponse
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.StoreXiaomiMacResponse
import com.example.logifitappp.data.remote.dto.response.WearableAuthenticationKeyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WearableApi {
    @POST("api/workers/{id}/associate-device")
    suspend fun associate(@Path("id") userId: Int, @Body associateWearableRequest: AssociateWearableRequest): Response<GeneralResponse>

    @GET("api/devices/by/mac/{mac}")
    suspend fun fetchAuthenticationKey(@Path("mac") mac: String): Response<WearableAuthenticationKeyResponse>

    @GET("api/xiaomi_credentials")
    suspend fun fetchXiaomiCredentials(): Response<List<FetchXiaomiCredentialResponse>>

    @GET("api/xiaomi_macs")
    suspend fun fetchXiaomiMacs(@Query("credentials") credentialId: Int, @Query("password") password: String, @Query("type") type: String?, @Query("username") username: String): Response<List<FetchXiaomiMacResponse>>

    @POST("api/device_update")
    suspend fun storeXiaomiMacs(@Body storeXiaomiMacRequest: StoreXiaomiMacRequest): Response<StoreXiaomiMacResponse>
}