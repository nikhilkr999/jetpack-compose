package com.nikhil.catfactsapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nikhil.catfactsapp.presentation.state.UiState
import com.nikhil.catfactsapp.presentation.viewmodel.CatViewModel

@Composable
fun CatScreen(
    viewModel: CatViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = uiState.value) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val catFact = state.catFact
                Text(
                    text = "Cat Fact: ${catFact.fact}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            is UiState.Error -> {
                Text(text = state.message)
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { viewModel.fetchFacts() }
        ) {
            Text(text = "Get new fact")
        }
    }
}
