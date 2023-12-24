package com.example.fooddeliveryfirebase.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fooddeliveryfirebase.CustomTextField
import com.example.fooddeliveryfirebase.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    db: DbHelper,
) {
    var login by rememberSaveable {
        mutableStateOf("")
    }
//    var password by rememberSaveable {
//        mutableStateOf("")
//    }

    var password by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current


    BackGroundImage()

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

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            placeholder = { Text("Password") },
            modifier = Modifier.padding(16.dp),
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray,
                containerColor = Color.LightGray,
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )

//        CustomTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = "Password"
//        )
        Button(
            onClick = {
                if (login == "" || password.text == "") {
                    makeToast(context = context, text = "Fill in all the fields")
                } else {
                    db.logIn(login, password.text, context, navController) { success ->
                        if (success) {
                            login = ""
                            //password.text = ""
                        }
                    }
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
                login = ""
                //password = ""
                navController.navigate(Marshroutes.registrationRoute)
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
fun BackGroundImage() {
    Image(
        painter = painterResource(id = R.drawable.bg3),
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
        modifier = Modifier.fillMaxSize()
    )
}
