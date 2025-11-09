package com.compose.a12jcnewsapp.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.compose.a12jcnewsapp.retrofit.Post

@Composable
fun PostList(posts: List<Post>){

    LazyColumn {
        items(posts){
                post -> PostItem(post = post)
        }
    }

}