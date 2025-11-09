package com.compose.a10jccoursesapp.screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.compose.a10jccoursesapp.R

@Composable
fun BackgroundGradient(modifier: Modifier){
    Image(painter = painterResource(id = R.drawable.gradient_background),
        contentDescription = "Main Background",
        contentScale = ContentScale.FillBounds,
        modifier = modifier)
}