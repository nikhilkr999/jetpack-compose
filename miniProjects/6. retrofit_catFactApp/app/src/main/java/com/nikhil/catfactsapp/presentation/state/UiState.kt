package com.nikhil.catfactsapp.presentation.state

import com.nikhil.catfactsapp.domain.model.CatFact

sealed class UiState {
    object Loading : UiState()
    data class Success(val catFact: CatFact) : UiState()
    data class Error(val message : String) : UiState()
}