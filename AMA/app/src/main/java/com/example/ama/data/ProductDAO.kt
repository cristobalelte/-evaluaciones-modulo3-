package com.example.ama.data

import androidx.room.Dao
import androidx.room.Query
import com.example.ama.data.dataclass.ProductData
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDAO {
    @Query("SELECT * FROM product")
    fun getAll(): Flow<List<ProductData>>

}