package com.example.ama.ui.screens.catalog

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.Normalizer
import kotlin.math.min

class CatalogViewModel : ViewModel() {

    // ---- Datos mock con región y tipo ----
    private val all: List<Product> = listOf(
        Product("1","Bufanda de lana tejida a mano",15000.0,"","Juana Pérez",  true,3, region="RM",     type=ProductType.TEXTIL),
        Product("2","Juego de cerámica pintado a mano",25000.0,"","Cristóbal Elte", true,1, region="Valparaíso", type=ProductType.CERAMICA),
        Product("3","Canasto mimbre",18000.0,"","Fernando Rojas", false,5, region="Biobío", type=ProductType.MADERA),
        Product("4","Chaleco tejido",22000.0,"","Rosa Muñoz", true,0, region="Araucanía", type=ProductType.TEXTIL),
    )

    // ---- Estado carrito ----
    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount
    fun addToCart(p: Product) { _cartCount.value = _cartCount.value + 1 }

    // ---- Filtros / búsqueda ----
    private val _onlyAvailable = MutableStateFlow(true)
    val onlyAvailable: StateFlow<Boolean> = _onlyAvailable

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _regions = MutableStateFlow<Set<String>>(emptySet())
    val regions: StateFlow<Set<String>> = _regions

    private val _types = MutableStateFlow<Set<ProductType>>(emptySet())
    val types: StateFlow<Set<ProductType>> = _types

    fun setQuery(q: String) { _query.value = q; refresh() }
    fun toggleRegion(r: String) {
        _regions.value = _regions.value.toMutableSet().also { if(!it.add(r)) it.remove(r) }
        refresh()
    }
    fun toggleType(t: ProductType) {
        _types.value = _types.value.toMutableSet().also { if(!it.add(t)) it.remove(t) }
        refresh()
    }
    fun clearFilters() {
        _regions.value = emptySet()
        _types.value = emptySet()
        _query.value = ""
        _onlyAvailable.value = true
        refresh()
    }
    fun setOnlyAvailable(v: Boolean) { _onlyAvailable.value = v; refresh() }

    // ---- Paginación ----
    private val pageSize = 12
    private var nextIndex = 0

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private fun baseFiltered(): List<Product> =
        all.filter { p -> (!onlyAvailable.value || (p.isActive && p.stock > 0)) }

    // Búsqueda tolerante a typos: normaliza, contiene o distancia de edición ≤ umbral
    private fun matchesQuery(p: Product, q: String): Boolean {
        if (q.isBlank()) return true
        val nq = norm(q)
        val fields = listOf(p.name, p.author).map(::norm) // buscamos por nombre y autor
        return fields.any { it.contains(nq) || editDistance(it, nq) <= typoThreshold(nq) }
    }
    private fun typoThreshold(q: String): Int = when {
        q.length <= 4 -> 1
        q.length <= 8 -> 2
        else -> 3
    }
    private fun norm(s: String): String =
        Normalizer.normalize(s.lowercase(), Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

    // Edit distance simple
    private fun editDistance(a: String, b: String): Int {
        val m = a.length; val n = b.length
        if (m == 0) return n; if (n == 0) return m
        val dp = IntArray(n + 1) { it }
        for (i in 1..m) {
            var prev = dp[0]
            dp[0] = i
            for (j in 1..n) {
                val tmp = dp[j]
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[j] = min(min(dp[j] + 1, dp[j - 1] + 1), prev + cost)
                prev = tmp
            }
        }
        return dp[n]
    }

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
    fun getById(id: String): Product? = all.firstOrNull { it.id == id }

    fun loadMore() {
        val filtered = combinedFilter()
        if (nextIndex >= filtered.size) return
        val end = min(nextIndex + pageSize, filtered.size)
        _products.value = _products.value + filtered.subList(nextIndex, end)
        nextIndex = end
    }

    // Datos para UI filtros
    val availableRegions: List<String> = all.map { it.region }.distinct().sorted()
    val availableTypes: List<ProductType> = ProductType.values().toList()
}

