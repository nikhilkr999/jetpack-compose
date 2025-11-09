package com.compose.a11jcnotey.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.a11jcnotey.roomdb.Note

@Composable
fun NoteListItem(note: Note){

    Card (
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(note.color)),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier.padding(4.dp)

    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)


        ){
            Text(text = "${note.title}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = "${note.description}",
                fontSize = 16.sp)



        }
    }
}