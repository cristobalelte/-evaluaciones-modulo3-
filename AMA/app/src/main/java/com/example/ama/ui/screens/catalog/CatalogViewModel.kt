package com.example.ama.ui.screens.catalog

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.data.CartRepository
import com.example.ama.data.CatalogRepository
import com.example.ama.data.db.CartRow
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Normalizer
import java.util.UUID
import kotlin.math.min

class CatalogViewModel : ViewModel() {

    private var all: List<Product> = emptyList()

    // --- carrito (estado que ya usabas en la UI) ---
    data class CartItem(val product: Product, val qty: Int = 1)
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount

    private fun recomputeCount() {
        _cartCount.value = _cartItems.value.sumOf { it.qty }
    }

    // repo del carrito
    private var cartRepo: CartRepository? = null
    private var catalogRepo: com.example.ama.data.CatalogRepository? = null

    fun attachCatalog(context: android.content.Context) {
        if (catalogRepo == null) catalogRepo = com.example.ama.data.CatalogRepository(context)
    }
    fun addProduct(
        name: String,
        price: Double,
        author: String,
        region: String,
        type: ProductType,
        stock: Int,
        imageSrc: android.net.Uri? // uri que elegiste en la pantalla
    ) = viewModelScope.launch(Dispatchers.IO) {
        val repo = catalogRepo ?: return@launch
        val imageUrl = imageSrc?.let { repo.persistImage(it) } ?: ""

        val newProduct = com.example.ama.ui.components.Product(
            id = UUID.randomUUID().toString(),
            name = name,
            price = price,
            imageUrl = imageUrl,
            author = author,
            isActive = true,
            stock = stock,
            region = region,
            type = type
        )

        repo.add(newProduct)           // escribe en JSON
        // Actualiza memoria y UI:
        all = all + newProduct
        withContext(Dispatchers.Main) { refresh() }
    }

    fun attachCart(context: Context) {
        if (cartRepo != null) return
        cartRepo = CartRepository(context)

        viewModelScope.launch {
            cartRepo!!.rows.collectLatest { rows ->
                _cartItems.value = rows.mapNotNull { row -> mapRow(row) }
                recomputeCount()
            }
        }
    }

    private fun mapRow(row: CartRow): CartItem? {
        val p = all.firstOrNull { it.id == row.productId } ?: return null
        return CartItem(p, row.qty)
    }

    fun addToCart(p: Product) = viewModelScope.launch { cartRepo?.add(p.id) }
    fun incQty(id: String)     = viewModelScope.launch { cartRepo?.inc(id) }
    fun decQty(id: String)     = viewModelScope.launch { cartRepo?.dec(id) }
    fun removeFromCart(id: String) = viewModelScope.launch { cartRepo?.remove(id) }
    fun clearCart()            = viewModelScope.launch { cartRepo?.clear() }
    fun cartTotal(): Double    = _cartItems.value.sumOf { it.product.price * it.qty }

    // --- filtros/búsqueda (igual que antes) ---
    private val _onlyAvailable = MutableStateFlow(true)
    val onlyAvailable: StateFlow<Boolean> = _onlyAvailable

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _regions = MutableStateFlow<Set<String>>(emptySet())
    val regions: StateFlow<Set<String>> = _regions

    private val _types = MutableStateFlow<Set<ProductType>>(emptySet())
    val types: StateFlow<Set<ProductType>> = _types

    fun setQuery(q: String) { _query.value = q; refresh() }
    fun toggleRegion(r: String) { _regions.value = _regions.value.toMutableSet().also { if (!it.add(r)) it.remove(r) }; refresh() }
    fun toggleType(t: ProductType) { _types.value = _types.value.toMutableSet().also { if (!it.add(t)) it.remove(t) }; refresh() }
    fun clearFilters() { _regions.value = emptySet(); _types.value = emptySet(); _query.value = ""; _onlyAvailable.value = true; refresh() }
    fun setOnlyAvailable(v: Boolean) { _onlyAvailable.value = v; refresh() }

    // --- paginación / productos ---
    private val pageSize = 12
    private var nextIndex = 0

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private fun baseFiltered(): List<Product> =
        all.filter { p -> (!onlyAvailable.value || (p.isActive && p.stock > 0)) }

    private fun combinedFilter(): List<Product> {
        val q = _query.value
        val selectedRegions = _regions.value
        val selectedTypes = _types.value

        return baseFiltered().filter { p ->
            matchesQuery(p, q) &&
                    (selectedRegions.isEmpty() || p.region in selectedRegions) &&
                    (selectedTypes.isEmpty() || p.type in selectedTypes)
        }
    }

    fun refresh() {
        _products.value = emptyList()
        nextIndex = 0
        loadMore()
    }

    fun loadMore() {
        val filtered = combinedFilter()
        if (nextIndex >= filtered.size) return
        val end = min(nextIndex + pageSize, filtered.size)
        _products.value = _products.value + filtered.subList(nextIndex, end)
        nextIndex = end
    }

    fun getById(id: String): Product? = all.firstOrNull { it.id == id }

    // Regiones y tipos derivan de 'all'; se recalculan al cargar JSON.
    val availableRegions: List<String> get() = all.map { it.region }.distinct().sorted()
    val availableTypes: List<ProductType> = ProductType.values().toList()

    // --- JSON local ---
    fun loadFromDisk(context: Context) {
        val repo = CatalogRepository(context)
        all = repo.load()
        refresh()
    }

    // utilidades búsqueda (tus helpers)
    private fun matchesQuery(p: Product, q: String): Boolean {
        if (q.isBlank()) return true
        val nq = norm(q)
        val fields = listOf(p.name, p.author).map(::norm)
        return fields.any { it.contains(nq) || editDistance(it, nq) <= typoThreshold(nq) }
    }
    private fun typoThreshold(q: String) = when { q.length <= 4 -> 1; q.length <= 8 -> 2; else -> 3 }
    private fun norm(s: String) = Normalizer.normalize(s.lowercase(), Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    private fun editDistance(a: String, b: String): Int { /* …igual que tenías… */
        val m=a.length; val n=b.length; if(m==0)return n; if(n==0)return m
        val dp = IntArray(n+1){it}
        for(i in 1..m){
            var prev = dp[0]; dp[0]=i
            for(j in 1..n){
                val tmp=dp[j]; val cost = if(a[i-1]==b[j-1])0 else 1
                dp[j] = min(min(dp[j]+1, dp[j-1]+1), prev+cost); prev=tmp
            }
        }
        return dp[n]
    }
}

