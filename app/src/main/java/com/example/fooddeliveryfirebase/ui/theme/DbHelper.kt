package com.example.fooddeliveryfirebase.ui.theme

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.fooddeliveryfirebase.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DbHelper {

    var mAuth: FirebaseAuth = Firebase.auth

    val userStatusLiveData = MutableLiveData<Int>() // пользователь авторизован/нет

    var cUser: FirebaseUser? = mAuth.currentUser // пользователь который сейчас авторизован

    val mDatabase: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Menu") // подключение к меню

    fun logIn(login: String, password: String, context: Context, navController: NavController)
    {
        mAuth.signInWithEmailAndPassword(login, password)
            .addOnCompleteListener() { task ->
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



}