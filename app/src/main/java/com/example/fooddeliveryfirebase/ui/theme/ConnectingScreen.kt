package com.example.fooddeliveryfirebase.ui.theme

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

@Composable
fun ConnectingScreen(navController: NavController, db: DbHelper) {
    BackGroundImage()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        val userEmail = db.cUser?.email
        if (userEmail != null)
            Text(text = "You are logged in: $userEmail", color = Color.White)

        ResumeButton(navController = navController, db = db)

        LogOutButton(db = db, navController = navController)

    }
}

@Composable
fun LogOutButton(db: DbHelper, navController: NavController) {
    Button(
        onClick = {
            db.logOut(navController)
        }, shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .width(150.dp)
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Text(text = "log out", color = Color.White)
    }
}

@Composable
fun ResumeButton(navController: NavController, db: DbHelper) {
    Button(
        onClick = {
            if (db.userRole == 0)
                navController.navigate(Marshroutes.menuRoute)
            else if (db.userRole == 2)
                navController.navigate(Marshroutes.forAdminRoute)
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
fun ForAdminButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Marshroutes.forAdminRoute)
        }, shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .width(150.dp)
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Text(text = "for admin", color = Color.White)
    }
}
