package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val itemCount: Int,
    val bannerColorHex: Long = 0xFF0F172A
)

object PredefinedCategories {
    val allCategories = listOf(
        Category(
            id = "smartphones",
            name = "Téléphonie",
            icon = Icons.Default.PhoneAndroid,
            itemCount = 240,
            bannerColorHex = 0xFF1E3A8A
        ),
        Category(
            id = "computing",
            name = "Informatique",
            icon = Icons.Default.Computer,
            itemCount = 135,
            bannerColorHex = 0xFF0369A1
        ),
        Category(
            id = "appliances",
            name = "Électroménager",
            icon = Icons.Default.Kitchen,
            itemCount = 98,
            bannerColorHex = 0xFF047857
        ),
        Category(
            id = "tv_audio",
            name = "TV & Audio",
            icon = Icons.Default.Tv,
            itemCount = 76,
            bannerColorHex = 0xFF7C2D12
        ),
        Category(
            id = "fashion",
            name = "Mode & Style",
            icon = Icons.Default.Checkroom,
            itemCount = 310,
            bannerColorHex = 0xFFBE185D
        ),
        Category(
            id = "beauty",
            name = "Beauté & Soins",
            icon = Icons.Default.Spa,
            itemCount = 180,
            bannerColorHex = 0xFF9333EA
        ),
        Category(
            id = "groceries",
            name = "Épicerie & Frais",
            icon = Icons.Default.Restaurant,
            itemCount = 145,
            bannerColorHex = 0xFF15803D
        ),
        Category(
            id = "solar_brico",
            name = "Énergie Solaire",
            icon = Icons.Default.WbSunny,
            itemCount = 64,
            bannerColorHex = 0xFFD97706
        ),
        Category(
            id = "gaming",
            name = "Gaming & Jeux",
            icon = Icons.Default.SportsEsports,
            itemCount = 82,
            bannerColorHex = 0xFF4338CA
        ),
        Category(
            id = "auto_moto",
            name = "Auto & Moto",
            icon = Icons.Default.DirectionsCar,
            itemCount = 52,
            bannerColorHex = 0xFF475569
        )
    )
}
