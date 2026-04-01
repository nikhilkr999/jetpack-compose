package com.nikhil.randomuser

import com.nikhil.randomuser.model.User

sealed class UiState {
    object Loading : UiState()
    data class Success(val user : User) : UiState()
    data class Error(val message : String ): UiState()
}