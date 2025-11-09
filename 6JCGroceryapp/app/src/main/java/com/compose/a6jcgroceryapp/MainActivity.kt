package com.compose.a6jcgroceryapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.a6jcgroceryapp.ui.theme.Item

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LazyColumnWithStickyHeader()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnWithStickyHeader(){

    // 1- Creating the Data Source: map with key-value pairs
    val groupedItems = mapOf(

        "Fruits" to listOf(
            Item("Apple", "A sweet red Fruit",R.drawable.apple),
            Item("Banana", "A long yellow Fruit",R.drawable.bananas),
            Item("Cherry","A small red Fruit",R.drawable.cherry),
            Item("Mango","A delicious Fruit",R.drawable.mango),
            Item("WaterMelon","A big and sweet Fruit", R.drawable.watermelon),
            Item("Grapes", "Tasty fruits", R.drawable.grapes)
        ),

        "Vegetables" to listOf(
            Item("Carrot", "A long orange vegetable",R.drawable.carrot),
            Item("Lettuce","A leafy green vegetable",R.drawable.lettuce),
            Item("Broccoli","A beautiful vegetable",R.drawable.brocoli),
            Item("Onion", "Amazing Vegetable", R.drawable.onion),
            Item("Potato","A yellow vegetable",R.drawable.potato),
            Item("Tomato","A red and sweet vegetable",R.drawable.tomato),
            Item("Pea", "Sweet Vegetable",R.drawable.pea)
        )
    )

    LazyColumn {
        groupedItems.forEach {
            (header, itemsList) ->
            stickyHeader {
                MyCustomHeader(title = header)
            }

            items(itemsList) { item ->
                MyCustomItem(item)
            }
        }

    }
}

@Composable
fun MyCustomHeader(title:String){
    Text(
        text = title,
        fontSize = 32.sp,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green)
            .padding(8.dp)

    )
}

@Composable
fun MyCustomItem(item: Item){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column (modifier = Modifier.padding(16.dp)){
            Image(painter = painterResource(id = item.image),
                contentDescription = "Item Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            // Adding Space between UI Elements
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = item.desc, fontSize = 18.sp)
        }

    }
}


