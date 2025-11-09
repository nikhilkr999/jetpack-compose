package com.compose.a10jccoursesapp.screens


import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.compose.a10jccoursesapp.R


@Composable
fun WelcomeText(modifier: Modifier){
    Text(
        text = "Welcome Back",
        fontSize = 32.sp,
        color = Color.Blue,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}
@Composable
fun QuestionText(modifier: Modifier){
    Text(
        text = "What do you want to learn?",
        fontSize = 18.sp,
        color = Color.Black,
        modifier = modifier
    )
}

@Composable
fun JoinNowButton(modifier: Modifier, onCLick:()-> Unit){
    Button(
        onClick = {onCLick()},
        modifier = modifier
    ){
        Text(text = "Join now")
    }
}

@Composable
fun CoursesImage(modifier: Modifier){
    Image(
        painter = painterResource(R.drawable.fruits),
        contentDescription = "Courses",
        modifier = modifier
    )
}