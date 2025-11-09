package com.meet.a4jclayoutsdeepdive

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meet.a4jclayoutsdeepdive.ui.theme._4JCLayoutsDeepDiveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //MyRow()
            //MyColumn()
            //MyBox()
            // MySurface()

            Scaffold(
                topBar = { MyTopAppBar()},
                bottomBar = {
                    MyBottomAppBar()
                },
                floatingActionButton = {
                    MyFab()
                }
            ) { innerPadding ->
                // Apply innerPadding to the content to avoid overlap
                MyColumn(modifier = Modifier.padding(innerPadding))
            }


        }
    }

    //1. row
    @Composable
    fun MyRow() {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "welcome to ")
            Text(text = "Master Coding")
        }
    }

    //2.Column
    @Composable
    fun MyColumn(modifier: Modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "welcome to ")
            Text(text = "mastercoding app")
            Text(text = "Download from playstore")
        }
    }

    //3.Box
    @Composable
    fun MyBox() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "welcome to ",
                modifier = Modifier.align(Alignment.TopStart)
            )
            Text(
                text = "mastercoding app",
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = "Download from playstore",
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }

    //4.Surface
    /* Holds one child at a time
        provides many style treatment for its children
     */
    @Composable
    fun MySurface() {
        Surface(
            modifier = Modifier.size(100.dp),
            color = Color.Red,
            contentColor = colorResource(R.color.black),
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, Color.Green)
        ) {
            MyColumn()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyTopAppBar(){
        TopAppBar(
            title = {Text(text = "Top app bar")},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Green,
                titleContentColor = Color.Black
            ),
            navigationIcon = {
                IconButton(onClick = {
                    Toast.makeText(this, "You clicked the nav icon", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(painter = painterResource(R.drawable.outline_menu_24),
                        contentDescription = "menu")
                }
            },
            actions = {
                IconButton(onClick = {
                    Toast.makeText(applicationContext, "You clicked the setting icon", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(painter = painterResource(R.drawable.outline_settings_24),
                        contentDescription = "Setting")
                }
            }
        )
    }

    @Composable
    fun MyBottomAppBar(){
        BottomAppBar(
            containerColor = Color.DarkGray,
            contentColor = Color.Yellow
        ){
            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
            ){
                IconButton(onClick = {
                    Toast.makeText(applicationContext,"Clicked first Icon", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(painter = painterResource(R.drawable.baseline_home_24),
                        contentDescription = "home")
                }


                IconButton(onClick = {
                    Toast.makeText(applicationContext,"Clicked second Icon", Toast.LENGTH_SHORT).show()

                }) {
                    Icon(painter = painterResource(R.drawable.baseline_collections_24),
                        contentDescription = "home")
                }

                IconButton(onClick = {
                    Toast.makeText(applicationContext,"Clicked second Icon", Toast.LENGTH_SHORT).show()

                }) {
                    Icon(painter = painterResource(R.drawable.baseline_people_24),
                        contentDescription = "home")
                }
            }
            }

    }

    @Composable
    fun MyFab(){
        FloatingActionButton(
            onClick = {
                Toast.makeText(applicationContext,"Clicked fab Icon", Toast.LENGTH_SHORT).show()
            },
            containerColor = Color.Red,
            contentColor = Color.Black
        ) {
            Icon(painter = painterResource(R.drawable.baseline_add_24),
                contentDescription = "fab")
        }
    }
}


