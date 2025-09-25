package com.example.ama.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import kotlinx.serialization.encodeToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.net.Uri
import java.util.UUID

// ---- MODELOS SERIALIZABLES ----
@Serializable
data class ProductDTO(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String = "",
    val author: String = "",
    val isActive: Boolean = true,
    val stock: Int = 0,
    val region: String = "",
    val type: String // se mapea a tu enum ProductType
)
private fun toDto(p: com.example.ama.ui.components.Product) = ProductDTO(
    id = p.id,
    name = p.name,
    price = p.price,
    imageUrl = p.imageUrl,
    author = p.author,
    isActive = p.isActive,
    stock = p.stock,
    region = p.region,
    type = p.type.name
)
class CatalogRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private val internalFile: File get() = File(context.filesDir, "catalog.json")

    fun load(): List<com.example.ama.ui.components.Product> {
        ensureInternalJson()
        val txt = internalFile.readText()
        val list = json.decodeFromString<List<ProductDTO>>(txt)
        return list.mapNotNull { dto ->
            val typeEnum = runCatching {
                com.example.ama.ui.components.ProductType.valueOf(dto.type)
            }.getOrNull() ?: com.example.ama.ui.components.ProductType.OTRO

            com.example.ama.ui.components.Product(
                id = dto.id,
                name = dto.name,
                price = dto.price,
                imageUrl = dto.imageUrl,
                author = dto.author,
                isActive = dto.isActive,
                stock = dto.stock,
                region = dto.region,
                type = typeEnum
            )
        }
    }

    private fun ensureInternalJson() {
        if (internalFile.exists()) return
        // copia desde assets la primera vez
        context.assets.open("catalog_seed.json").use { inS ->
            internalFile.outputStream().use { outS -> inS.copyTo(outS) }
        }
    }
    /** Sobrescribe el JSON interno con la lista completa */
    suspend fun saveAll(products: List<com.example.ama.ui.components.Product>) = withContext(Dispatchers.IO) {
        val dtos = products.map(::toDto)
        internalFile.writeText(json.encodeToString(dtos))
    }


    suspend fun add(product: com.example.ama.ui.components.Product) = withContext(Dispatchers.IO) {
        val current = load()                 // lee lo que hay
        val next = current + product
        saveAll(next)                        // escribe el JSON actualizado
    }


    suspend fun persistImage(src: Uri): String = withContext(Dispatchers.IO) {
        val imagesDir = File(context.filesDir, "images").apply { mkdirs() }
        val outFile = File(imagesDir, "${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(src)!!.use { inS ->
            outFile.outputStream().use { outS -> inS.copyTo(outS) }
        }
        "file://${outFile.absolutePath}"
    }
}
