package com.compose.a10jccoursesapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.compose.a10jccoursesapp.R

@Composable
fun ProfileImage(modifier: Modifier){

    Image(painter = painterResource(R.drawable.appointment_doctor),
        contentDescription = "Profile",
        modifier = modifier.padding(16.dp).clip(CircleShape).size(42.dp)
    )
}

@Composable
fun NotificationImage(modifier: Modifier){

    Image(painter = painterResource(R.drawable.profile),
        contentDescription = "Notification",
        modifier = modifier.padding(16.dp).clip(CircleShape).size(42.dp))
}