package com.example.ama.data.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductData(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String? = null,
    val price: Double? = null,
    val material: String? = null,
    val craftType: String? = null,
    val isFeatured: Boolean? = null,
    val isActive: Boolean? = null,
    val creatorId: String? = null,
    val stock: Int? = null,
    val region: String? = null,
    val createdAt: String? = null
)