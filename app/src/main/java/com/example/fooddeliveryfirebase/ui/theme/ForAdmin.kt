package com.example.fooddeliveryfirebase.ui.theme


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.example.fooddeliveryfirebase.CustomTextField
import com.example.fooddeliveryfirebase.MainActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


@Composable
fun ForAdminScreen(imgHandler: MainActivity.Img, db: DbHelper) {
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
                db.addDishInFirebase(name, description, price, context)
            }, shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            ),
            modifier = Modifier.width(150.dp)
        ) {
            Text(text = "add in db", color = Color.Black)
        }

        Text(text = imgHandler.myImg.value)

    }
}



