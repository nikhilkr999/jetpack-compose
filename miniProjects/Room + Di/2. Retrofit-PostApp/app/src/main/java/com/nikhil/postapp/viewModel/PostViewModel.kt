package com.nikhil.postapp.viewModel

import androidx.lifecycle.ViewModel
import com.nikhil.postapp.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository : PostRepository ) : ViewModel(){

}