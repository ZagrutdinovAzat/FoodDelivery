package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun MenuScreen() {
    val listData = remember { mutableStateListOf<Product>() }

    fun getMenuFromFirebase(listData: MutableList<Product>) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Menu")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val name = ds.child("name").getValue(String::class.java)
                    val description = ds.child("description").getValue(String::class.java)
                    val price = ds.child("price").getValue(Double::class.java)

                    if (name != null && description != null && price != null) {
                        listData.add(Product(name, description, price))
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок
            }
        })
    }

    LaunchedEffect(Unit) {
        getMenuFromFirebase(listData)
    }

//    LaunchedEffect(Unit)
//    {
//        fun qwr()
//        {
//            println("125456")
//        }
//    }
    backGroundImage()
    Column {
        Text(text = "MENU", fontSize = 25.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, textAlign = TextAlign.Center, modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Transparent
            ), color = Color.White)
        LazyMenu(listData = listData)
    }
}


@Composable
fun LazyMenu(listData: List<Product>)
{
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(16.dp) // добавим небольшой отступ вокруг элементов
    ) {
        itemsIndexed(listData) {_, menuItem ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp) // добавим пространства между элементами
            ) {
                Card(modifier = Modifier.border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))) {
                    Text(
                        text = menuItem.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = menuItem.description,
                        style = TextStyle(
                            fontSize = 14.sp
                        ),
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        text = "$${menuItem.price}",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = Color.Black,
                        textAlign = TextAlign.Right
                    )
                }
                Divider( // линия-разделитель между элементами
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}





