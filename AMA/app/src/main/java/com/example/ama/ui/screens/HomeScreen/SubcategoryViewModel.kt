package com.example.ama.ui.screens.subcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.core.dto.CategoryDto
import com.example.ama.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubcategoryViewModel(
    private val repo: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _subcats = MutableStateFlow<List<CategoryDto>>(emptyList())
    val subcats: StateFlow<List<CategoryDto>> = _subcats

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadSubcategories(parentCategoryId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val allCategories = repo.getAllCategories()

                // 🧠 Buscar la categoría padre por ID y usar sus hijos
                val parent = allCategories.find { it.id.toString() == parentCategoryId }

                val children = parent?.children ?: emptyList()

                _subcats.value = children
            } catch (e: Exception) {
                _error.value = "Error al cargar subcategorías: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}