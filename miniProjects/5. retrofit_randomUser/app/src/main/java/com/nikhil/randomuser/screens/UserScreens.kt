package com.nikhil.randomuser.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nikhil.randomuser.UiState
import com.nikhil.randomuser.viewModel.UserViewModel

@Composable
fun UserScreen(viewModel: UserViewModel){
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when(state){
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Success -> {
                val user = (state as UiState.Success).user

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = user.picture.large,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )

                    Text(text = "${user.name.firstName} ${user.name.lastName}")
                    Text(text = user.email)

                    Button(onClick = {
                        viewModel.fetchUser()
                    }) {
                        Text(text = "Refresh")
                    }
                }
            }

            is UiState.Error ->{
                Text(text = (state as UiState.Error).message)
            }
        }
    }
}
