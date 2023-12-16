package com.example.fooddeliveryfirebase.ui.theme

data class Product(
    val name: String,
    val description: String,
    val price: Double,
    var cValue: Int? = 0
)