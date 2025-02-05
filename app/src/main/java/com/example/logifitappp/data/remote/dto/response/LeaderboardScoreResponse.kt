package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("profile") val profilePhoto: String?
)

data class Score(
    @SerializedName("score_total") val points: Int,
    @SerializedName("user") val user: User
)

data class LeaderboardScoreResponse(
    @SerializedName("scores") val score: List<Score>
)
