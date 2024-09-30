package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName


data class Notification(
    @SerializedName("body") val body: String,
    @SerializedName("title") val title: String
)

data class NotificationResponse(
    @SerializedName("id") val id: String,
    @SerializedName("data") val data: Notification,
    @SerializedName("read_at") val readAt: String?,
    @SerializedName("created_at") val createdAt: String
)