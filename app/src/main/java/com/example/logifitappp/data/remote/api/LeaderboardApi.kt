package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.LeaderboardScoreResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface LeaderboardApi {
    @GET("api/score_list/{id}")
    suspend fun fetchScore(@Path("id") userIdentifier: Int): Response<LeaderboardScoreResponse>
}