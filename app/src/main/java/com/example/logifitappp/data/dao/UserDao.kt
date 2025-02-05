package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.data.models.UserModel

@Dao
abstract class UserDao {
    @Query("DELETE FROM users WHERE has_logged_in = 1")
    abstract fun deleteLoggedIn()

    @Query("SELECT * FROM users WHERE has_logged_in = 1 LIMIT 1")
    abstract fun getLoggedIn(): UserModel?

    @Query("UPDATE users SET has_logged_in = 0")
    abstract fun logout()

    @Upsert
    abstract fun store(user: UserModel)
}