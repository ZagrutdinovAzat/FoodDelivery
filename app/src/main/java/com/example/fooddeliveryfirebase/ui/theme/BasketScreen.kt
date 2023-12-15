package com.example.fooddeliveryfirebase.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BasketScreen(navController: NavController, db: DbHelper) {
    val listData = remember { mutableStateOf(emptyList<Product>()) }
    LaunchedEffect(Unit) {
        db.getBasketFromFirebase(listData)
    }

    val cost = remember { mutableStateOf(0.0) }
    LaunchedEffect(listData.value) {
        cost.value = listData.value.sumByDouble { it.cValue!! * it.price }
    }

    BottomBarForCart(
        navController = navController,
        function = { MyBasket(listData = listData.value, db = db) },
        cost = cost.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomBarForCart(
    navController: NavController,
    function: @Composable () -> Unit,
    cost: Double
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .background(Color.Transparent)
                    .height(60.dp)
            ) {
                Column {
                    Button(
                        onClick = { navController.navigate(Marshroutes.orderRoute) },
                        modifier = Modifier
                            .height(30.dp)
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        Text("place an order for $${cost}", fontSize = 12.sp)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        IconButton(onClick = { navController.navigate(Marshroutes.menuRoute) }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.Black)
                        }
                        IconButton(onClick = { navController.navigate(Marshroutes.basketRoute) }) {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = "Cart",
                                tint = Color.Black
                            )
                        }
                        IconButton(onClick = { /* Действие при нажатии кнопки "Profile" */ }) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "Profile",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
    )
    {
        function()
    }
}


@Composable
fun MyBasket(listData: List<Product>, db: DbHelper) {
    BackGroundImage()
    Column {
        Text(
            text = "CART",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.Transparent
                ),
            color = Color.White
        )
        LazyBasket(listData = listData, db = db)
    }
}

@Composable
fun LazyBasket(listData: List<Product>, db: DbHelper) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(bottom = 55.dp),
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
                        Text(
                            text = menuItem.name,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp),
                        )

                        Text(
                            text = menuItem.description.toString(),
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

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AddRemoveButtons(
                                    icon = Icons.Sharp.Delete,
                                    db,
                                    menuItem.name,
                                    null,
                                    null,
                                    -1
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = menuItem.cValue.toString(), /* Текущее количество товаров */
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                AddRemoveButtons(
                                    icon = Icons.Sharp.Add,
                                    db,
                                    menuItem.name,
                                    null,
                                    null,
                                    1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}