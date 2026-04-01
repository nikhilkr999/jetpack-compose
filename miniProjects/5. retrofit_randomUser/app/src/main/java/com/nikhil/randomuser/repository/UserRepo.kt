package com.nikhil.randomuser.repository

import com.nikhil.randomuser.api.ApiService
import com.nikhil.randomuser.model.User

class UserRepo(
    private val api : ApiService
) {
    suspend fun getRandomUser() : User {
        val response = api.getRandomUser()
        return response.results[0]
    }
}