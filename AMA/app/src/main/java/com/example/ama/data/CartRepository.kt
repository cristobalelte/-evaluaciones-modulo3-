package com.example.ama.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ama.core.dto.ShoppingCarsDto
import com.example.ama.core.network.NetworkModule
import com.example.ama.data.db.AppDb
import com.example.ama.data.db.CartRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CartRepository(context: Context) {

    // --- Room (DB + migración 1 -> 2: agrega name/price/imageUrl) ---
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""ALTER TABLE cart ADD COLUMN name TEXT NOT NULL DEFAULT ''""")
            db.execSQL("""ALTER TABLE cart ADD COLUMN price REAL NOT NULL DEFAULT 0.0""")
            db.execSQL("""ALTER TABLE cart ADD COLUMN imageUrl TEXT""")
        }
    }

    private val db: AppDb = Room.databaseBuilder(
        context.applicationContext,
        AppDb::class.java,
        "ama.db"
    )
        .addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigration() // opcional mientras iteras
        .build()

    private val dao = db.cartDao()
    private val api = NetworkModule.api

    val rows: Flow<List<CartRow>> = dao.observeAll()
    suspend fun refreshFromServer(owner: String) = withContext(Dispatchers.IO) {
        try {
            // Este endpoint devuelve TODOS los carritos.
            val url = "http://54.243.16.169:3000/carrito"
            val carts: List<ShoppingCarsDto> = api.getShoppingCarList(url)

            Log.d("AMA", "GET /carrito -> ${carts.size} carritos")

            // Quédate SOLO con el carrito del usuario actual
            val myCart = carts.firstOrNull { it.owner == owner }

            // Si no hay carrito del usuario, deja Room vacío/limpio
            val mapped = myCart
                ?.productos
                .orEmpty()
                .filterNotNull() // backend trae un null en un carrito
                .map { p ->
                    CartRow(
                        productId = p.id,             // String en tu API
                        name      = p.name ?: "",
                        price     = (p.price ?: 0.0).toDouble(),
                        imageUrl  = null,
                        qty       = 1
                    )
                }

            db.withTransaction {
                dao.clear()
                if (mapped.isNotEmpty()) dao.upsertAll(mapped)
            }
        } catch (e: HttpException) {
            Log.e("AMA", "HTTP ${e.code()} al leer carrito", e)
        } catch (e: Exception) {
            Log.e("AMA", "Error leyendo carrito", e)
        }
    }

    // Operaciones LOCALES del carrito (Room) usadas por la UI

    suspend fun add(product: com.example.ama.ui.components.Product) {
        val current = dao.getById(product.id)
        val nextQty = (current?.qty ?: 0) + 1

        val row = CartRow(
            productId = product.id,
            name      = product.name,
            price     = product.price,
            imageUrl  = product.imageUrl,
            qty       = nextQty
        )
        dao.upsert(row)
    }

    suspend fun inc(productId: String) {
        val current = dao.getById(productId) ?: return
        dao.upsert(current.copy(qty = current.qty + 1))
    }

    suspend fun dec(productId: String) {
        val current = dao.getById(productId) ?: return
        val next = current.qty - 1
        if (next <= 0) dao.delete(productId) else dao.upsert(current.copy(qty = next))
    }

    suspend fun remove(productId: String) = dao.delete(productId)
    suspend fun clear() = dao.clear()
}
