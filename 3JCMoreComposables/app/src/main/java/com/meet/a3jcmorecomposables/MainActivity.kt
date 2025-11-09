package com.meet.a3jcmorecomposables

import android.os.Bundle
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meet.a3jcmorecomposables.ui.theme._3JCMoreComposablesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                CheckBoxExample()
                SwitchExample()
                RadioButtonExample()
                RadioButtonWithText()
                CircularProgInd()
                LinearProgInd()
                DeterminateProgInd()
            }

        }
    }


    //1.CheckBox
    @Composable
    fun CheckBoxExample() {
        var checked by remember { mutableStateOf(false) }
        val context = androidx.compose.ui.platform.LocalContext.current  // ✅ Get context

        Column(modifier = Modifier.padding(top = 30.dp)) {
            Checkbox(
                checked = checked,
                onCheckedChange = { isChecked ->
                    checked = isChecked
                    Toast.makeText(context, "Is Checked $isChecked", Toast.LENGTH_SHORT).show() // ✅ use context
                }
            )
        }
    }

    //2.Switch
    @Composable
    fun SwitchExample(){
        var isChecked by remember { mutableStateOf(false) }
        Switch(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                Toast.makeText(this, "isChecked $isChecked", Toast.LENGTH_SHORT).show()
            }

        )
    }

    //3.RadioButtonExample
    @Composable
    fun RadioButtonExample(){
        var selectedOption by remember { mutableStateOf("Option 1") }
        Column {
            RadioButton(
                selected = selectedOption=="Option 1",
                onClick = {selectedOption = "Option 1"}
            )
            RadioButton(
                selected = selectedOption=="Option 2",
                onClick = {selectedOption = "Option 2"}
            )
            RadioButton(
                selected = selectedOption=="Option 3",
                onClick = {selectedOption = "Option 3"}
            )
        }
    }


    @Composable
    fun RadioButtonWithText() {
        var selectedOption by remember { mutableStateOf("Option 1") }
        Column {
            RadioButtonRow(
                "Option 1",
                selectedOption == "Option 1",
                {selectedOption = "Option 1"}
            )
            RadioButtonRow(
                "Option 2",
                selectedOption == "Option 2",
                {selectedOption = "Option 2"}
            )
        }
    }

    //Displaying text beside radio button
    @Composable
    fun RadioButtonRow(text:String, isSelected:Boolean, onSelected:()->Unit){
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = isSelected,
                onClick = onSelected,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(text = text)
        }
    }

    //4. Progress indicator
    //Circular progress indicator
    @Composable
    fun CircularProgInd(){
        CircularProgressIndicator(
        //progress = { 0.2f }
        )
    }

    //Linear progress indicator
    @Composable
    fun LinearProgInd(){
        LinearProgressIndicator(
            color = Color.Red
        )
    }

    //Determinate progress indicator
    @Composable
    fun DeterminateProgInd(){

    }

}


