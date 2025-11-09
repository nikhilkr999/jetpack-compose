package com.compose.a12jcnewsapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.a12jcnewsapp.repository.PostRepository
import com.compose.a12jcnewsapp.retrofit.Post
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {
    private val repository = PostRepository()

    // ViewModel uses MutableStateOf<List<Post>> instead of Live Data
    // Allowing direct Compose state Handling
    var posts by mutableStateOf<List<Post>>(emptyList())
        private set  //only PostViewModel can change 'posts'
    init {
        viewModelScope.launch {
            // Fetching Data
            val fetchedPosts = repository.getPosts()

            // Updating the State
            // any update to 'posts' will trigger
            // a recomposition of any Composable
            // that read this state
            posts= fetchedPosts
        }
    }
}