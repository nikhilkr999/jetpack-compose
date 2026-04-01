package com.nikhil.postapp.data.repository

import com.nikhil.postapp.data.local.PostDao
import com.nikhil.postapp.data.remote.PostApiService
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postApi : PostApiService,
    private val postDao : PostDao
) {

    suspend fun fetchPosts(){
        try {
            val posts = postApi.getPosts()
            
        }catch (e : Exception){

        }
    }

}