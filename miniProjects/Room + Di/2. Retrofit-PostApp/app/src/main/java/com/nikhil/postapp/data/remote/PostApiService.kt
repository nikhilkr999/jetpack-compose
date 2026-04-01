package com.nikhil.postapp.data.remote

import retrofit2.http.GET

interface PostApiService {
    @GET("posts")
    suspend fun getPosts() : List<Post>
}