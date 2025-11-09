package com.compose.a10jccoursesapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ConstraintLayoutScreen(){
    ConstraintLayout (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        //Creating refrences for Composables that will be constraint within the layout
         val (gradientBackground, profileImage, notificationImage,
             welcomeText, questionText, joinNowButton, coursesImage, card, newCourses,
             androidCourse, javaCourse, pythonCourse,
             androidText, javaText, pythonText) = createRefs()

        ///create another creatRef if it exceeds 16 references
        val ( latestLession, seeAll, lessionCard) = createRefs()


        //Guideline
        val horizontalGuideline1 = createGuidelineFromTop(0.45f)

        //1. background
        BackgroundGradient(modifier = Modifier.constrainAs(gradientBackground){
            top.linkTo(parent.top)
            end.linkTo(parent.end)
            start.linkTo(parent.start)
            bottom.linkTo(horizontalGuideline1)

            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        })

        //2. header- (chain example)
        val topGuidLine = createGuidelineFromTop(30.dp)
        val startGuidline = createGuidelineFromStart(16.dp)
        val endGuidLine = createGuidelineFromEnd(16.dp)

        // Profile Image
        ProfileImage(
            modifier = Modifier.constrainAs(profileImage) {
                top.linkTo(topGuidLine)
                start.linkTo(startGuidline)
            }
        )

        // Notification Image
        NotificationImage(
            modifier = Modifier.constrainAs(notificationImage) {
                top.linkTo(topGuidLine)
                end.linkTo(endGuidLine)
            }
        )

        //chain
        // Optional: Create horizontal chain between profile and notification
        createHorizontalChain(profileImage, notificationImage, chainStyle = ChainStyle.SpreadInside)


        //3. middle part ui

        WelcomeText(modifier = Modifier
            .constrainAs(welcomeText){
                top.linkTo(profileImage.bottom, margin = 32.dp)
                start.linkTo(startGuidline)
            })
        QuestionText(modifier = Modifier
            .constrainAs(questionText){
                top.linkTo(welcomeText.bottom, margin = 16.dp)
                start.linkTo(welcomeText.start)
            })
        JoinNowButton(
            modifier = Modifier
                .constrainAs(joinNowButton) {
                    top.linkTo(questionText.bottom, margin = 16.dp)
                    start.linkTo(questionText.start)
                    end.linkTo(questionText.end)
                },
            onCLick = { }
        )

        CoursesImage(modifier = Modifier
            .constrainAs(coursesImage){
                bottom.linkTo(horizontalGuideline1)
                end.linkTo(endGuidLine)

                start.linkTo(joinNowButton.end, margin = 8.dp)
                top.linkTo(joinNowButton.bottom, margin = 16.dp)

                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

        //4.MyCard
        MyCard(modifier = Modifier.constrainAs(card){
            top.linkTo(horizontalGuideline1, margin = (-16).dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)

            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        })

        //5.OurCourses
        TextOurCourses(modifier = Modifier.constrainAs(newCourses){
            top.linkTo(card.top, margin = 24.dp)
            start.linkTo(card.start, margin = 10.dp)
        })

        AndroidCourseImage(modifier = Modifier.constrainAs(androidCourse){
            top.linkTo(newCourses.bottom, margin = 20.dp)
            start.linkTo(newCourses.start)
        })

        JavaCourseImage(modifier = Modifier.constrainAs(javaCourse){
            top.linkTo(androidCourse.top)
            bottom.linkTo(androidCourse.bottom)
        })

        PythonCourseImage(modifier = Modifier.constrainAs(pythonCourse){
            top.linkTo(androidCourse.top)
            bottom.linkTo(androidCourse.bottom)
        })

        //chain the three items
        createHorizontalChain(androidCourse, javaCourse, pythonCourse,
            chainStyle = ChainStyle.SpreadInside)

        AndroidText(modifier = Modifier.constrainAs(androidText){
            top.linkTo(androidCourse.bottom, margin = 20.dp)
            start.linkTo(androidCourse.start)
            end.linkTo(androidCourse.end)
        })
        JavaText(modifier = Modifier.constrainAs(javaText){
            top.linkTo(javaCourse.bottom, margin = 20.dp)
            start.linkTo(javaCourse.start)
            end.linkTo(javaCourse.end)
        })
        PythonText(modifier = Modifier.constrainAs(pythonText){
            top.linkTo(pythonCourse.bottom, margin = 20.dp)
            start.linkTo(pythonCourse.start)
            end.linkTo(pythonCourse.end)
        })

        LatestLessionText(modifier = Modifier.constrainAs(latestLession){
            top.linkTo(androidText.bottom, margin = 20.dp)
            start.linkTo(startGuidline)
        })

        SeeAllText(modifier = Modifier.constrainAs(seeAll){
            top.linkTo(pythonText.bottom, margin = 20.dp)
            end.linkTo(endGuidLine)
        })

        LessionCard(modifier = Modifier.constrainAs(lessionCard){
            top.linkTo(latestLession.bottom, margin = 16.dp)
            start.linkTo(latestLession.start)
            end.linkTo(endGuidLine)

            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        })


    }

}