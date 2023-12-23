package com.example.fooddeliveryfirebase.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    val cost = remember { mutableDoubleStateOf(0.0) }
    LaunchedEffect(listData.value) {
        cost.doubleValue = listData.value.sumOf { it.cValue!! * it.price }
    }

    BottomBarForCart(
        navController = navController,
        function = { MyBasket(listData = listData, db = db) },
        cost = cost.doubleValue
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
                            .background(Color.Transparent),
                        colors = ButtonDefaults.buttonColors( contentColor = Color.White),
                        border = BorderStroke(1.dp, Color.White),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text("place an order for $${"%.2f".format(cost)}", fontSize = 12.sp)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        IconButton(onClick = { navController.navigate(Marshroutes.menuRoute) }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                        IconButton(onClick = { navController.navigate(Marshroutes.basketRoute) }) {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = "Cart",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { navController.navigate(Marshroutes.profileRoute) }) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "Profile",
                                tint = Color.White
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
fun MyBasket(listData: MutableState<List<Product>>, db: DbHelper) {
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
        LazyColumnWithDish(listData = listData, db = db)
    }
}