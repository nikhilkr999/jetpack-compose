package com.nikhil.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Item(student: Student){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(

        ) {
            Text(text = student.name)
            Text(text = student.age.toString())
            Text(text = student.gender)
        }

    }
}