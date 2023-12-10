package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BasketScreen(navController: NavController,db: DbHelper) {
    val listData = remember { mutableStateListOf<BasketItem>() }
//    val myBasketRef: DatabaseReference =
//        FirebaseDatabase.getInstance().getReference("Basket").child(db.cUser!!.uid)
//
//    myBasketRef.addChildEventListener(object : ChildEventListener {
//        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
//            // Получаем ключ и значение и делаем с ними что-то
//            val key = dataSnapshot.key
//            val value = dataSnapshot.value
//
//            listData.add(BasketItem(key.toString(), value.toString()))
//            // Делайте что-то с полученным ключом и значением
//        }
//
//        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
//            // Метод вызывается при изменении значения дочернего элемента
//
//            val key = dataSnapshot.key
//
//            val itemIndex = listData.indexOfFirst { it.key == key } // Найти индекс элемента с определенным ключом
//            if (itemIndex != -1) { // Если элемент найден
//                listData[itemIndex] = BasketItem(dataSnapshot.key.toString(), dataSnapshot.value.toString())
//            }
//        }
//
//        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
//            // Метод вызывается при удалении дочернего элемента
//            val key = dataSnapshot.key
//            val itemIndex = listData.indexOfFirst { it.key == key }
//            listData.removeAt(itemIndex)
//        }
//
//        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
//            // Метод вызывается при перемещении дочернего элемента
//        }
//
//        override fun onCancelled(databaseError: DatabaseError) {
//            // Обработка ошибок
//        }
//    })
    db.getBasketFromFirebase(listData)
    BottomBar(navController = navController, function = { MyBasket(listData = listData, db = db) })
    //LazyBasket(listData = listData, db = db)


    //Basket(listData = listData)
    //BasketScreen(db = db)
}


class BasketItem(val key: String?, var cValue: Int?, var price: Double? = 0.0)

@Composable
fun MyBasket(listData: List<BasketItem>, db: DbHelper) {
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
        LazyBasket(listData = listData, db = db)

    }
}

@Composable
fun LazyBasket(listData: List<BasketItem>, db: DbHelper) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(bottom = 30.dp),
        contentPadding = PaddingValues(16.dp),
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
                ) {
                    Column {
                        Text(
                            text = menuItem.key.toString(),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                        Text(
                            text = menuItem.cValue.toString(),
                            style = TextStyle(
                                fontSize = 14.sp
                            ),
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 8.dp)
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
                                AddRemoveButtons(icon = Icons.Sharp.Delete, db, menuItem.key.toString(), null, -1)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "0", /* Текущее количество товаров */
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                AddRemoveButtons(icon = Icons.Sharp.Add, db,menuItem.key.toString(),  null, 1)
                            }
                        }
                    }
                }
            }
        }
    }
}