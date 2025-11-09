package com.compose.a11jcnotey.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.compose.a11jcnotey.repository.NotesRepository

class NoteViewModelFactory(private val repository: NotesRepository)
    : ViewModelProvider.Factory{

    // if your ViewModel requires additional parameters,
    // such as a 'repository' or a 'context', you need to
    // create a 'ViewModelProvider.Factory' to handle
    // the instantiation

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(NoteViewModel::class.java)){

            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}