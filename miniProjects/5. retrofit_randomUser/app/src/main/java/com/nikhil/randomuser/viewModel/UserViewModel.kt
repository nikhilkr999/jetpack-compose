package com.nikhil.randomuser.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.randomuser.UiState
import com.nikhil.randomuser.repository.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepo) : ViewModel( ) {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val state : StateFlow<UiState> = _uiState

    init {
        fetchUser()
    }

    fun fetchUser(){
        viewModelScope.launch {
            _uiState.value= UiState.Loading

            try {
                val response = userRepo.getRandomUser()
                _uiState.value = UiState.Success(response)
            }catch (e : Exception){
                _uiState.value = UiState.Error("Failed to load user")
            }
        }
    }
}