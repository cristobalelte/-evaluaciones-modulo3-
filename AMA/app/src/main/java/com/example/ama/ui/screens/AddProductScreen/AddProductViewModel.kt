package com.example.ama.ui.screens.AddProductScreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.core.dto.CreateProductRequest
import com.example.ama.core.dto.ProductDto
import com.example.ama.core.dto.PublishForm
import com.example.ama.core.dto.PublishStep
import com.example.ama.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class AddProductUiState(
    val step: PublishStep = PublishStep.STEP_1,
    val form: PublishForm = PublishForm(),
    val loading: Boolean = false,
    val error: String? = null,
    val created: ProductDto? = null
)

class AddProductViewModel(
    private val productRepo: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(AddProductUiState())
    val ui: StateFlow<AddProductUiState> = _ui

    fun update(transform: (PublishForm) -> PublishForm) {
        _ui.update { it.copy(form = transform(it.form), error = null) }
    }

    fun setImage(uri: Uri?) = update { it.copy(imageUri = uri) }

    fun back() {
        _ui.update {
            val prev = when (it.step) {
                PublishStep.STEP_1 -> PublishStep.STEP_1
                PublishStep.STEP_2 -> PublishStep.STEP_1
                PublishStep.STEP_3 -> PublishStep.STEP_2
            }
            it.copy(step = prev, error = null)
        }
    }

    fun next() {
        val s = _ui.value
        val can = when (s.step) {
            PublishStep.STEP_1 -> validateStep1(s.form)
            PublishStep.STEP_2 -> validateStep2(s.form)
            PublishStep.STEP_3 -> true
        }
        if (!can) return

        _ui.update {
            val next = when (it.step) {
                PublishStep.STEP_1 -> PublishStep.STEP_2
                PublishStep.STEP_2 -> PublishStep.STEP_3
                PublishStep.STEP_3 -> PublishStep.STEP_3
            }
            it.copy(step = next, error = null)
        }
    }

    fun publish() = viewModelScope.launch {
        val f = _ui.value.form
        val req = toRequestOrNull(f)
        if (req == null) {
            _ui.update { it.copy(error = "Revisa Nombre y Precio (número) y Categoría (ID numérico).") }
            return@launch
        }

        _ui.update { it.copy(loading = true, error = null, created = null) }

        try {
            val created = productRepo.createProduct(req)
            _ui.update { it.copy(loading = false, created = created, step = PublishStep.STEP_3) }

            // Si tu catálogo usa backend, con un reload debería aparecer.
            // Si usas Room local, aquí deberías insertar en DB o invalidar cache (según tu repo).

        } catch (e: HttpException) {
            _ui.update { it.copy(loading = false, error = "Error HTTP ${e.code()}") }
        } catch (_: IOException) {
            _ui.update { it.copy(loading = false, error = "Sin conexión o timeout") }
        } catch (e: Exception) {
            _ui.update { it.copy(loading = false, error = e.message ?: "Error") }
        }
    }

    private fun validateStep1(f: PublishForm): Boolean {
        val okName = f.name.trim().isNotBlank()
        val okCat = f.categoryId.trim().toIntOrNull() != null
        return if (okName && okCat) true else {
            _ui.update { it.copy(error = "Completa Nombre y Categoría.") }
            false
        }
    }

    private fun validateStep2(f: PublishForm): Boolean {
        val okPrice = f.price.trim().toIntOrNull()?.let { it > 0 } == true
        return if (okPrice) true else {
            _ui.update { it.copy(error = "Precio debe ser un número mayor a 0.") }
            false
        }
    }

    private fun toRequestOrNull(f: PublishForm): CreateProductRequest? {
        val price = f.price.trim().toIntOrNull() ?: return null
        val cat = f.categoryId.trim().toIntOrNull()

        if (f.name.trim().isBlank()) return null

        return CreateProductRequest(
            name = f.name.trim(),
            description = f.description.trim().ifBlank { null },
            categoryId = cat,
            material = f.material.trim().ifBlank { null },
            color = f.color.trim().ifBlank { null },
            size = f.size.trim().ifBlank { null },
            price = price,
            currency = f.currency,
            stock = f.stock.trim().toIntOrNull() ?: 1,
            publicationStatus = f.publicationStatus
        )
    }
}

