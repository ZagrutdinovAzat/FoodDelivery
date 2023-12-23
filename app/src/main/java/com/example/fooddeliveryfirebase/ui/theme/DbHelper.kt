package com.example.fooddeliveryfirebase.ui.theme

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DbHelper {

    private var mAuth: FirebaseAuth = Firebase.auth

    val userStatusLiveData = MutableLiveData<Int>() // пользователь авторизован/нет

    var cUser: FirebaseUser? = mAuth.currentUser // пользователь который сейчас авторизован

    private val myMenu: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Menu") // подключение к меню

    private val myBasket: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Basket") // подключение к корзине

    private val myOrders: DatabaseReference = FirebaseDatabase.getInstance().getReference("Orders")


    fun checkUser(context: Context) {
        if (cUser != null) {
            makeToast(context = context, "User not null")
            userStatusLiveData.value = 1
        } else {
            makeToast(context = context, "User null")
            userStatusLiveData.value = 0
        }
    }

    fun logIn(login: String, password: String, context: Context, navController: NavController) {
        mAuth.signInWithEmailAndPassword(login, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (mAuth.currentUser!!.isEmailVerified) {
                        cUser = mAuth.currentUser
                        makeToast(context, "Authentication successful.")
                        navController.navigate(Marshroutes.connectingRoute)
                    } else {
                        makeToast(context, "Confirm your email.")
                    }
                } else {
                    makeToast(context = context, "Authentication failed.")
                }
            }
    }


    fun logOut(navController: NavController) {
        mAuth.signOut()
        navController.navigate(Marshroutes.loginRoute)
    }

    fun getMenuFromFirebase(listData: MutableState<List<Product>>) {
        myMenu.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val products = mutableListOf<Product>()
                for (ds in dataSnapshot.children) {
                    val name = ds.child("name").getValue(String::class.java)
                    val description = ds.child("description").getValue(String::class.java)
                    val price = ds.child("price").getValue(Double::class.java)

                    if (name != null && description != null && price != null) {
                        val product = Product(name = name, description = description, price = price)
                        products.add(product)
                    }
                }
                listData.value = products
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок
            }
        })
    }


    fun registration(
        login: String,
        password: String,
        context: Context,
        navController: NavController
    ) {
        mAuth.createUserWithEmailAndPassword(login, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (task.isSuccessful) {
                                    makeToast(
                                        context = context,
                                        "Registration is successful, check your email for confirmation"
                                    )
                                    navController.navigate(Marshroutes.loginRoute)
                                } else {
                                    makeToast(
                                        context = context,
                                        "Registration failed: " + task.exception?.message
                                    )
                                }
                            }
                        }
                } else {
                    makeToast(context = context, "Registration failed: " + task.exception?.message)
                }
            }
    }

    fun addDishInFirebase(name: String, description: String, price: String, context: Context) {
        if (name == "" || description == "" || price.toDoubleOrNull() == null) {
            makeToast(context = context, "Fill all fields")
        } else {
            val dish = Product(name = name, description = description, price = price.toDouble())
            myMenu.child(name).setValue(dish)
        }
    }

    fun addInBasket(name: String, price: Double?, description: String?, c: Int) {
        val basketRef = myBasket.child(cUser!!.uid).child(name)
        basketRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentCount = dataSnapshot.child("count").getValue(Int::class.java) ?: 0

                val newCount = currentCount + c
                if (newCount <= 0) {
                    if (newCount == 0) {
                        basketRef.child("count").setValue(newCount)
                    }
                    basketRef.removeValue()
                } else {
                    basketRef.child("count").setValue(newCount)
                    if (price != null)
                        basketRef.child("price").setValue(price)
                    if (description != null) {
                        basketRef.child("description").setValue(description)
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок
            }
        })
    }

    fun getBasketProductsFromFirebase(listData: MutableState<List<Product>>) {
        myBasket.child(cUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val updatedProducts = mutableListOf<Product>()
                for (product in listData.value) {
                    dataSnapshot.children.find { it.key == product.name }?.let { basketItem ->
                        val count = basketItem.child("count").getValue(Int::class.java) ?: 0
                        val updatedProduct = product.copy(cValue = count)
                        updatedProducts.add(updatedProduct)
                    } ?: updatedProducts.add(product)
                }

                listData.value = updatedProducts
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    fun getBasketFromFirebase(listData: MutableState<List<Product>>) {
        myBasket.child(cUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = mutableListOf<Product>()
                dataSnapshot.children.forEach { dishSnapshot ->
                    val idDish = dishSnapshot.key ?: ""
                    val count = dishSnapshot.child("count").getValue(Int::class.java) ?: 0
                    val price = dishSnapshot.child("price").getValue(Double::class.java) ?: 0.0
                    val description =
                        dishSnapshot.child("description").getValue(String()::class.java) ?: ""
                    items.add(
                        Product(
                            name = idDish,
                            cValue = count,
                            price = price,
                            description = description
                        )
                    )
                }
                listData.value = items
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun placeOrder(
        address: String,
        phoneNumber: String,
        date: String,
        price: Double,
        basket: List<Product>,
        cont: Context,
        navController: NavController
    ) {

        var v = 0

        if (address == "" || phoneNumber == "" || date == "") {
            v = 1
        }

        if (basket.isEmpty()) {
            v = 2
        }

        if (v == 0) {
            val userRef = myOrders.child(cUser!!.uid)
            val orderRef = userRef.push()

            val serializedBasket = basket.associate { product ->
                product.name to mapOf(
                    "price" to product.price,
                    "cValue" to product.cValue,
                    "description" to product.description
                )
            }

            val orderData = hashMapOf(
                "address" to address,
                "phoneNumber" to phoneNumber,
                "order date" to DateTimeFormatter.ofPattern("yyyy:MM:dd:HH:mm")
                    .format(LocalDateTime.now()),
                "estimated order date" to date,
                "price" to price,
                "basket" to serializedBasket
            )

            orderRef.setValue(orderData).addOnSuccessListener {
                // Запись прошла успешно
                myBasket.child(cUser!!.uid).removeValue()
            }
                .addOnFailureListener {
                    v = 3
                }
        }

        when (v) {
            0 -> {
                makeToast(context = cont, text = "The order has been successfully created")
                navController.navigate(Marshroutes.menuRoute)
            }

            1 -> makeToast(context = cont, text = "Fill in all the fields")
            2 -> makeToast(context = cont, text = "Cart is empty")
            3 -> makeToast(context = cont, text = "Error when creating an order")
        }
    }


    fun getAllOrdersForUser(listData: MutableState<List<Order>>) {
        val userOrdersRef = myOrders.child(cUser!!.uid)


        userOrdersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val ordersList = mutableListOf<Order>()
                for (orderSnapshot in dataSnapshot.children) {
                    val orderId = orderSnapshot.key
                    val address = orderSnapshot.child("address").getValue(String::class.java)
                    val estimatedOrderDate =
                        orderSnapshot.child("estimated order date").getValue(String::class.java)
                    val orderDate = orderSnapshot.child("order date").getValue(String::class.java)
                    val phoneNumber =
                        orderSnapshot.child("phoneNumber").getValue(String::class.java)
                    val totalPrice = orderSnapshot.child("price").getValue(Double::class.java)

                    if (orderId != null && address != null && estimatedOrderDate != null && orderDate != null && phoneNumber != null && totalPrice != null) {
                        val order = Order(
                            id = orderId,
                            address = address,
                            estimatedOrderDate = estimatedOrderDate,
                            orderDate = orderDate,
                            phoneNumber = phoneNumber,
                            price = totalPrice
                        )
                        ordersList.add(order)
                    }
                }
                listData.value = ordersList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок, если не удалось получить данные
            }
        })
    }

    fun getBasketForOrder(orderId: String, listData: MutableState<List<Product>>) {
        val orderRef = myOrders.child(cUser!!.uid).child(orderId).child("basket")

        orderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val basketList = mutableListOf<Product>()
                for (itemSnapshot in dataSnapshot.children) {
                    val dishName = itemSnapshot.key
                    val count = itemSnapshot.child("cValue").getValue(Int::class.java)
                    val price = itemSnapshot.child("price").getValue(Double::class.java)
                    val description = itemSnapshot.child("description").getValue(String::class.java)
                    if (dishName != null && count != null && price != null) {
                        val product = Product(
                            name = dishName.toString(),
                            cValue = count,
                            price = price,
                            description = description!!
                        )
                        basketList.add(product)
                    }
                }
                listData.value = basketList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок, если не удалось получить данные
            }
        })
    }
}
