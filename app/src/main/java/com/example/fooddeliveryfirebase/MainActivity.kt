package com.example.fooddeliveryfirebase

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddeliveryfirebase.ui.theme.DbHelper
import com.example.fooddeliveryfirebase.ui.theme.FoodDeliveryFireBaseTheme
import com.example.fooddeliveryfirebase.ui.theme.Marshroutes
import com.example.fooddeliveryfirebase.ui.theme.MyNavigation

class MainActivity : ComponentActivity() {
    private val db: DbHelper = DbHelper()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imgHandler = Img() // поиск изображения для menuscreen

        db.checkUser(this)

        setContent {
            FoodDeliveryFireBaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val start: String = if (db.userStatusLiveData.value == 1) {
                        Marshroutes.connectingRoute
                    } else {
                        Marshroutes.loginRoute
                    }
                    MyNavigation.MinimalNavigation(db, start, imgHandler)
                }
            }
        }
    }

    inner class Img {
        val myImg = mutableStateOf("")

        private val getContent: ActivityResultLauncher<String> =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    myImg.value = uri.path.toString()
                } else {
                    myImg.value = ""
                }
            }

        fun getImage() {
            getContent.launch("image/*")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    TextField(
        value = value,
        onValueChange = onValueChange,
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
        singleLine = true,
        label = { Text(label) },
    )
}

