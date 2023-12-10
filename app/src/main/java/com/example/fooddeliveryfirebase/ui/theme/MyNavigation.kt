package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddeliveryfirebase.MainActivity

object MyNavigation {
    @Composable
    fun MinimalNavigation(db: DbHelper, start: String, imgHandler: MainActivity.Img) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = start)
        {
            composable(route = Marshroutes.route1) {
                LoginScreen(navController = navController, db)
            }
            composable(route = Marshroutes.route2) {
                RegistrationScreen(navController = navController, db)
            }
            composable(route = Marshroutes.route3)
            {
                ConnectingScreen(navController = navController, db)
            }
            composable(route = Marshroutes.route4)
            {
                MenuScreen(db, navController)
            }

            composable(route = Marshroutes.route5)
            {
                ForAdminScreen(imgHandler, db)
            }
            composable(route = Marshroutes.route6)
            {
                BasketScreen(navController = navController,db = db)
            }
        }
    }
}

object Marshroutes {
    const val route1 = "LogIn"
    const val route2 = "Registration"
    const val route3 = "Profile"
    const val route4 = "Menu"
    const val route5 = "ForAdmin"
    const val route6 = "Basket"
}
