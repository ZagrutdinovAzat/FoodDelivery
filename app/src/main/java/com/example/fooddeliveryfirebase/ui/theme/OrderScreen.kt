package com.example.fooddeliveryfirebase.ui.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    db: DbHelper,
) {
    val listData = remember { mutableStateOf(emptyList<Product>()) }
    LaunchedEffect(Unit) {
        db.getBasketFromFirebase(listData)
    }

    val totalCost = remember { mutableStateOf(0.0) }
    LaunchedEffect(listData.value) {
        totalCost.value = listData.value.sumByDouble { it.cValue!! * it.price }
    }

    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val deliveryTime =
        calculateDeliveryTime() // Предположим, у вас есть функция для расчета времени доставки

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                listData.value.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    ) {
                        // отображение информации о товаре
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Total Cost: $${"%.2f".format(totalCost.value)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Estimated Delivery Time: $deliveryTime") // Отображение оценочного времени доставки
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        placeOrder(
                            listData.value,
                            db,
                            navController,
                            address,
                            phoneNumber
                        )
                    }, // Добавление адреса и номера телефона
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Place Order")
                }
            }
        }
    )
}

fun placeOrder(
    listData: List<Product>,
    db: DbHelper,
    navController: NavController,
    address: String,
    phoneNumber: String
) {
    // Логика размещения заказа, отправка данных в базу данных, обработка заказа и переход к следующему экрану
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateDeliveryTime(): String {
    // Здесь вы можете вставить свою логику для расчета оценочного времени доставки
    // Например, можно использовать текущее время и добавить к нему оценочное время доставки
    val currentTime = LocalTime.now()
    val estimatedDeliveryTime =
        currentTime.plusHours(1) // Например, предположим, что доставка займет 1 час

    // Форматирование оценочного времени доставки в строку
    val formattedDeliveryTime = DateTimeFormatter.ofPattern("HH:mm").format(estimatedDeliveryTime)

    return formattedDeliveryTime // Возвращаем оценочное время доставки в виде строки
}

