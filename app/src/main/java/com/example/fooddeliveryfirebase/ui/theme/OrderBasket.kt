package com.example.fooddeliveryfirebase.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderBasketScreen(db: DbHelper, orderId: String, navController: NavController, userId: String) {
    val basketResult = remember { mutableStateOf(emptyList<Product>()) }

    LaunchedEffect(Unit) {
        db.getBasketForOrder(orderId, basketResult, userId)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Back", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = {
            LazyColumnWithDishForBasketOrders(listData = basketResult.value)
        }
    )
}


@Composable
fun LazyColumnWithDishForBasketOrders(listData: List<Product>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent).padding(top = 40.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        itemsIndexed(listData) { _, menuItem ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = menuItem.name,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = Color.White,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                            if (menuItem.cValue!! * menuItem.price != 0.0) {
                                Text(
                                    text = "Full price: $${"%.2f".format(menuItem.cValue!! * menuItem.price)}",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    ),
                                    color = Color.White,
                                    modifier = Modifier.padding(end = 8.dp),
                                )
                            }
                        }
                        Text(
                            text = menuItem.description,
                            style = TextStyle(
                                fontSize = 14.sp
                            ),
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            text = "$${menuItem.price}",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Right
                        )
                    }
                }
            }
        }
    }
}
