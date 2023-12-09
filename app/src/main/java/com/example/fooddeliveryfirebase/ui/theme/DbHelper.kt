package com.example.fooddeliveryfirebase.ui.theme

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DbHelper {

    private var mAuth: FirebaseAuth = Firebase.auth

    val userStatusLiveData = MutableLiveData<Int>() // пользователь авторизован/нет

    var cUser: FirebaseUser? = mAuth.currentUser // пользователь который сейчас авторизован

    private val myMenu: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Menu") // подключение к меню

    val myBasket: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Basket") // подключение к корзине


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
                        navController.navigate(Marshroutes.route3)
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
        navController.navigate(Marshroutes.route1)
    }

    fun getMenuFromFirebase(listData: MutableList<Product>) {
        myMenu.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val products = mutableListOf<Product>()
                for (ds in dataSnapshot.children) {
                    val name = ds.child("name").getValue(String::class.java)
                    val description = ds.child("description").getValue(String::class.java)
                    val price = ds.child("price").getValue(Double::class.java)

                    if (name != null && description != null && price != null) {
                        products.add(Product(name, description, price))
                    }
                }
                listData.clear()
                listData.addAll(products)
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
                                    navController.navigate(Marshroutes.route1)
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
            val dish = Product(name, description, price.toDouble())
            myMenu.child(name).setValue(dish)
        }
    }

    fun addInBasket(menuItem: Product, c: Int) {
        myBasket.child(cUser!!.uid).child(menuItem.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val currentValue = dataSnapshot.getValue(Int::class.java) ?: 0
                    if (c == -1 && currentValue == 1) {
                        myBasket.child(cUser!!.uid).child(menuItem.name).removeValue()
                    } else if (c == -1 && currentValue == 0) {
                    } else {
                        val newValue = currentValue + c
                        myBasket.child(cUser!!.uid).child(menuItem.name).setValue(newValue)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обработка ошибок
                }
            })
    }

    fun getBasketFromFirebase(listData: MutableList<BasketItem>) {
        myBasket.child(cUser!!.uid).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                listData.add(BasketItem(dataSnapshot.key.toString(), dataSnapshot.value.toString()))
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                listData[listData.indexOfFirst { it.key == dataSnapshot.key }] =
                    BasketItem(dataSnapshot.key.toString(), dataSnapshot.value.toString())
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                listData.removeAt(listData.indexOfFirst { it.key == dataSnapshot.key })
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}