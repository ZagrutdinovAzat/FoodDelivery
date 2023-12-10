package com.example.fooddeliveryfirebase.ui.theme

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

class Product(
    val name: String,
    val description: String,
    val price: Double,
    var cValue: Int? = null
)