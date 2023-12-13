package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fooddeliveryfirebase.CustomTextField

@Composable
fun RegistrationScreen(navController: NavController, db: DbHelper) {
    var login by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }

    BackGroundImage()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Text(
            text = "REGISTRATION",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        CustomTextField(value = login, onValueChange = { login = it }, label = "Login")
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )
        RegisterButton(login, password, navController, db)
    }
}

@Composable
fun RegisterButton(
    login: String,
    password: String,
    navController: NavController,
    db: DbHelper
) {
    val context = LocalContext.current

    Button(
        onClick = {
            if (login == "" || password == "") {
                makeToast(context, "Fill in all the fields")
            } else {
                db.registration(login, password, context, navController)
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .width(150.dp)
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Text(text = "register", color = Color.White)
    }
}