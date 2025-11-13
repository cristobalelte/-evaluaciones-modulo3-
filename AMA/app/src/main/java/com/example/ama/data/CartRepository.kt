package com.example.ama.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ama.core.dto.ShoppingCarsDto

import com.example.ama.data.db.AppDb
import com.example.ama.data.db.CartRow
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.Instant
import kotlin.math.roundToInt

class CartRepository(context: Context) {


    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""ALTER TABLE cart ADD COLUMN name TEXT NOT NULL DEFAULT ''""")
            db.execSQL("""ALTER TABLE cart ADD COLUMN price REAL NOT NULL DEFAULT 0.0""")
            db.execSQL("""ALTER TABLE cart ADD COLUMN imageUrl TEXT""")
        }
    }

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDb::class.java,
        "ama.db"
    )
        .addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigrationOnDowngrade()
        .build()

    private val dao = db.cartDao()
    private val api = NetworkModule.api
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val cartAdapter = moshi.adapter(ShoppingCarsDto::class.java)
    val rows: Flow<List<CartRow>> = dao.observeAll()

    /** Trae el carrito del backend y lo guarda en Room. */
    suspend fun refreshFromServer(owner: String? = null) = withContext(Dispatchers.IO) {
        val carts = api.getShoppingCarList(owner) // si no filtra en server, igual filtra cliente
        val chosen = if (owner != null) {
            carts.firstOrNull { it.owner.equals(owner, ignoreCase = true) }
        } else {
            carts.maxByOrNull { it.createdAt ?: "" }
        }

        if (owner != null && chosen == null) {
            // NO hay carrito remoto de este owner -> NO toques la DB local
            Log.d("AMA", "No hay carrito remoto para owner=$owner; se mantiene el local.")
            return@withContext
        }

        val mapped = chosen?.productos
            ?.filterNotNull()
            ?.map { p ->
                CartRow(
                    productId = p.id.toString(),
                    name      = p.name ?: "",
                    price     = (p.price ?: 0).toDouble(),
                    imageUrl  = null,
                    qty       = 1
                )
            }.orEmpty()

        db.withTransaction {
            dao.clear()
            if (mapped.isNotEmpty()) dao.upsertAll(mapped)
        }
    Log.d("AMA", "GET /carrito -> ${carts.size} carritos (owner=$owner), elegido=$chosen")
    }


    suspend fun add(id: String, name: String, price: Double, imageUrl: String?) {
        val current = dao.getById(id)
        val nextQty = (current?.qty ?: 0) + 1
        dao.upsert(
            CartRow(
                productId = id,
                name = name,
                price = price,
                imageUrl = imageUrl, // puede ser null
                qty = nextQty
            )
        )
    }
    suspend fun pushCart(owner: String): Boolean {
        // Mapea Room -> DTO
        val rows = dao.getAllOnce()

        // Productos como lista de Map, con id > 0
        val productos = rows.mapIndexed { idx, r ->
            val pid = r.productId.toIntOrNull() ?: (idx + 1)  // nunca 0
            mapOf(
                "id"    to pid,
                "name"  to (r.name ?: "Producto ${idx + 1}"),
                "price" to ((r.price ?: 0.0).roundToInt()).coerceAtLeast(0)
            )
        }

        val total = productos.sumOf { it["price"] as Int }

        // Payload que respeta lo que mostraba Swagger (createdAt y, si quieren, id del carrito)
        val payload: Map<String, Any> = mapOf(
            // si el backend exige id del carrito, puedes enviar un folio simple > 0
            "id"        to ((System.currentTimeMillis() / 1000).toInt()),
            "productos" to productos,
            "total"     to total,
            "createdAt" to Instant.now().toString(),  // ISO 8601
            "owner"     to owner
        )

        Log.d("AMA", "POST /carrito payload: ${payload}")

        return try {
            val resp = NetworkModule.api.upsertShoppingCarRaw(payload)
            if (resp.isSuccessful) {
                Log.d("AMA", "POST /carrito OK")
                true
            } else {
                Log.e("AMA", "POST /carrito ${resp.code()} ${resp.message()} \n${resp.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("AMA", "POST /carrito EX", e)
            false
        }
    }

    suspend fun inc(productId: String) {
        val c = dao.getById(productId) ?: return
        dao.upsert(c.copy(qty = c.qty + 1))
    }
    suspend fun dec(productId: String) {
        val c = dao.getById(productId) ?: return
        val next = c.qty - 1
        if (next <= 0) dao.delete(productId) else dao.upsert(c.copy(qty = next))
    }
    suspend fun remove(productId: String) = dao.delete(productId)
    suspend fun clear() = dao.clear()

}
