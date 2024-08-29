package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.User

interface LeaderboardRepository {
    suspend fun getLeaderboard(): List<User>
}