package com.example.ama.data

import android.content.Context
import androidx.room.Room
import com.example.ama.data.db.AppDb
import com.example.ama.data.db.CartRow
import kotlinx.coroutines.flow.Flow

class CartRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDb::class.java,
        "ama.db"
    ).build()

    private val dao = db.cartDao()

    val rows: Flow<List<CartRow>> = dao.observeAll()

    suspend fun add(productId: String) {
        val current = dao.getById(productId)
        val nextQty = (current?.qty ?: 0) + 1
        dao.upsert(CartRow(productId, nextQty))
    }

    suspend fun inc(productId: String) = add(productId)

    suspend fun dec(productId: String) {
        val current = dao.getById(productId) ?: return
        val next = current.qty - 1
        if (next <= 0) dao.delete(productId) else dao.upsert(CartRow(productId, next))
    }

    suspend fun remove(productId: String) = dao.delete(productId)
    suspend fun clear() = dao.clear()
}
