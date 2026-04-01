package com.nikhil.catfactsapp.data.remote

import com.nikhil.catfactsapp.domain.model.CatFact
import retrofit2.http.GET

interface ApiService {

    @GET("fact")
    suspend fun getCatFacts() : CatFact
}