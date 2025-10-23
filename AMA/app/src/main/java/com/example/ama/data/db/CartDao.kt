package com.example.ama.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun observeAll(): Flow<List<CartRow>>

    @Query("SELECT * FROM cart")
    suspend fun getAllOnce(): List<CartRow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(row: CartRow)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(rows: List<CartRow>)

    @Query("SELECT * FROM cart WHERE productId = :id")
    suspend fun getById(id: String): CartRow?

    @Query("DELETE FROM cart WHERE productId = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM cart")
    suspend fun clear()
}
