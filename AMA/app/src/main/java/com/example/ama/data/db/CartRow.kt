package com.example.ama.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartRow(
    @PrimaryKey val productId: String,
    val name: String,
    val price: Double,
    val imageUrl: String?,   // si no tienes, déjalo null
    val qty: Int
)

