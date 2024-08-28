package com.example.logifitappp.data

data class User(
    val name: String,
    val score: Int,
    val imageResId: Int,
    val isOwner: Boolean,
    val id: Int
)

data class LeaderboardData(
    val users: List<User>,
    val topScore: Int,
    val bottomScore: Int
)

interface LeaderboardRepository {
    suspend fun getLeaderboard(): LeaderboardData
    suspend fun getSelfUser(): User
    suspend fun shouldShowMessage(): Boolean
}