package com.example.fooddeliveryfirebase

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fooddeliveryfirebase.ui.theme.FoodDeliveryFireBaseTheme
import com.example.fooddeliveryfirebase.ui.theme.Marshroutes
import com.example.fooddeliveryfirebase.ui.theme.MyNavigation
import com.example.fooddeliveryfirebase.ui.theme.makeToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

 import androidx.activity.compose.setContent
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.lazy.LazyColumn
 import androidx.compose.foundation.lazy.items
 import androidx.compose.material3.Text
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.toMutableStateList
 import androidx.compose.ui.text.font.FontWeight
 import androidx.compose.ui.unit.dp
 import androidx.compose.ui.unit.sp
import com.example.fooddeliveryfirebase.ui.theme.DbHelper

class MainActivity : ComponentActivity() {
//    private var mAuth: FirebaseAuth = Firebase.auth
//
//    private val userStatusLiveData = MutableLiveData<Int>() // пользователь авторизован/нет
//
//    private val cUser: FirebaseUser? = mAuth.currentUser // пользователь который сейчас авторизован
//
//    val mDatabase: DatabaseReference =
//        FirebaseDatabase.getInstance().getReference("Menu") // подключение к меню

    private val db: DbHelper = DbHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imgHandler = Img(contentResolver) // поиск изображения для menuscreen

        if (db.cUser != null) {
            makeToast(this, "User not null")
            db.userStatusLiveData.value = 1
        } else {
            makeToast(this, "User null")
            db.userStatusLiveData.value = 0
        }

        setContent {


            FoodDeliveryFireBaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val start: String = if (db.userStatusLiveData.value == 1) {
                        Marshroutes.route3
                    } else {
                        Marshroutes.route1
                    }
                    MyNavigation.MinimalNavigation(db, this@MainActivity, start, imgHandler)
                }
            }
        }
    }

    inner class Img(private val contentResolver: ContentResolver) {
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
            //println(myImg.value)
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

