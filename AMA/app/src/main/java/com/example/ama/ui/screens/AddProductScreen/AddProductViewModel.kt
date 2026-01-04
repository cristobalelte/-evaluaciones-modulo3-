package com.example.ama.ui.screens.AddProductScreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.core.dto.CategoryDto
import com.example.ama.core.mappers.toCreateRequestOrNull
import com.example.ama.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

enum class PublishStep { STEP_1, STEP_2, STEP_3 }

data class PublishForm(
    val name: String = "",
    val categoryId: String = "",
    val subcategoryId: String = "",
    val material: String = "",
    val color: String = "",
    val size: String = "",
    val price: String = "",
    val currency: String = "CLP",
    val stock: String = "1",
    val publicationStatus: String = "PUBLISHED",
    val description: String = "",
    val imageUri: Uri? = null
)

data class AddProductUiState(
    val step: PublishStep = PublishStep.STEP_1,
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val createdProductId: Int? = null,

    val loadingCategories: Boolean = false,
    val categories: List<CategoryDto> = emptyList(),
    val mainCategories: List<CategoryDto> = emptyList(),
    val subcategories: List<CategoryDto> = emptyList(),
    val selectedCategory: CategoryDto? = null,
    val selectedSubcategory: CategoryDto? = null,

    val isValidName: Boolean = false,
    val isValidCategory: Boolean = false,
    val isValidSubcategory: Boolean = false,
    val isValidSize: Boolean = false,
    val isValidColor: Boolean = false,
    val isValidPrice: Boolean = false,
    val isValidDescription: Boolean = false
)

class AddProductViewModel(
    private val productRepo: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _form = MutableStateFlow(PublishForm())
    val form: StateFlow<PublishForm> = _form

    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState: StateFlow<AddProductUiState> = _uiState

    // ------------------- Navegación entre pasos -------------------
    fun back() {
        _uiState.update {
            val prev = when (it.step) {
                PublishStep.STEP_1 -> PublishStep.STEP_1
                PublishStep.STEP_2 -> PublishStep.STEP_1
                PublishStep.STEP_3 -> PublishStep.STEP_2
            }
            it.copy(step = prev, error = null)
        }
    }

    fun next() {
        _uiState.update {
            val next = when (it.step) {
                PublishStep.STEP_1 -> PublishStep.STEP_2
                PublishStep.STEP_2 -> PublishStep.STEP_3
                PublishStep.STEP_3 -> PublishStep.STEP_3
            }
            it.copy(step = next, error = null)
        }
    }

    // ------------------- Setters del formulario -------------------
    fun setName(v: String) {
        _form.update { it.copy(name = v) }
        _uiState.update { it.copy(isValidName = v.trim().isNotEmpty()) }
    }

    fun setDescription(v: String) {
        _form.update { it.copy(description = v) }
        _uiState.update { it.copy(isValidDescription = v.trim().isNotEmpty()) }
    }

    fun setMaterial(v: String) = _form.update { it.copy(material = v) }

    fun setColor(v: String) {
        _form.update { it.copy(color = v) }
        _uiState.update { it.copy(isValidColor = v.trim().isNotEmpty()) }
    }

    fun setSize(v: String) {
        _form.update { it.copy(size = v) }
        _uiState.update { it.copy(isValidSize = v.trim().isNotEmpty()) }
    }

    fun setPrice(v: String) {
        val digits = v.filter(Char::isDigit)
        _form.update { it.copy(price = digits) }
        _uiState.update { it.copy(isValidPrice = digits.isNotEmpty() && digits.toIntOrNull()?.let { it > 0 } == true) }
    }

    fun setStock(v: String) = _form.update { it.copy(stock = v.filter(Char::isDigit)) }
    fun setImageUri(uri: Uri?) = _form.update { it.copy(imageUri = uri) }

    // ------------------- Categorías -------------------
    fun loadCategories() = viewModelScope.launch {
        _uiState.update { it.copy(loadingCategories = true, error = null) }
        try {
            val all = productRepo.getCategories().filter { it.isActive == true }

            val mainCats = all.filter { it.parentId.isNullOrEmpty() }
            Log.d("AddProductVM", "Categorías principales: ${mainCats.map { it.name }}")

            _uiState.update {
                it.copy(
                    loadingCategories = false,
                    categories = all,
                    mainCategories = mainCats
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(loadingCategories = false, error = "No se pudieron cargar categorías") }
        }
    }

    fun onCategorySelected(cat: CategoryDto) {
        val allCategories = _uiState.value.categories

        // Filtro robusto por coincidencia exacta de ID
        val subs = allCategories.filter { sub ->
            sub.parentId?.trim() == cat.id?.trim()
        }

        Log.d("AddProductVM", "Categoría seleccionada: ${cat.name} (${cat.id}), Subcategorías: ${subs.map { it.name }}")

        _uiState.update {
            it.copy(
                selectedCategory = cat,
                selectedSubcategory = null,
                subcategories = subs,
                isValidCategory = true,
                isValidSubcategory = subs.isEmpty() // Si no hay subcategorías, se considera válido
            )
        }

        _form.update { it.copy(categoryId = cat.id.orEmpty(), subcategoryId = "") }
    }

    fun onSubcategorySelected(sub: CategoryDto) {
        _uiState.update { it.copy(selectedSubcategory = sub, isValidSubcategory = true, error = null) }
        _form.update { it.copy(subcategoryId = sub.id.orEmpty()) }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }

    // ------------------- Validaciones por paso -------------------
    fun canContinueStep1(): Boolean = with(_uiState.value) {
        isValidName && isValidCategory && isValidSize && isValidColor && isValidPrice
    }

    fun canContinueStep2(): Boolean = _uiState.value.isValidDescription

    // ------------------- Publicación -------------------
    fun publish() = viewModelScope.launch {
        val req = _form.value.toCreateRequestOrNull()
        if (req == null) {
            _uiState.update { it.copy(error = "Revisa Nombre y Precio (número).") }
            return@launch
        }

        _uiState.update { it.copy(loading = true, error = null, success = false) }

        try {
            val created = productRepo.createProduct(req)
            val createdId = created.id

            _uiState.update {
                it.copy(
                    loading = false,
                    success = true,
                    createdProductId = createdId as? Int
                )
            }

            // Limpieza del formulario
            _form.value = PublishForm()
            _uiState.update {
                it.copy(
                    step = PublishStep.STEP_1,
                    selectedCategory = null,
                    selectedSubcategory = null,
                    subcategories = emptyList()
                )
            }

        } catch (e: HttpException) {
            _uiState.update { it.copy(loading = false, error = "Error HTTP ${e.code()}") }
        } catch (_: IOException) {
            _uiState.update { it.copy(loading = false, error = "Sin conexión o timeout") }
        } catch (e: Exception) {
            _uiState.update { it.copy(loading = false, error = e.message ?: "Error desconocido") }
        }
    }
}
