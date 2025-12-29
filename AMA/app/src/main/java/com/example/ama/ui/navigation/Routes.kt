package com.example.ama.ui.navigation

import com.example.ama.ui.Register.RolScreen
import com.example.ama.ui.components.ProductType


object Routes {
    const val HOME = "home"
    const val LOGIN = "login"
    //    navController.navigate("carritoList")
    const val CART = "cart"
//    const val CART = "carritoList"

    const val PUBLISH = "publish"
    const val CATALOG = "catalog"
    const val SETTINGS = "settings"
    const val DETAIL = "detail/{id}"
    const val EDITAR_PRODUCTO = "editarProducto/{id}"
    const val CATALOG_ARG = "catalog?type={type}&sub={sub}"
    const val SUBCATEGORY = "subcategory?category={category}"
    const val REGISTER = "register"
    const val START = "start"
    const val DATOS_ENVIO  = "datosEnvio"
    const val METODO_PAGO = "metodoPago"
    const val ROLE = "rolScreen"


    const val REGISTER_WITH_ROLE = "register?role={role}"


    fun registerWithRole(role: String): String = "register?role=$role"


}

enum class Subcategory {
    GUANTES, CHALECOS, GORROS, CALCETINES, PONCHOS, MANTAS, AMIGURUMIS, OTROS
}

fun Subcategory.label(): String = when (this) {
    Subcategory.GUANTES -> "Guantes"
    Subcategory.CHALECOS -> "Chalecos"
    Subcategory.GORROS -> "Gorros"
    Subcategory.CALCETINES -> "Calcetines"
    Subcategory.PONCHOS -> "Ponchos"
    Subcategory.MANTAS -> "Mantas"
    Subcategory.AMIGURUMIS -> "Amigurumis"
    Subcategory.OTROS -> "Otros"
}

val SUBCATS: Map<ProductType, List<Subcategory>> = mapOf(
    ProductType.LANA to listOf(
        Subcategory.GUANTES, Subcategory.CHALECOS, Subcategory.GORROS,
        Subcategory.CALCETINES, Subcategory.PONCHOS, Subcategory.MANTAS,
        Subcategory.AMIGURUMIS, Subcategory.OTROS
    )
    // agrega las de MADERA, CERAMICA, etc. cuando las tengas
)


