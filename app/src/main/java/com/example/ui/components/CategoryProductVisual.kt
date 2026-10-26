package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CategoryProductVisual(
    categoryId: String,
    title: String,
    modifier: Modifier = Modifier
) {
    val (icon: ImageVector, bgTint: Color, iconColor: Color) = when (categoryId) {
        "smartphones" -> Triple(Icons.Default.PhoneAndroid, Color(0xFFFFF7ED), Color(0xFFEA580C))
        "computing" -> Triple(Icons.Default.Computer, Color(0xFFF0FDF4), Color(0xFF16A34A))
        "appliances" -> Triple(Icons.Default.Kitchen, Color(0xFFFFFBEB), Color(0xFFD97706))
        "tv_audio" -> Triple(Icons.Default.Tv, Color(0xFFFEF2F2), Color(0xFFDC2626))
        "fashion" -> Triple(Icons.Default.Checkroom, Color(0xFFFDF2F8), Color(0xFFDB2777))
        "beauty" -> Triple(Icons.Default.Spa, Color(0xFFFAF5FF), Color(0xFF9333EA))
        "groceries" -> Triple(Icons.Default.Restaurant, Color(0xFFECFDF5), Color(0xFF059669))
        "solar_brico" -> Triple(Icons.Default.WbSunny, Color(0xFFFFFBEB), Color(0xFFD97706))
        "gaming" -> Triple(Icons.Default.SportsEsports, Color(0xFFFFFBEB), Color(0xFFD97706))
        "auto_moto" -> Triple(Icons.Default.DirectionsCar, Color(0xFFF9FAFB), Color(0xFF374151))
        else -> Triple(Icons.Default.ShoppingBag, Color(0xFFFFF7ED), Color(0xFFEA580C))
    }

    Box(
        modifier = modifier.background(bgTint, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(36.dp)
        )
    }
}
