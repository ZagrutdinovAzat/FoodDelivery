package com.example.fooddeliveryfirebase.ui.theme

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DbHelper {

    private var mAuth: FirebaseAuth = Firebase.auth
    var userRole: Int? = 0

    val userStatusLiveData = MutableLiveData<Int>() // пользователь авторизован/нет

    var cUser: FirebaseUser? = mAuth.currentUser // пользователь который сейчас авторизован

    private val myMenu: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Menu") // подключение к меню

    private val myBasket: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Basket") // подключение к корзине

    private val myOrders: DatabaseReference = FirebaseDatabase.getInstance().getReference("Orders")


    private val completedOrderRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("CompletedOrders")

    val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")


    fun checkUser(context: Context) {
        if (cUser != null) {
            makeToast(context = context, "User not null")
            userStatusLiveData.value = 1
            addInUser()
        } else {
            makeToast(context = context, "User null")
            userStatusLiveData.value = 0
        }
    }

    private fun addInUser() {
        userRef.child(cUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    userRole = dataSnapshot.getValue(Int::class.java)
                } else {
                    userRef.child(cUser!!.uid).setValue(0)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки запроса
            }
        })
    }


    fun logIn(
        login: String,
        password: String,
        context: Context,
        navController: NavController,
        callback: (Boolean) -> Unit
    ) {
        mAuth.signInWithEmailAndPassword(login, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (mAuth.currentUser!!.isEmailVerified) {
                        cUser = mAuth.currentUser
                        makeToast(context, "Authentication successful.")

                        addInUser()

                        navController.navigate(Marshroutes.connectingRoute)
                        callback(true)
                    } else {
                        makeToast(context, "Confirm your email.")
                        callback(false)
                    }
                } else {
                    makeToast(context = context, "Authentication failed.")
                    callback(false)
                }
            }
    }


    fun logOut(navController: NavController) {
        mAuth.signOut()
        navController.navigate(
            route = Marshroutes.loginRoute, navOptions =
            NavOptions.Builder().setPopUpTo(Marshroutes.connectingRoute, true).build()
        )

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

    fun addDishInMenu(name: String, description: String, price: String, context: Context) {
        if (name == "" || description == "" || price.toDoubleOrNull() == null) {
            makeToast(context = context, "Fill all fields")
        } else {
            val dish = Product(name = name, description = description, price = price.toDouble())
            myMenu.child(name).setValue(dish)
        }
    }

    fun deleteDishInMenu(dishId: String, context: Context) {
        val menuRef = FirebaseDatabase.getInstance().getReference("Menu")
        val dishRef = menuRef.child(dishId)

        dishRef.removeValue()
            .addOnSuccessListener {
                makeToast(context, "The deletion was successful")
            }
            .addOnFailureListener { error ->
                println("Error deleting a record: $error")
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
                "basket" to serializedBasket,
                "status" to 0
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

    fun addInCompletedOrders(
        userId: String,
        orderId: String,
        listData: MutableState<List<OrdersForAdmin>>
    ) {
        val database = Firebase.database
        val ordersRef = myOrders.child(userId).child(orderId)
        val completedOrdersRef =
            database.getReference("CompletedOrders").child(userId).child(orderId)

        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val address = dataSnapshot.child("address").getValue(String::class.java)
                val estimatedOrderDate =
                    dataSnapshot.child("estimated order date").getValue(String::class.java)
                val orderDate = dataSnapshot.child("order date").getValue(String::class.java)
                val phoneNumber = dataSnapshot.child("phoneNumber").getValue(String::class.java)
                val price = dataSnapshot.child("price").getValue(Double::class.java)
                val status = dataSnapshot.child("status").getValue(Double::class.java)

                val basket: MutableList<Product> = mutableListOf()
                val basketSnapshot = dataSnapshot.child("basket")
                basketSnapshot.children.forEach { dishSnapshot ->
                    val dishId = dishSnapshot.key
                    val cValue = dishSnapshot.child("cValue").getValue(Double::class.java)!!.toInt()
                    val description = dishSnapshot.child("description").getValue(String::class.java)
                    val dishPrice = dishSnapshot.child("price").getValue(Double::class.java)!!

                    val dish = Product(
                        name = dishId.toString(),
                        cValue = cValue,
                        description = description.toString(),
                        price = dishPrice
                    )
                    basket.add(dish)
                }

                val orderDateTime =
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd:HH:mm"))

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
                    "order date" to orderDate,
                    "estimated order date" to estimatedOrderDate,
                    "delivery date" to orderDateTime,
                    "price" to price,
                    "basket" to serializedBasket,
                    "status" to status
                )

                completedOrdersRef.setValue(orderData).addOnSuccessListener {
                    val ref = myOrders.child(userId).child(orderId)

                    ref.removeValue()
                        .addOnSuccessListener {

                            // shit
                            getAllOrdersForAdmin(listData)

                            //

                        }
                        .addOnFailureListener { error ->
                            println("Ошибка при удалении заказа: $error")
                        }
                }.addOnFailureListener {
                    // Обработка ошибки, если таковая произошла
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки, если таковая произошла
                Log.w("Firebase", "Ошибка при чтении данных", databaseError.toException())
            }
        })
    }

    fun getCompletedOrders(listData: MutableState<List<CompletedOrder>>) {
        val completedOrdersLink = completedOrderRef.child(cUser!!.uid)

        completedOrdersLink.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val completedOrdersList: MutableList<CompletedOrder> = mutableListOf()
                for (orderSnapshot in dataSnapshot.children) {
                    val orderId = orderSnapshot.key
                    val address = orderSnapshot.child("address").getValue(String::class.java)
                    val estimatedOrderDate =
                        orderSnapshot.child("estimated order date").getValue(String::class.java)
                    val orderDate = orderSnapshot.child("order date").getValue(String::class.java)
                    val deliveryDate =
                        orderSnapshot.child("delivery date").getValue(String::class.java)
                    val phoneNumber =
                        orderSnapshot.child("phoneNumber").getValue(String::class.java)
                    val price = orderSnapshot.child("price").getValue(Double::class.java)
                    val status = orderSnapshot.child("status").getValue(Double::class.java)

                    val order = CompletedOrder(
                        id = orderId.toString(),
                        address = address!!,
                        estimatedOrderDate = estimatedOrderDate!!,
                        orderDate = orderDate!!,
                        deliveryDate = deliveryDate!!,
                        phoneNumber = phoneNumber!!,
                        price = price,
                        status = status!!,
                    )
                    completedOrdersList.add(order)
                }

                listData.value = completedOrdersList

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки, если она произошла
                Log.w("Firebase", "Ошибка при чтении данных", databaseError.toException())
            }
        })
    }


    fun getAllOrdersForAdmin(listData: MutableState<List<OrdersForAdmin>>) {
        myOrders.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val ordersList = mutableListOf<OrdersForAdmin>()
                for (userSnapshot in dataSnapshot.children) {
                    val userId = userSnapshot.key

                    userSnapshot.children.forEach { orderSnapshot ->
                        val orderId = orderSnapshot.key

                        val address = orderSnapshot.child("address").getValue(String::class.java)
                        //val basket = orderSnapshot.child("basket").getValue()
                        val estimatedOrderDate =
                            orderSnapshot.child("estimated order date").getValue(String::class.java)
                        val orderDate =
                            orderSnapshot.child("order date").getValue(String::class.java)
                        val phoneNumber =
                            orderSnapshot.child("phoneNumber").getValue(String::class.java)
                        val price = orderSnapshot.child("price").getValue(Double::class.java)

                        val order = OrdersForAdmin(
                            userId = userId!!,
                            orderId = orderId!!,
                            address = address!!,
                            estimatedOrderDate = estimatedOrderDate!!,
                            orderDate = orderDate!!,
                            phoneNumber = phoneNumber!!,
                            price = price
                        )

                        ordersList.add(order)
                    }
                }
                listData.value = ordersList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Firebase", "Ошибка при чтении данных", databaseError.toException())
            }
        })
    }

    fun updateStatusOrderAdmin(userId: String, orderId: String, status: Int) {

        val ordersRef = myOrders.child(userId).child(orderId)

        ordersRef.child("status").setValue(status)
            .addOnSuccessListener {
                println("Статус успешно обновлен в базе данных")
            }
            .addOnFailureListener { error ->
                println("Ошибка при обновлении статуса: $error")
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
                    val status = orderSnapshot.child("status").getValue(Double::class.java)

                    if (orderId != null && address != null && estimatedOrderDate != null && orderDate != null && phoneNumber != null && totalPrice != null) {
                        val order = Order(
                            id = orderId,
                            address = address,
                            estimatedOrderDate = estimatedOrderDate,
                            orderDate = orderDate,
                            phoneNumber = phoneNumber,
                            price = totalPrice,
                            status = status
                        )
                        ordersList.add(order)
                    }
                }
                listData.value = ordersList
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    fun getBasketForOrder(orderId: String, listData: MutableState<List<Product>>, userId: String) {
        val orderRef = myOrders.child(userId).child(orderId).child("basket")

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
            }
        })
    }
}
