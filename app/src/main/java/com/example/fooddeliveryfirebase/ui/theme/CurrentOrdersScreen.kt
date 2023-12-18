package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


class Order(
    val id: String,
    val address: String,
    val basket: MutableList<Product>,
    val estimatedOrderDate: String,
    val orderDate: String,
    val phoneNumber: String,
    val price: Double?
)

@Composable
fun CurrentOrders(db: DbHelper) {
    val listData = remember { mutableStateOf(emptyList<Order>()) }

    LaunchedEffect(Unit)
    {
        db.getAllOrdersForUser(listData)
    }

    LazyColumn {
        items(listData.value) { order ->
            OrderItem(order = order)
        }
    }

}

@Composable
fun OrderItem(order: Order) {
    Column {
        Text(text = "Order ID: ${order.id}")
        Text(text = "Address: ${order.address}")
        Text(text = "Phone Number: ${order.phoneNumber}")
        Text(text = "Estimated Order Date: ${order.estimatedOrderDate}")
        Text(text = "Order Date: ${order.orderDate}")
        Text(text = "Total Price: ${order.price}")

        // Отобразить содержимое корзины товаров
        Text(text = "Basket:")
        order.basket.forEach { product ->
            Text(text = "${product.name} - ${product.price} - Count: ${product.cValue}")
        }
    }
}