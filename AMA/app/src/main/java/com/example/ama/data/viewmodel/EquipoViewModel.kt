package com.example.ama.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.data.dataclass.EquipoAmaItem
import com.example.ama.data.repository.EquipoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EquipoViewModel: ViewModel() {
    private val equipoRepository = EquipoRepository()

    val equipoList = MutableStateFlow<List<EquipoAmaItem>>(emptyList())

    private var nextPage = 0

    init {
        getEquipoList()
    }

    fun getNextInteger() {
        nextPage++
        getEquipoList()
    }


    private fun getEquipoList() {
        viewModelScope.launch {
            equipoRepository.getEquipo().collect {
                equipoList.value = it
            }
        }
    }

}