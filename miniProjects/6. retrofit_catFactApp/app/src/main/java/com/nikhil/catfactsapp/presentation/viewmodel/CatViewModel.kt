package com.nikhil.catfactsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.catfactsapp.data.repository.CatFactRepo
import com.nikhil.catfactsapp.domain.model.CatFact
import com.nikhil.catfactsapp.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatViewModel : ViewModel() {

    private val catFactRepo = CatFactRepo()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState>  = _uiState

    init {
        fetchFacts()
    }

    fun fetchFacts(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                val catFact = catFactRepo.getCatFacts()
                _uiState.value = UiState.Success(catFact)
            }catch (e : Exception){
                _uiState.value = UiState.Error(e.message?:"Unknown error")
            }
        }
    }
}