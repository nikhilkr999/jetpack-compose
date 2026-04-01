package com.nikhil.randomuser.api

import com.nikhil.randomuser.model.ResponseData
import retrofit2.http.GET

interface ApiService {

    @GET("api/")
    suspend fun getRandomUser(): ResponseData
}