package com.example.fooddeliveryfirebase.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddeliveryfirebase.MainActivity

object MyNavigation {
    @RequiresApi(Build.VERSION_CODES.O)
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
            composable(route = Marshroutes.connectingRoute)
            {
                ConnectingScreen(navController = navController, db)
            }
            composable(route = Marshroutes.menuRoute)
            {
                MenuScreen(db, navController)
            }

            composable(route = Marshroutes.forAdminRoute)
            {
                ForAdminScreen(imgHandler, db, navController)
            }
            composable(route = Marshroutes.basketRoute)
            {
                BasketScreen(navController = navController, db = db)
            }
            composable(route = Marshroutes.orderRoute) {
                CheckoutScreen(navController = navController, db)
            }
            composable(route = Marshroutes.profileRoute)
            {
                ProfileScreen(navController = navController, db = db)
            }

            composable(route = Marshroutes.currentOrdersRoute)
            {
                CurrentOrders(db = db, navController = navController)
            }

            composable(route = "${Marshroutes.orderBasketRoute}/{orderId}/{userId}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                OrderBasketScreen(db = db, orderId = orderId, navController = navController, userId)
            }

            composable(route = Marshroutes.addDish) {
                AddNewDish(imgHandler =  imgHandler, db = db)
            }
            
            composable(route = Marshroutes.deleteDish)
            {
                DeleteDish(db = db)
            }

            composable(route = Marshroutes.ordersForAdmin)
            {
                CheckOrdersForAdmin(db = db, navController = navController)
            }

            composable(route = Marshroutes.completedOrders)
            {
                CompletedOrdersScreen(db = db, navController = navController)
            }
        }
    }
}

object Marshroutes {
    const val loginRoute = "LogIn"
    const val registrationRoute = "Registration"
    const val connectingRoute = "Connecting"
    const val menuRoute = "Menu"
    const val basketRoute = "Basket"
    const val orderRoute = "Order"
    const val profileRoute = "Profile"

    const val currentOrdersRoute = "CurrentOrders"
    const val orderBasketRoute = "OrderBasket"

    const val forAdminRoute = "ForAdmin"
    const val addDish = "AddNewDish"
    const val deleteDish = "DeleteDish"
    const val ordersForAdmin = "OrdersForAdmin"


    const val completedOrders = "CompletedOrders"
}
