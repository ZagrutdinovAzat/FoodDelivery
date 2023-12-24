package com.example.fooddeliveryfirebase.ui.theme


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fooddeliveryfirebase.CustomTextField
import com.example.fooddeliveryfirebase.MainActivity

@Composable
fun ForAdminScreen(imgHandler: MainActivity.Img, db: DbHelper, navController: NavController) {
    Column(modifier = Modifier.padding(10.dp)) {

        Text(
            text = "ADMIN PANEL",
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

        Button(
            onClick = { navController.navigate(Marshroutes.addDish) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(text = "Add new dish", color = Color.White)
        }

        Button(
            onClick = { navController.navigate(Marshroutes.deleteDish) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(text = "Delete dish", color = Color.White)
        }

        Button(
            onClick = { /*todo*/ navController.navigate(Marshroutes.ordersForAdmin) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(text = "Check current orders", color = Color.White)
        }
    }
}

@Composable
fun AddNewDish(imgHandler: MainActivity.Img, db: DbHelper) {
    var name by rememberSaveable {
        mutableStateOf("")
    }
    var description by rememberSaveable {
        mutableStateOf("")
    }

    var price by rememberSaveable {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {

        CustomTextField(value = name, onValueChange = { name = it }, label = "name of dish")

        CustomTextField(
            value = description,
            onValueChange = { description = it },
            label = "description"
        )

        CustomTextField(value = price, onValueChange = {
            price = if (it.toDoubleOrNull() != null) it
            else {
                makeToast(context = context, "You didn't enter a double")
                ""
            }
        }, label = "price")

        Button(
            onClick = {
                imgHandler.getImage()
            }, shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            ),
            modifier = Modifier.width(150.dp)
        ) {
            Text(text = "choose image", color = Color.Black)
        }


        Button(
            onClick = {
                db.addDishInMenu(name, description, price, context)
            }, shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            ),
            modifier = Modifier.width(150.dp)
        ) {
            Text(text = "add to the menu", color = Color.Black)
        }

        Text(text = imgHandler.myImg.value)
    }
}

@Composable
fun DeleteDish(db: DbHelper) {

    var name by rememberSaveable {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        CustomTextField(value = name, onValueChange = { name = it }, label = "name of dish")

        Button(
            onClick = {
                db.deleteDishInMenu(name, context = context)
            }, shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            ),
            modifier = Modifier.width(150.dp)
        ) {
            Text(text = "delete in the menu", color = Color.Black)
        }
    }
}


@Composable
fun CheckOrdersForAdmin(db: DbHelper, navController: NavController) {
    val listData = remember { mutableStateOf(emptyList<OrdersForAdmin>()) }
    LaunchedEffect(Unit)
    {
        db.getAllOrdersForAdmin(listData)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(top = 40.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(listData.value) { order ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate("${Marshroutes.orderBasketRoute}/${order.orderId}/${order.userId}")
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "User ID: ${order.userId}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Order ID: ${order.orderId}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Address: ${order.address}")
                    Text(text = "Phone Number: ${order.phoneNumber}")
                    Text(text = "Estimated Order Date: ${order.estimatedOrderDate}")
                    Text(text = "Order Date: ${order.orderDate}")
                    Text(text = "Total Price: ${order.price}")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { /*todo*/ },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            modifier = Modifier.width(150.dp)
                        ) {
                            Text(text = "accept order", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Button(
                            onClick = { /*todo*/ },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            modifier = Modifier.width(150.dp)
                        ) {
                            Text(text = "reject order", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}


class OrdersForAdmin(
    val userId: String,
    val orderId: String,
    val address: String,
    val estimatedOrderDate: String,
    val orderDate: String,
    val phoneNumber: String,
    val price: Double?
)