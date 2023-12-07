package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun ConnectingScreen(navController: NavController, mAuth: FirebaseAuth) {
    backGroundImage()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        val cUser: FirebaseUser? = mAuth.currentUser
        val userEmail = cUser?.email
        if (userEmail != null)
            Text(text = "You are logged in: $userEmail", color = Color.White)

        logOutButton(mAuth = mAuth, navController = navController)

        resumeButton(navController = navController)

        forAdminButton(navController = navController)

    }
}

@Composable
fun logOutButton(mAuth: FirebaseAuth, navController: NavController) {
    Button(
        onClick = {
            mAuth.signOut()
            navController.navigate(Marshroutes.route1)
        }, shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .width(150.dp)
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
            //.background(color = Color.Black)
    ) {
        Text(text = "log out", color = Color.White)
    }
}

@Composable
fun resumeButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Marshroutes.route4)
        }, shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .width(150.dp)
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Text(text = "resume", color = Color.White)
    }
}

@Composable
fun forAdminButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Marshroutes.route5)
        }, shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .width(150.dp)
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
            //.background(color = Color.Black)
    ) {
        Text(text = "for admin", color = Color.White)
    }
}
