package com.example.fooddeliveryfirebase.ui.theme


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
            onClick = { /*todo*/ },
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