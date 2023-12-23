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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MenuScreen(db: DbHelper, navController: NavController) {
    val listData = remember { mutableStateOf(emptyList<Product>()) }

    LaunchedEffect(Unit) {
        db.getMenuFromFirebase(listData)
    }

    LaunchedEffect(Unit) {
        db.getBasketProductsFromFirebase(listData)
    }

    BottomBarForMenu(
        navController = navController,
        function = { MyMenu(listData = listData, db = db) })

}


@Composable
fun LazyColumnWithDish(listData: MutableState<List<Product>>, db: DbHelper) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(bottom = 55.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        itemsIndexed(listData.value) { _, menuItem ->
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
                                    menuItem.price,
                                    menuItem.description,
                                    -1
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = menuItem.cValue.toString(),
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                AddRemoveButtons(
                                    icon = Icons.Sharp.Add,
                                    db,
                                    menuItem.name,
                                    menuItem.price,
                                    menuItem.description,
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

@Composable
fun AddRemoveButtons(
    icon: ImageVector,
    db: DbHelper,
    name: String,
    price: Double?,
    description: String?,
    c: Int
) {
    IconButton(
        onClick = { db.addInBasket(name, price, description, c) },
        modifier = Modifier
            .size(50.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomBarForMenu(navController: NavController, function: @Composable () -> Unit) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .background(Color.Transparent)
                    .height(30.dp)
            ) {
                Column {

                    Divider(
                        color = Color.White,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

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
fun MyMenu(listData: MutableState<List<Product>>, db: DbHelper) {
    BackGroundImage()
    Column {
        Text(
            text = "MENU",
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