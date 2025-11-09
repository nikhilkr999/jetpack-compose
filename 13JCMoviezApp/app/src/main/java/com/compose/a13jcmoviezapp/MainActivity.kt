package com.compose.a13jcmoviezapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.compose.a13jcmoviezapp.repository.Repository
import com.compose.a13jcmoviezapp.screen.MovieScreen
import com.compose.a13jcmoviezapp.ui.theme._13JCMoviezAppTheme
import com.compose.a13jcmoviezapp.viewModel.MovieViewModel
import com.compose.a13jcmoviezapp.viewModel.MovieViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = Repository(applicationContext)

        val viewModelFactory = MovieViewModelFactory(repository)

        val movieViewModdel = ViewModelProvider(
            this,
            viewModelFactory
        )[MovieViewModel::class]

        setContent {
            _13JCMoviezAppTheme {
                MovieScreen(movieViewModdel)
            }
        }
    }
}