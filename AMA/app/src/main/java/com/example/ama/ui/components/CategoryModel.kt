package com.example.ama.ui.components

import kotlinx.serialization.Serializable

@Serializable
enum class Subcategory {
    // TEXTIL
    GUANTES, CHALECOS, GORROS, CALCETINES, PONCHOS, MANTAS, AMIGURUMIS, TEXTIL_OTROS,

    // MADERA
    TALLADOS, UTENSILIOS_COCINA, DECORACION_MADERA, JUGUETES_MADERA, MARCOS_PORTARRETRATOS,
    LLAVEROS_ACCESORIOS_MADERA, MUEBLES_PEQUENOS, TABLAS_PICAR, MADERA_OTROS,

    // CERÁMICA
    TAZAS_MUGS, PLATOS_FUENTES, CUENCOS_BOWLS, JARRONES_VASIJAS, MACETEROS_CERAMICA,
    AZULEJOS_BALDOSAS, ESCULTURAS_CERAMICA, VAJILLA_SETS, CERAMICA_OTROS,

    // GREDA (rústica)
    OLLAS_CAZUELAS, JARROS_JARRAS, PLATOS_GREDA, PAILAS, MATES_VASOS, MACETEROS_GREDA,
    DECORACION_GREDA, GREDA_OTROS,

    // HILO (tejidos/arte con hilo)
    TAPICES_MACRAME, BORDADO, CARTERAS_BOLSOS, COJINES_DECORACION, POSAVASOS_INDIVIDUALES,
    ROPA_BEBE, HILO_AMIGURUMIS, HILO_OTROS,

    // PINTURA
    OLEO, ACRILICO, ACUARELA, ILUSTRACION_DIBUJO, GRABADO, LAMINAS_PRINTS,
    CUADROS_LIENZO, MURAL_DECORATIVO, PINTURA_OTROS,

    // OTRO/GENÉRICO
    PERSONALIZADOS, SETS_REGALOS, INSUMOS, OTROS
}

fun Subcategory.label(): String = when (this) {
    // TEXTIL
    Subcategory.GUANTES -> "Guantes"
    Subcategory.CHALECOS -> "Chalecos"
    Subcategory.GORROS -> "Gorros"
    Subcategory.CALCETINES -> "Calcetines"
    Subcategory.PONCHOS -> "Ponchos"
    Subcategory.MANTAS -> "Mantas"
    Subcategory.AMIGURUMIS -> "Amigurumis"
    Subcategory.TEXTIL_OTROS -> "Otros"

    // MADERA
    Subcategory.TALLADOS -> "Tallados"
    Subcategory.UTENSILIOS_COCINA -> "Utensilios de cocina"
    Subcategory.DECORACION_MADERA -> "Decoración"
    Subcategory.JUGUETES_MADERA -> "Juguetes"
    Subcategory.MARCOS_PORTARRETRATOS -> "Marcos / Portarretratos"
    Subcategory.LLAVEROS_ACCESORIOS_MADERA -> "Llaveros / Accesorios"
    Subcategory.MUEBLES_PEQUENOS -> "Muebles pequeños"
    Subcategory.TABLAS_PICAR -> "Tablas para picar"
    Subcategory.MADERA_OTROS -> "Otros"

    // CERÁMICA
    Subcategory.TAZAS_MUGS -> "Tazas / Mugs"
    Subcategory.PLATOS_FUENTES -> "Platos / Fuentes"
    Subcategory.CUENCOS_BOWLS -> "Cuencos / Bowls"
    Subcategory.JARRONES_VASIJAS -> "Jarrones / Vasijas"
    Subcategory.MACETEROS_CERAMICA -> "Maceteros"
    Subcategory.AZULEJOS_BALDOSAS -> "Azulejos / Baldosas"
    Subcategory.ESCULTURAS_CERAMICA -> "Esculturas"
    Subcategory.VAJILLA_SETS -> "Vajilla / Sets"
    Subcategory.CERAMICA_OTROS -> "Otros"

    // GREDA
    Subcategory.OLLAS_CAZUELAS -> "Ollas / Cazuelas"
    Subcategory.JARROS_JARRAS -> "Jarros / Jarras"
    Subcategory.PLATOS_GREDA -> "Platos / Fuentes"
    Subcategory.PAILAS -> "Pailas"
    Subcategory.MATES_VASOS -> "Mates / Vasos"
    Subcategory.MACETEROS_GREDA -> "Maceteros"
    Subcategory.DECORACION_GREDA -> "Decoración"
    Subcategory.GREDA_OTROS -> "Otros"

    // HILO
    Subcategory.TAPICES_MACRAME -> "Tapices / Macramé"
    Subcategory.BORDADO -> "Bordado"
    Subcategory.CARTERAS_BOLSOS -> "Carteras / Bolsos"
    Subcategory.COJINES_DECORACION -> "Cojines / Decoración"
    Subcategory.POSAVASOS_INDIVIDUALES -> "Posavasos / Individuales"
    Subcategory.ROPA_BEBE -> "Ropa bebé"
    Subcategory.HILO_AMIGURUMIS -> "Amigurumis"
    Subcategory.HILO_OTROS -> "Otros"

    // PINTURA
    Subcategory.OLEO -> "Óleo"
    Subcategory.ACRILICO -> "Acrílico"
    Subcategory.ACUARELA -> "Acuarela"
    Subcategory.ILUSTRACION_DIBUJO -> "Ilustración / Dibujo"
    Subcategory.GRABADO -> "Grabado"
    Subcategory.LAMINAS_PRINTS -> "Láminas / Prints"
    Subcategory.CUADROS_LIENZO -> "Cuadros en lienzo"
    Subcategory.MURAL_DECORATIVO -> "Mural / Decorativo"
    Subcategory.PINTURA_OTROS -> "Otros"

    // OTROS
    Subcategory.PERSONALIZADOS -> "Personalizados"
    Subcategory.SETS_REGALOS -> "Sets / Regalos"
    Subcategory.INSUMOS -> "Insumos"
    Subcategory.OTROS -> "Otros"
}

val SUBCATS: Map<ProductType, List<Subcategory>> = mapOf(
    // TEXTIL (Lana)
    ProductType.TEXTIL to listOf(
        Subcategory.GUANTES, Subcategory.CHALECOS, Subcategory.GORROS, Subcategory.CALCETINES,
        Subcategory.PONCHOS, Subcategory.MANTAS, Subcategory.AMIGURUMIS, Subcategory.TEXTIL_OTROS
    ),

    // MADERA
    ProductType.MADERA to listOf(
        Subcategory.TALLADOS, Subcategory.UTENSILIOS_COCINA, Subcategory.TABLAS_PICAR,
        Subcategory.JUGUETES_MADERA, Subcategory.MARCOS_PORTARRETRATOS,
        Subcategory.LLAVEROS_ACCESORIOS_MADERA, Subcategory.MUEBLES_PEQUENOS,
        Subcategory.DECORACION_MADERA, Subcategory.MADERA_OTROS
    ),

    // CERÁMICA
    ProductType.CERAMICA to listOf(
        Subcategory.TAZAS_MUGS, Subcategory.PLATOS_FUENTES, Subcategory.CUENCOS_BOWLS,
        Subcategory.JARRONES_VASIJAS, Subcategory.MACETEROS_CERAMICA, Subcategory.VAJILLA_SETS,
        Subcategory.AZULEJOS_BALDOSAS, Subcategory.ESCULTURAS_CERAMICA, Subcategory.CERAMICA_OTROS
    ),

    // GREDA
    ProductType.GREDA to listOf(
        Subcategory.OLLAS_CAZUELAS, Subcategory.PLATOS_GREDA, Subcategory.PAILAS,
        Subcategory.JARROS_JARRAS, Subcategory.MATES_VASOS, Subcategory.MACETEROS_GREDA,
        Subcategory.DECORACION_GREDA, Subcategory.GREDA_OTROS
    ),

    // HILO
    ProductType.HILO to listOf(
        Subcategory.HILO_AMIGURUMIS, Subcategory.TAPICES_MACRAME, Subcategory.BORDADO,
        Subcategory.CARTERAS_BOLSOS, Subcategory.COJINES_DECORACION,
        Subcategory.POSAVASOS_INDIVIDUALES, Subcategory.ROPA_BEBE, Subcategory.HILO_OTROS
    ),

    // PINTURA
    ProductType.PINTURA to listOf(
        Subcategory.CUADROS_LIENZO, Subcategory.LAMINAS_PRINTS, Subcategory.ILUSTRACION_DIBUJO,
        Subcategory.ACRILICO, Subcategory.OLEO, Subcategory.ACUARELA,
        Subcategory.GRABADO, Subcategory.MURAL_DECORATIVO, Subcategory.PINTURA_OTROS
    ),

    // OTRO
    ProductType.OTRO to listOf(
        Subcategory.PERSONALIZADOS, Subcategory.SETS_REGALOS, Subcategory.INSUMOS, Subcategory.OTROS
    )
)
