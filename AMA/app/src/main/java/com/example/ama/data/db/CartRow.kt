package com.example.ama.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartRow(
    @PrimaryKey val productId: String,
    val qty: Int
)