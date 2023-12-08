package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.google.firebase.database.ktx.values
import com.google.firebase.database.values

@Composable
fun MenuScreen(db: DbHelper) {
    val listData = remember { mutableStateListOf<Product>() }

    LaunchedEffect(Unit) {
        db.getMenuFromFirebase(listData)
    }

    BackGroundImage()
    Column {
        Text(
            text = "MENU",
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
        LazyMenu(listData = listData, db = db)
    }
}


@Composable
fun LazyMenu(listData: List<Product>, db: DbHelper) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(listData) { _, menuItem ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {/* menuItem.count.intValue++*/
//                            val myBasket: DatabaseReference =
//                                FirebaseDatabase.getInstance().getReference("Basket")
//                           //myBasket.child(db.cUser!!.uid).child(menuItem.name).setValue(++1)
//                            myBasket.child(db.cUser!!.uid).child(menuItem.name)
//                                .addListenerForSingleValueEvent(object : ValueEventListener {
//                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                        val currentValue = dataSnapshot.getValue(Int::class.java) ?: 0
//                                        val newValue = currentValue + 1
//                                        // Теперь обновляем значение в базе данных
//                                        myBasket.child(db.cUser!!.uid).child(menuItem.name).setValue(newValue)
//                                    }
//
//                                    override fun onCancelled(databaseError: DatabaseError) {
//                                        // Обработка ошибок
//                                    }
//                                })
                        }
                ) {
                    Column {

                        Text(
                            text = menuItem.name,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp),
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
                            color = Color.White,
                            textAlign = TextAlign.Right
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AddRemoveButtons(icon = Icons.Sharp.Delete, db, menuItem, -1)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "0", /* Текущее количество товаров */
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                AddRemoveButtons(icon = Icons.Sharp.Add, db, menuItem, 1)
                            }
                        }
                    }
//                    Divider( // линия-разделитель между элементами
//                        color = Color.LightGray,
//                        thickness = 1.dp,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
                }
            }
        }
    }
}

@Composable
fun AddRemoveButtons(icon: ImageVector, db: DbHelper, menuItem: Product, c: Int) {
    IconButton(
        onClick = { db.addInBasket(menuItem = menuItem, c) },
        modifier = Modifier
            .size(50.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = Color.White
        )
    }
}







