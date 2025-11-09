package com.compose.a10jccoursesapp.screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyCard(modifier: Modifier){
    Card (
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(
            topEnd = 32.dp,
            topStart = 32.dp
        ),
        modifier = modifier
    ){

    }
}