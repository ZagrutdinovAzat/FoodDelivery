package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun OrderBasketScreen(db: DbHelper, orderId: String) {
    val basketResult = remember { mutableStateOf(emptyList<Product>()) }

    LaunchedEffect(Unit) {
        db.getBasketForOrder(orderId, basketResult)
    }

    LazyColumn {
        items(basketResult.value) { product ->
            Text(
                text = "${product.name} - Count: ${product.cValue} - Price: ${product.price}"
            )
        }
    }
}