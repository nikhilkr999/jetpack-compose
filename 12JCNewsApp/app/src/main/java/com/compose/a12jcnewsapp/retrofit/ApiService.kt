package com.compose.a12jcnewsapp.retrofit

import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts() : List<Post>
}