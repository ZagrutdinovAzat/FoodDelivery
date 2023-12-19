package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentOrders(db: DbHelper, navController: NavController) {
    val listData = remember { mutableStateOf(emptyList<Order>()) }

    LaunchedEffect(Unit)
    {
        db.getAllOrdersForUser(listData)
    }

    LazyColumn {
        items(listData.value) { order ->
            Card(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    navController.navigate("${Marshroutes.orderBasketRoute}/${order.id}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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

@Composable
fun OrderItem(order: Order) {
    Card(
        shape = RoundedCornerShape(8.dp),
        //backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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