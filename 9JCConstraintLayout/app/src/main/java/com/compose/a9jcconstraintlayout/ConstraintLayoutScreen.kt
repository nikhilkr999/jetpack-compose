package com.compose.a9jcconstraintlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ConstraintLayoutScreen(){
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ){
        // 1- Define the references for the Constrains
        // createRefs(): generates unique references for the composables,
        // which are then used with 'constrainAs' modifier to specify the constraints
        val (box1, box2, text) = createRefs()

        // GuideLines: Helps you create consistent & adaptive layouts by positioning elements relative to a percentage or a specific distance
        //             from the start or end of the parent
        val guideLine1 = createGuidelineFromStart(0.05f)

        // Barriers: Used to create constraints relative to the edges of multiple elements.
        val barrier1 = createEndBarrier(box1,box2)

        // 2- Constrain the First Box
        Box(modifier = Modifier
            .size(100.dp).background(Color.Red)
            .constrainAs(box1) {
                // constrainAs(): allows you to define constraints for a composable relative to other composables or the parent layout
                // Define here the constraints
                top.linkTo(parent.top, margin = 100.dp)
                start.linkTo(guideLine1)    } )

        // 2nd Box
        Box(modifier = Modifier
            .size(80.dp)
            .background(Color.Green)
            .constrainAs(box2) {
                // Define here the constraints
                top.linkTo(box1.bottom, margin = 20.dp)
                start.linkTo(guideLine1)    }   )

        // 3rd Composable
        Text(text = "Hello Constraint Layout",
            modifier = Modifier.constrainAs(text){
                top.linkTo(box2.bottom, margin = 16.dp)
                start.linkTo(barrier1, margin = 16.dp)  }     )
        // Chains
        createVerticalChain( box1, box2,chainStyle = ChainStyle.Spread )

    }

}