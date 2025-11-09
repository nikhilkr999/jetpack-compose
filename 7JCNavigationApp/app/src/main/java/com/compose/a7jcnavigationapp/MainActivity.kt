package com.compose.a7jcnavigationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.a7jcnavigationapp.ui.theme._7JCNavigationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "first"
            ) {
                composable("first") {
                    FirstScreen(navController)
                }

                composable(
                    route = "second/{username}?age={age}",
                    arguments = listOf(
                        navArgument("username") {
                            type = NavType.StringType
                        },
                        navArgument("age"){
                            type = NavType.StringType
                            defaultValue="30"    //making second field "age" optional wont cause crash if not entered age
                            nullable = true
                        }
                    )
                ) { backStackEntry ->
                    SecondScreen(
                        navController,
                        backStackEntry.arguments?.getString("username").toString(),
                        backStackEntry.arguments?.getString("age").toString()
                    )
                }
            }
        }
    }
}


@Composable
fun FirstScreen(navController: NavController){
    Column(
        modifier = Modifier.padding(40.dp)
    ) {
        Text(text = "First Screen")

        var enteredText by remember { mutableStateOf("") }
        var enteredText2 by remember { mutableStateOf("") }

        TextField(
            value = enteredText,
            onValueChange = {enteredText = it},
            label = {Text(text = "Enter username")}
        )
        TextField(
            value = enteredText2,
            onValueChange = {enteredText2 = it},
            label = {Text(text = "Enter age")}
        )
        Button(onClick = {
            navController.navigate("second/$enteredText?age=/$enteredText2")
        }) {
            Text(text = "Go to second screen")
        }
    }
}

@Composable
fun SecondScreen(navController: NavController, userName:String, age:String){
    Column(
        modifier = Modifier.padding(40.dp)
    ) {
        Text(text = "Welcome $userName Your age is $age" )
        Button(onClick = {
            navController.navigateUp()
        }) {
            Text(text = "Go to First Screen")
        }
    }
}
