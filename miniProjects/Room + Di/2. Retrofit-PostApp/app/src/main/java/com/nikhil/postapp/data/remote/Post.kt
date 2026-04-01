package com.nikhil.postapp.data.remote

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)