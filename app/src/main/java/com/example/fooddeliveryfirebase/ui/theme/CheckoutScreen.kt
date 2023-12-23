package com.example.fooddeliveryfirebase.ui.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    db: DbHelper,
) {


    val cont = LocalContext.current


    val listData = remember { mutableStateOf(emptyList<Product>()) }
    LaunchedEffect(Unit) {
        db.getBasketFromFirebase(listData)
    }

    val totalCost = remember { mutableDoubleStateOf(0.0) }
    LaunchedEffect(listData.value) {
        totalCost.doubleValue = listData.value.sumOf { it.cValue!! * it.price }
    }

    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val deliveryTime = remember { mutableStateOf(calculateDeliveryTime()) }


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
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Divider(
                    color = Color.White,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "Total Cost: $${"%.2f".format(totalCost.doubleValue)}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Delivery Address", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        textColor = Color.White,
                        placeholderColor = Color.White,
                        cursorColor = Color.White
                    ),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number", color = Color.White) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        textColor = Color.White,
                        placeholderColor = Color.White,
                        cursorColor = Color.White
                    ),
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Estimated Delivery Time: ${deliveryTime.value}",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        db.placeOrder(
                            address = address,
                            phoneNumber = phoneNumber,
                            date = deliveryTime.value,
                            price = totalCost.doubleValue,
                            basket = listData.value,
                            cont = cont,
                            navController = navController
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.White),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text("Place Order")
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateDeliveryTime(): String {
    val currentDateTime = LocalDateTime.now()
    val estimatedDeliveryTime = currentDateTime.plusHours(1)
    return DateTimeFormatter.ofPattern("yyyy:MM:dd:HH:mm").format(estimatedDeliveryTime)
}

