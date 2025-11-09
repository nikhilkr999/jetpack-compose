package com.meet.a3jccodingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meet.a3jccodingchallenge.ui.theme._3JCCodingChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var progress by remember { mutableStateOf(0.0f) }
            Column(modifier = Modifier.padding(top = 100.dp)){

                LinearProg(progress)
                BtnIncreaseProg {
                    progress += 0.1f

                    if(progress >= 1f){
                        progress = 0f
                    }
                }
                ProgressText(progress)
            }


        }
    }
}

/** Coding challenge
 * Create 3 composable
 * 1- Linear progress indicator
 * 2- Button : Allows the user to increase the progress when button is clicked
 * 3- Text: Displaying the progress
 *
 * Note: All this component should be stateless
 */


@Composable
fun LinearProg(progress: Float){
    LinearProgressIndicator(progress = progress)
}

@Composable
fun BtnIncreaseProg(onCLick: ()-> Unit){
    Button(onClick = onCLick) {
        Text(text = "Increase prog")
    }
}

@Composable
fun ProgressText(progress: Float){
    Text(text = "The progress is $progress")
}

