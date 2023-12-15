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
            composable(route = Marshroutes.loginRoute) {
                LoginScreen(navController = navController, db)
            }
            composable(route = Marshroutes.registrationRoute) {
                RegistrationScreen(navController = navController, db)
            }
            composable(route = Marshroutes.profileRoute)
            {
                ConnectingScreen(navController = navController, db)
            }
            composable(route = Marshroutes.menuRoute)
            {
                MenuScreen(db, navController)
            }

            composable(route = Marshroutes.forAdminRoute)
            {
                ForAdminScreen(imgHandler, db)
            }
            composable(route = Marshroutes.basketRoute)
            {
                BasketScreen(navController = navController,db = db)
            }
            composable(route = Marshroutes.orderRoute) {
                CheckoutScreen(navController = navController, db)
            }
        }
    }
}

object Marshroutes {
    const val loginRoute = "LogIn"
    const val registrationRoute = "Registration"
    const val profileRoute = "Profile"
    const val menuRoute = "Menu"
    const val forAdminRoute = "ForAdmin"
    const val basketRoute = "Basket"
    const val orderRoute = "Order"
}
