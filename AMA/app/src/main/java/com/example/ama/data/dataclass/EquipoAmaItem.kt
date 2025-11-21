package com.example.ama.data.dataclass

import androidx.room.Entity

@Entity(tableName = "equipo")
data class EquipoAmaItem(
    val area: String,
    val lider: Boolean,
    val nombre: String,
    val rut: String
)