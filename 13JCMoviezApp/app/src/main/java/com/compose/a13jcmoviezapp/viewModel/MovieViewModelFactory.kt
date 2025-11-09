package com.compose.a13jcmoviezapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.compose.a13jcmoviezapp.repository.Repository

class MovieViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)){

            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")


    }
}