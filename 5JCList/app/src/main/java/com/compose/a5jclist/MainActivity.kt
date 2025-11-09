package com.compose.a5jclist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.a5jclist.ui.theme._5JCListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //ScrollingColumn()
            //MyLazyColumn()
            //MyLazyRow()
            //MyCustomItemStickyHeader()
            MyCard()
        }
    }
}

@Composable
fun ScrollingColumn(){
    Column (
        modifier = Modifier.verticalScroll(rememberScrollState())
    ){

        Image(
            painter = painterResource(R.drawable.pic1),
            contentDescription = "Pic 1",
            contentScale = ContentScale.FillBounds
        )

        Image(
            painter = painterResource(R.drawable.pic2),
            contentDescription = "Pic 2",
            contentScale = ContentScale.FillBounds
        )

        Image(
            painter = painterResource(R.drawable.pic3),
            contentDescription = "Pic 3",
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun MyLazyColumn(){
    val itemList = listOf<String>("Item 1", "Item 2", "item 3", "Nikhil 4", "item 5",
        "Item 6", "Item 7", "item 8", "Nikhil 9", "item 10","Item 11", "Item 12", "item 13", "Nikhil 14", "item 15")


    LazyColumn {
        items(itemList){
            item -> MyCustomItem(item)
        }
    }
}

@Composable
fun MyLazyRow(){
    val itemList = listOf<String>("Item 1", "Item 2", "item 3", "Nikhil 4", "item 5",
        "Item 6", "Item 7", "item 8", "Nikhil 9", "item 10","Item 11", "Item 12", "item 13", "Nikhil 14", "item 15")

    LazyRow (modifier = Modifier.padding(top = 50.dp)){
        items(itemList){
            item -> MyCustomItem(item)
        }
    }
}

@Composable
fun MyCustomItem(itemTitle: String) {
    val context = LocalContext.current  // Get the current context

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = {
                Toast.makeText(context, "Item clicked: $itemTitle", Toast.LENGTH_SHORT).show()
            })
    ) {
        Text(
            text = itemTitle,
            fontSize = 42.sp,
            modifier = Modifier.background(Color.Green)
                .pointerInput(Unit){
                    detectTapGestures(
                        onLongPress = {
                            Toast.makeText(context, "Item long clicked: $itemTitle", Toast.LENGTH_SHORT).show()
                        },
                        onTap = {
                            Toast.makeText(context, "on pressed: $itemTitle", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyCustomItemStickyHeader(){
    val items = listOf<String>("Item 1", "Item 2", "item 3", "Nikhil 4", "item 5",
        "Item 6", "Item 7", "item 8", "Nikhil 9", "item 10","Item 11", "Item 12", "item 13", "Nikhil 14", "item 15")

    LazyColumn (modifier = Modifier.padding(vertical = 50.dp)){
        stickyHeader {
            Text(text = "Sticky Header",
                fontSize = 42.sp,
                modifier = Modifier.background(Color.Blue))
        }

        item {
            Text(text = "Title for items",
                fontSize = 42.sp)
        }

        items(items){
            item->MyCustomItem(item)
        }
    }
}

@Composable
fun MyCard(){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(16.dp),
        colors = CardDefaults.cardColors(Color.Yellow),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Text(text = "This is card view",
            modifier = Modifier.padding(16.dp))
    }
}