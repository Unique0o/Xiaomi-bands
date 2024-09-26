package com.example.logifitappp.domain.repository

interface UserRepository {
    suspend fun isUserAdmin(): Boolean
}