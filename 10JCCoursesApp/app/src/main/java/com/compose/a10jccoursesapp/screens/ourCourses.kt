package com.compose.a10jccoursesapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.a10jccoursesapp.R

@Composable
fun TextOurCourses(modifier: Modifier){
    Text(
        text = "New Courses",
        modifier = modifier,
        fontSize = 20.sp,
        color = Color.Black
    )
}

@Composable
fun AndroidCourseImage(modifier: Modifier){
    Image(
        painter = painterResource(R.drawable.doctor ),
        contentDescription = "Android Courses",
        modifier = modifier.size(80.dp)
    )
}

@Composable
fun JavaCourseImage(modifier: Modifier){
    Image(
        painter = painterResource(R.drawable.profile),
        contentDescription = "Java Course",
        modifier = modifier.size(80.dp)
    )
}

@Composable
fun PythonCourseImage(modifier: Modifier){
    Image(
        painter = painterResource(R.drawable.appointment_doctor),
        contentDescription = "Java Course",
        modifier = modifier.size(80.dp)
    )
}

@Composable
fun AndroidText(modifier: Modifier){
    Text(
        text = "Android",
        fontWeight = FontWeight.SemiBold,
        modifier = modifier,
        fontSize = 18.sp
    )
}
@Composable
fun JavaText(modifier: Modifier){
    Text(
        text = "Java",
        fontWeight = FontWeight.SemiBold,
        modifier = modifier,
        fontSize = 18.sp
    )
}
@Composable
fun PythonText(modifier: Modifier){
    Text(
        text = "Python",
        fontWeight = FontWeight.SemiBold,
        modifier = modifier,
        fontSize = 18.sp
    )
}

