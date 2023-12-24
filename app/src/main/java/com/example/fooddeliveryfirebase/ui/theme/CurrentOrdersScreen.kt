package com.example.fooddeliveryfirebase.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


class Order(
    val id: String,
    val address: String,
    val estimatedOrderDate: String,
    val orderDate: String,
    val phoneNumber: String,
    val price: Double?
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentOrders(db: DbHelper, navController: NavController) {
    val listData = remember { mutableStateOf(emptyList<Order>()) }

    LaunchedEffect(Unit) {
        db.getAllOrdersForUser(listData)
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
                                navController.navigate("${Marshroutes.orderBasketRoute}/${order.id}/${db.cUser!!.uid}")
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Order ID: ${order.id}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Address: ${order.address}")
                            Text(text = "Phone Number: ${order.phoneNumber}")
                            Text(text = "Estimated Order Date: ${order.estimatedOrderDate}")
                            Text(text = "Order Date: ${order.orderDate}")
                            Text(text = "Total Price: ${order.price}")
                        }
                    }
                }
            }
        }
    )
}


//@Composable
//fun OrderItem(order: Order) {
//    Card(
//        shape = RoundedCornerShape(8.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//        ) {
//            Text(
//                text = "Order ID: ${order.id}",
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(text = "Address: ${order.address}")
//            Text(text = "Phone Number: ${order.phoneNumber}")
//            Text(text = "Estimated Order Date: ${order.estimatedOrderDate}")
//            Text(text = "Order Date: ${order.orderDate}")
//            Text(text = "Total Price: ${order.price}")
//        }
//    }
//}