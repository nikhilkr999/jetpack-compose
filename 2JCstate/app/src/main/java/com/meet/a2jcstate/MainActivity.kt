package com.meet.a2jcstate

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
import com.meet.a2jcstate.ui.theme._2JCStateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           // Counter()

            //StatefullTextField()


            var text by remember { mutableStateOf("") }
            StateLessTextFiled(
                text = text,
                onTextChanged = {newText->text = newText}
            )
        }
    }
}


//State Management
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.padding(80.dp)
    ) {
        Text(text = "The counter: $count")
        Button(onClick = { count++ }) {
            Text(text = "Increase counter")
        }

    }
}

@Composable
fun StatefullTextField(){
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(80.dp)) {
        TextField(
            value = text,
            onValueChange = {
                newText -> text  = newText
            },
            label = {Text(text = "enter text")}
        )
        Text(text = "You typed $text")
    }
}

@Composable
fun StateLessTextFiled(text:String, onTextChanged:(String)->Unit){
    Column(modifier = Modifier.padding(0.dp)) {
        TextField(
            value = text,
            onValueChange = onTextChanged,
            label = {Text(text = "Enter text")}
        )
        Text(text = "You typed $text")
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    _2JCStateTheme {
    }
}