package com.example.ama.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductData(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val price: Double,
    val material: String
)
