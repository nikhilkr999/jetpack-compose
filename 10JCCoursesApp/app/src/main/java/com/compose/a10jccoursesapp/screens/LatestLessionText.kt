package com.compose.a10jccoursesapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.compose.a10jccoursesapp.R

@Composable
fun LatestLessionText(modifier: Modifier){
    Text(
        text = "Latest Lessions",
        fontSize = 20.sp,
        color = Color.Black,
        modifier = modifier
    )
}

@Composable
fun SeeAllText(modifier: Modifier){
    Text(
        text = "See All",
        fontSize = 20.sp,
        color = Color.Blue,
        modifier = modifier
    )
}

@Composable
fun LessionCard(modifier: Modifier){
    Card (
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ){
        Box(
            modifier = modifier

                .wrapContentSize(Alignment.TopStart)
                .fillMaxHeight()
                .background(Color.White)
        ){
            Spacer(modifier = Modifier.width(8.dp))

            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (lessionDate, timeimg, lessionSchedule, lessionImg, lessionTitle, lessionDec) = createRefs()

                Text(
                    text = "lession date",
                    color = Color.Black,
                    modifier = Modifier.constrainAs(lessionDate){
                        top.linkTo(parent.top, 16.dp)
                        start.linkTo(parent.start, 16.dp)
                    })
                Image(
                    painter = painterResource(R.drawable.baseline_watch_later_24),
                    contentDescription = "time",
                    modifier = Modifier.constrainAs(timeimg){
                        top.linkTo(lessionDate.bottom, 8.dp)
                        start.linkTo(lessionDate.start)
                    }
                )
                Text(
                    text = "Thus June 6 | 8:00 - 8:30 PM",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.constrainAs(lessionSchedule){
                        top.linkTo(timeimg.top)
                        start.linkTo(timeimg.end, 8.dp)
                        bottom.linkTo(timeimg.bottom)
                    })
                Image(
                    painter = painterResource(R.drawable.doctor),
                    contentDescription = "time",
                    modifier = Modifier.constrainAs(lessionImg){
                        top.linkTo(timeimg.bottom, 16.dp)
                        start.linkTo(timeimg.start)
                        bottom.linkTo(parent.bottom, 16.dp)
                    }.size(60.dp)

                )

                Text(
                    text = "Data structure and algorithm",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.constrainAs(lessionTitle){
                        top.linkTo(lessionImg.top)
                        start.linkTo(lessionImg.end, 12.dp)
                    }
                )
                Text(
                    text = "To be a pro developer you need to master datastructure and algorithm",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.constrainAs(lessionDec){
                        top.linkTo(lessionTitle.bottom, 2.dp)
                        start.linkTo(lessionTitle.start )

                    }
                )


            }
        }
    }
}

