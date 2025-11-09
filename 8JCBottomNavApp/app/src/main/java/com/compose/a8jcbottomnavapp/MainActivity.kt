package com.compose.a8jcbottomnavapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.compose.a8jcbottomnavapp.navigation.BottomNavigationBar
import com.compose.a8jcbottomnavapp.navigation.NavGraph
import com.compose.a8jcbottomnavapp.ui.theme._8JCBottomNavAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            // Use Scaffold to organize content
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(navController) // Add the BottomNavigationBar here
                }
            ) {
                paddingValues -> // Add padding for the content to avoid overlap
                NavGraph(navController = navController,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}
