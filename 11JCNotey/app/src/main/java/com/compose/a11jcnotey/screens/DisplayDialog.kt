package com.compose.a11jcnotey.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.compose.a11jcnotey.roomdb.Note
import com.compose.a11jcnotey.viewModel.NoteViewModel

@Composable
fun DisplayDialog(viewModel: NoteViewModel,
                  showDialog: Boolean,
                  onDismiss: ()-> Unit){

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var selectedColor by remember {
        mutableStateOf(Color.Blue)
    }


    if (showDialog){
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Enter Note")},
            text = {
                Column {

                    TextField(
                        value = title,
                        onValueChange = {title = it},
                        label = { Text(text = "Note Title")}
                    )
                    Spacer(modifier = Modifier.height(8.dp))


                    TextField(
                        value = description,
                        onValueChange = {description=it},
                        label = { Text(text = "Note Description")}
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    // Color Picker Composable
                    MyColorPicker(
                        selectedColor = selectedColor,
                        onColorSelected = {selectedColor = it})

                }

            },

            confirmButton = {
                Button(onClick = {
                    var note = Note(
                        0,
                        title,
                        description,
                        // toArgb(): converts a 'Color'
                        // object into an integer representation
                        // of its ARGB (Alpha, Red, Green, Blue)
                        selectedColor.toArgb()
                    )

                    // insert note into DB
                    viewModel.insert(note)





                }) {

                    Text(text = "Save Note")

                }
            },

            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            }

        )
    }



}