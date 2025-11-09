package com.compose.a11jcnotey.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.a11jcnotey.repository.NotesRepository
import com.compose.a11jcnotey.roomdb.Note
import kotlinx.coroutines.launch

// View Model: store & Manage UI-related Data
// separating the UI-related logic from
// UI controller (composable/Activity/Frag.)
class NoteViewModel(private val repository: NotesRepository): ViewModel() {

    val allNotes : LiveData<List<Note>> = repository.allNotes

    // viewModelScope: is a coroutine scope tied to the
    // ViewModel's Lifecycle. Ensuring that any coroutines
    // launched within it are automatically canceled if
    // the ViewModel is cleared
    // launch: is a coroutine builder that launches a new
    fun insert(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }
}