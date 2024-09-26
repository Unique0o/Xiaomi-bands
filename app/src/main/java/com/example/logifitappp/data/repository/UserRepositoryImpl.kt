package com.example.logifitappp.data.repository

import com.example.logifitappp.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(

) : UserRepository {

    override suspend fun isUserAdmin(): Boolean {
        return false
    }
}