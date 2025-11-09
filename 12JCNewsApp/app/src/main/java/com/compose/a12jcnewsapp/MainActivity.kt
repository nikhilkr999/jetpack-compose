package com.compose.a12jcnewsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.a12jcnewsapp.screen.PostScreen
import com.compose.a12jcnewsapp.ui.theme._12JCNewsAppTheme
import com.compose.a12jcnewsapp.viewModel.PostViewModel

class MainActivity : ComponentActivity() {

    // View Model Initialization
    // viewModels(): used to create viewmodel instances
    // no need to manually handle the ViewModelProvider
    private val myViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            _12JCNewsAppTheme {
                Column {
                    HeaderComposable()
                    PostScreen(viewModel = myViewModel )
                }

            }
        }
    }
}

@Composable
fun HeaderComposable(){

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(text = "The News App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold)

        Text(text = "Get the latest Posts & News",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }


}
