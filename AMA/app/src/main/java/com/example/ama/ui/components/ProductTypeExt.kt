package com.example.ama.ui.components

fun ProductType.prettyLabel(): String = when (this) {
    ProductType.LANA   -> "Lana"
    ProductType.MADERA   -> "Madera"
    ProductType.CERAMICA -> "Cerámica"
    ProductType.GREDA    -> "Greda"
    ProductType.HILO     -> "Hilo"
    ProductType.PINTURA  -> "Pintura"
    ProductType.OTRO     -> "Otros"
}