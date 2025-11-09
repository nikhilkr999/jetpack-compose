package com.compose.a12jcnewsapp.repository

import com.compose.a12jcnewsapp.retrofit.Post
import com.compose.a12jcnewsapp.retrofit.RetrofitInstance

class PostRepository {
    // Repository: Acts as a mediator between
    // the data source(network API) & the ViewModel

    private val apiService = RetrofitInstance.api

    suspend fun getPosts() : List<Post>{
        return apiService.getPosts()
    }
}