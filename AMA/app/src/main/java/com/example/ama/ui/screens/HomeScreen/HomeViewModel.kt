package com.example.ama.ui.screens.HomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.core.dto.CategoryDto
import com.example.ama.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repo = CategoryRepository()

    private val _categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categories: StateFlow<List<CategoryDto>> = _categories

    private val _categoriesError = MutableStateFlow<String?>(null)
    val categoriesError: StateFlow<String?> = _categoriesError

    private val _loadingCategories = MutableStateFlow(false)
    val loadingCategories: StateFlow<Boolean> = _loadingCategories

    fun loadCategories() = viewModelScope.launch {
        _loadingCategories.value = true
        _categoriesError.value = null
        try {
            _categories.value = repo.getCategories()
        } catch (e: Exception) {
            _categoriesError.value = e.message ?: "Error cargando categorías"
        } finally {
            _loadingCategories.value = false
        }
    }
}