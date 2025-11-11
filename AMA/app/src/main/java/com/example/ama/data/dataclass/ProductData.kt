package com.example.ama.data.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductData(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String? = null,
    val price: Double? = null,
    val material: String? = null
)