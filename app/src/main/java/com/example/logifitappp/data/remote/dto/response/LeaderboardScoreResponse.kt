package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class UserScoreResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("profile") val profilePhoto: String?
)

data class ScoreResponse(
    @SerializedName("score_total") val points: Int,
    val user: UserScoreResponse
)

data class LeaderboardScoreResponse(
    @SerializedName("scores") val score: List<ScoreResponse>
)
