package com.example.fooddeliveryfirebase.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fooddeliveryfirebase.CustomTextField
import com.example.fooddeliveryfirebase.MainActivity
import com.example.fooddeliveryfirebase.R

@Composable
fun LoginScreen(
    navController: NavController,
    db: DbHelper,
    mainActivity: MainActivity
) {
    var login by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current


    backGroundImage()

    Image(
        painter = painterResource(id = R.drawable.loho),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .width(30.dp)
            .height(20.dp),
        alignment = Alignment.TopCenter
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "LOG IN", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.White)

        CustomTextField(value = login, onValueChange = { login = it }, label = "Login")

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )
        Button(
            onClick = {
                if (login == "" || password == "") {
                    makeToast(context = context, text = "Fill in all the fields")
                } else {
                    db.logIn(login, password, context, navController)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .width(150.dp)
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Text(text = "log in", color = Color.White)
        }
        Button(
            onClick = {
                navController.navigate(Marshroutes.route2)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .width(150.dp)
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Text(text = "registration", color = Color.White)
        }
    }
}

fun makeToast(context: Context, text: String) {
    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun backGroundImage() {
    Image(
        painter = painterResource(id = R.drawable.bg3),
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
        modifier = Modifier.fillMaxSize()
    )
}
