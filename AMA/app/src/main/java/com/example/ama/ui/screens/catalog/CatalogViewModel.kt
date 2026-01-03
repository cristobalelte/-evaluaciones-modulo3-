package com.example.ama.ui.screens.catalog

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.core.dto.ProductDto
import com.example.ama.data.CartRepository
import com.example.ama.data.CatalogRepository
import com.example.ama.data.db.CartRow
import com.example.ama.data.repository.ProductRepository
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.Subcategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Normalizer
import java.util.UUID

class CatalogViewModel : ViewModel() {

    // ------------------ Estado base catálogo local ------------------
    private var all: List<Product> = emptyList()

    data class CartItem(val product: Product, val qty: Int = 1)

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount

    // ------------------ Repos ------------------
    private var cartRepo: CartRepository? = null
    private var catalogRepo: CatalogRepository? = null
    private val productRepo = ProductRepository()

    private var lastRows: List<CartRow> = emptyList()

    // ------------------ NUEVOS PRODUCTOS (HOME) ------------------
    private val _newProducts = MutableStateFlow<List<Product>>(emptyList())
    val newProducts: StateFlow<List<Product>> = _newProducts

    private val _isLoadingNew = MutableStateFlow(false)
    val isLoadingNew: StateFlow<Boolean> = _isLoadingNew

    private val _errorNew = MutableStateFlow<String?>(null)
    val errorNew: StateFlow<String?> = _errorNew

    /** Mapper backend -> UI (OJO: price en tu DTO es Int?, NO String) */
    private fun ProductDto.toUi(): Product = Product(
        id = (id ?: 0).toString(),
        name = name ?: "(Sin nombre)",
        price = ((this.price ?: 0).toString()).toDouble(),
        imageUrl = "", // backend aún no trae imagen usable en tu UI actual
        author = sellerUserId?.toString() ?: "",
        isActive = publicationStatus == "PUBLISHED",
        stock = stock ?: 0,
        region = "",
        type = ProductType.OTRO,
        description = description ?: "",
        createdAt = 0L
    )

    fun loadNewProducts() = viewModelScope.launch {
        _isLoadingNew.value = true
        _errorNew.value = null

        try {
            val dtos = withContext(Dispatchers.IO) {
                productRepo.getAllNewProducts() // ya trae los 4 últimos publicados
            }

            val ui = withContext(Dispatchers.IO) {
                dtos.map { dto ->
                    val pid: Int = when (val raw = dto.id) {
                        is Int -> raw
                        is Number -> raw.toInt()
                        is String -> raw.toIntOrNull() ?: 0
                        else -> 0
                    }
                    val imgPath = if (pid != 0) productRepo.getPrimaryImageUrl(pid) else null
                    val full = productRepo.fullImageUrl(imgPath) ?: ""
                    dto.toUi().copy(imageUrl = full)
                }
            }

            _newProducts.value = ui

        } catch (e: Exception) {
            _errorNew.value = e.message ?: "Error"
            _newProducts.value = emptyList()
        } finally {
            _isLoadingNew.value = false
        }
    }


    // ------------------ Catálogo local (JSON) ------------------
    fun attachCatalog(context: Context) {
        if (catalogRepo == null) catalogRepo = CatalogRepository(context)
    }

    fun loadFromDisk(context: Context) {
        val repo = CatalogRepository(context)
        all = repo.load()
        refresh()
        remapRows()
    }

    fun addProduct(
        name: String,
        price: Double,
        author: String,
        region: String,
        type: ProductType,
        stock: Int,
        imageSrc: Uri?,
        description: String,
        subcategory: Subcategory,
    ) = viewModelScope.launch(Dispatchers.IO) {
        val repo = catalogRepo ?: return@launch
        val imageUrl = imageSrc?.let { repo.persistImage(it) } ?: ""

        val newProduct = Product(
            id = UUID.randomUUID().toString(),
            name = name,
            price = price,
            imageUrl = imageUrl,
            author = author,
            isActive = true,
            stock = stock,
            region = region,
            type = type,
            description = description,
            subcategory = subcategory,
            createdAt = System.currentTimeMillis()
        )

        repo.add(newProduct)
        all = all + newProduct

        withContext(Dispatchers.Main) {
            refresh()
            remapRows()
        }
    }

    // ------------------ Carrito ------------------
    fun attachCart(context: Context) {
        if (cartRepo != null) return
        cartRepo = CartRepository(context)
        viewModelScope.launch {
            cartRepo!!.rows.collectLatest { rows ->
                lastRows = rows
                remapRows()
            }
        }
    }

    private fun remapRows() {
        _cartItems.value = lastRows.mapNotNull { row ->
            all.firstOrNull { it.id == row.productId }?.let { p -> CartItem(p, row.qty) }
        }
        _cartCount.value = _cartItems.value.sumOf { it.qty }
    }

    fun addToCart(p: Product) = viewModelScope.launch {
        cartRepo?.add(p.id, p.name, p.price, p.imageUrl)
    }

    fun incQty(id: String) = viewModelScope.launch { cartRepo?.inc(id) }
    fun decQty(id: String) = viewModelScope.launch { cartRepo?.dec(id) }
    fun removeFromCart(id: String) = viewModelScope.launch { cartRepo?.remove(id) }
    fun clearCart() = viewModelScope.launch { cartRepo?.clear() }

    // ------------------ Filtros / búsqueda (lo que te falta) ------------------
    private val _onlyAvailable = MutableStateFlow(true)
    val onlyAvailable: StateFlow<Boolean> = _onlyAvailable

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _regions = MutableStateFlow<Set<String>>(emptySet())
    val regions: StateFlow<Set<String>> = _regions

    private val _types = MutableStateFlow<Set<ProductType>>(emptySet())
    val types: StateFlow<Set<ProductType>> = _types

    val availableRegions: List<String>
        get() = all.map { it.region }.filter { it.isNotBlank() }.distinct().sorted()

    val availableTypes: List<ProductType>
        get() = ProductType.values().toList()

    fun setOnlyAvailable(v: Boolean) { _onlyAvailable.value = v; refresh() }
    fun setQuery(q: String) { _query.value = q; refresh() }

    fun toggleRegion(r: String) {
        _regions.value = _regions.value.toMutableSet().also { if (!it.add(r)) it.remove(r) }
        refresh()
    }

    fun toggleType(t: ProductType) {
        _types.value = _types.value.toMutableSet().also { if (!it.add(t)) it.remove(t) }
        refresh()
    }

    fun clearFilters() {
        _regions.value = emptySet()
        _types.value = emptySet()
        _query.value = ""
        _onlyAvailable.value = true
        refresh()
    }

    // ------------------ Productos filtrados para UI ------------------
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    fun refresh() {
        val q = _query.value.trim()
        val selectedRegions = _regions.value
        val selectedTypes = _types.value
        val onlyAvail = _onlyAvailable.value

        _products.value = all.filter { p ->
            val okAvail = !onlyAvail || (p.isActive && p.stock > 0)
            val okRegion = selectedRegions.isEmpty() || p.region in selectedRegions
            val okType = selectedTypes.isEmpty() || p.type in selectedTypes
            val okQuery = matchesQuery(p, q)
            okAvail && okRegion && okType && okQuery
        }
    }

    fun getById(id: String): Product? = all.firstOrNull { it.id == id }

    private fun matchesQuery(p: Product, q: String): Boolean {
        if (q.isBlank()) return true
        val nq = norm(q)
        val fields = listOf(p.name, p.author).map(::norm)
        return fields.any { it.contains(nq) }
    }

    private fun norm(s: String) = Normalizer
        .normalize(s.lowercase(), Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
}
