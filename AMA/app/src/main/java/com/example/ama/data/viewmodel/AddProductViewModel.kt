package com.example.ama.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.data.dataclass.ProductData
import com.example.ama.data.repository.AddProductRepository
import com.example.ama.ui.components.MATERIALES
import com.example.ama.ui.components.REGIONES_CHILE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddProductViewModel : ViewModel() {
    private val addProductRepository = AddProductRepository()

    val addProductList = MutableStateFlow<List<ProductData>>(emptyList())

   /* init {
        addProduct()
    }*/

  /*  fun addProduct() {
        viewModelScope.launch {
            addProductRepository.addProduct().collect {
                addProductList.value = it
            }
        }
    }*/


    val priceOptions: List<String> = listOf(
        "Entre $5.000 y $10.000", "Entre $11.000 y $30.000", "Entre $31.000 y $50.000",
        "Entre $51.000 y $100.000", "Mas de $100.000"
    )

    val regionOptions: List<String> = (
            REGIONES_CHILE
            )

    val typeOptions: List<String> = (
            MATERIALES
            )

    val materialOptions: List<String> = (
            MATERIALES
            )


    //Listas de categorias de meds, esto es del negocio no de la pantalla, por ende va al viewModel::
    /*    val orales: List<String> =
            listOf("ABACAVIR", "ACETAMINOFÉN", "Ácido ACETILSALICÍLICO", "ACICLOVIR")

        //Lista 3 de indices economicos disponibles internacionales, esto es del negocio no de la pantalla, por ende va al viewModel::
        val pomadas: List<String> =
            listOf("Voltadol Forte", "Zovicrem", "Blastoestimulina", "Traumeel S", "Radio Salil")

        val opticos: List<String> = listOf("neomicina", "polimixina", "bacitracina")

        val intravenosos: List<String> =
            listOf("Tylenol", "Epinefrina", "Ampicilina", "Anfotericina B", "Dexametasona")

        val intradermicos: List<String> = listOf(
            "Vacuna contra la hepatitis B",
            "Vacuna contra el tétanos",
            "Vacuna contra el neumococo"
        )*/
    var precios by mutableStateOf("")
    var regiones by mutableStateOf("")

    var types by mutableStateOf("")

    var materials by mutableStateOf("")


    /*    fun getMedsOptions(): List<String> {
            return when (medsType) {
                "Orales: comprimidos" -> orales
                //Si selecciono Orales: comprimidos me muestra opt de la Lista orales

                "Tópicos: pomadas" -> pomadas

                "Ópticos: gotas para los ojos" -> opticos

                "Intravenosos o intramusculares: viales" -> intravenosos

                "Intradérmicos: insulina" -> intradermicos

                //En otro caso es que no se selecciono nada:
                else -> emptyList()
            }

        }*/

    //Funciones cuando cambia cada valor de las var de las 3 listas:
    fun onPriceTypeChange(newPrice: String) {
        precios = newPrice
    }

    fun onRegionChange(newRegion: String) {
        regiones = newRegion
    }

    fun onTypeChange(newType: String) {
        types = newType
    }

    fun onMaterialChange(newMaterial: String) {
        materials = newMaterial
    }


}