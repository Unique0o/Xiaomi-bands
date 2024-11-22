package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName


data class NotificationBody(
    @SerializedName("body") val body: String,
    @SerializedName("title") val title: String
)

data class NotificationStructure(
    @SerializedName("id") val id: String,
    @SerializedName("data") val data: NotificationBody,
    @SerializedName("read_at") val readAt: String?,
    @SerializedName("created_at") val createdAt: String
)

data class NotificationResponse(
    @SerializedName("data") val data: List<NotificationStructure>
)