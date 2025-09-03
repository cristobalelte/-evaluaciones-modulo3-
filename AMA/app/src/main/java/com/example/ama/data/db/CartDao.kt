package com.example.ama.data.db


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun observeAll(): Flow<List<CartRow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(row: CartRow)

    @Query("SELECT * FROM cart WHERE productId = :id")
    suspend fun getById(id: String): CartRow?

    @Query("DELETE FROM cart WHERE productId = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM cart")
    suspend fun clear()
}
