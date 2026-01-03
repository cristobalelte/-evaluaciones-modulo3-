package com.example.ama.core.dto

import ArtisanBannerUi

data class SellerProfileDto(
    val userId: String?,
    val storeName: String?,
    val description: String?,
    val imageUrl: String?,
    val contactPhone: String?,
    val regionId: String?,
    val commune: String?,
    val pickupAddress: String?,
    val artisanTitle: String?
)

data class ArtisanBanner(
    val name: String,
    val subtitle: String,
    val imageUrl: String
)
fun SellerProfileDto.toArtisanBannerUi(
    fullImageUrl: (String) -> String
): ArtisanBannerUi {
    val title = storeName?.takeIf { it.isNotBlank() } ?: "Artesano/a"
    val sub = artisanTitle?.takeIf { it.isNotBlank() }
        ?: commune?.takeIf { it.isNotBlank() }
        ?: ""

    val img = imageUrl?.takeIf { it.isNotBlank() }?.let(fullImageUrl) // si viene null, queda null

    return ArtisanBannerUi(
        id = userId?.toIntOrNull() ?: -1,
        title = title,
        subtitle = sub,
        imageUrl = img
    )
}
