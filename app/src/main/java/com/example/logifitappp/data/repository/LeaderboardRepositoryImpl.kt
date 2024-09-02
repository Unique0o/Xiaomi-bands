package com.example.logifitappp.data.repository

import com.example.logifitappp.R
import com.example.logifitappp.data.User
import com.example.logifitappp.domain.repository.LeaderboardRepository

class LeaderboardRepositoryImpl : LeaderboardRepository {
    override suspend fun getLeaderboard(): List<User> {

        return listOf(
            User("User 1", 300, R.drawable.user1, true, 1),
            User("User 2", 250, R.drawable.user1, false, 2),
            User("User 3", 200, R.drawable.user1, false, 3),
            User("RICARDO LOPEZ HUAMAN", 110, R.drawable.user1, false, 4),
            User("CARLOS EDUARDO TORRES ZARATE", 105, R.drawable.user1, false, 5),
            User("User 6", 90, R.drawable.user1, false, 6)
        )
    }
}