package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

//@Composable
//fun ProfileScreen(navController: NavController, db: DbHelper) {
//    Column(
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxSize()
//    ) {
//        Text(
//            text = "User Profile",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//        Text(
//            text = "Logged in as: ${db.cUser!!.email}",
//            fontSize = 18.sp,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = { navController.navigate(Marshroutes.currentOrdersRoute) },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            Text("Current Orders")
//        }
//
//        Button(
//            onClick = { /* TODO: Handle Completed Orders */ },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            Text("Completed Orders")
//        }
//        Button(
//            onClick = { db.logOut(navController) },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            Text("Logout")
//        }
//    }
//}


@Composable
fun ProfileScreen(navController: NavController, db: DbHelper) {


    BottomBarForMenu(
        navController = navController,
        function = { ProfileColumn(db = db, navController = navController) })

}

@Composable
fun ProfileColumn(db: DbHelper, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "User Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color.White // Цвет текста
        )
        Text(
            text = "Logged in as: ${db.cUser!!.email}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.White // Цвет текста
        )

        Divider(
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(Marshroutes.currentOrdersRoute) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text("Current Orders")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* TODO: Handle Completed Orders */ },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text("Completed Orders")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { db.logOut(navController) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text("Logout")
        }
    }
}