package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.OrangeMoneyColor
import com.example.ui.theme.PachaEmerald
import com.example.ui.theme.PachaGold
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.WaveColor

enum class NotificationType(
    val categoryLabel: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBgColor: Color
) {
    NEW_ORDER(
        categoryLabel = "Commande",
        icon = Icons.Default.ShoppingBag,
        iconColor = PachaOrange,
        iconBgColor = PachaOrange.copy(alpha = 0.1f)
    ),
    PAYMENT_CONFIRMED(
        categoryLabel = "Paiement",
        icon = Icons.Default.Payment,
        iconColor = WaveColor,
        iconBgColor = WaveColor.copy(alpha = 0.12f)
    ),
    ORDER_PROCESSING(
        categoryLabel = "Préparation",
        icon = Icons.Default.ShoppingBag,
        iconColor = OrangeMoneyColor,
        iconBgColor = OrangeMoneyColor.copy(alpha = 0.12f)
    ),
    ORDER_SHIPPED(
        categoryLabel = "Expédition",
        icon = Icons.Default.LocalShipping,
        iconColor = PachaOrange,
        iconBgColor = PachaOrange.copy(alpha = 0.12f)
    ),
    ORDER_DELIVERING(
        categoryLabel = "Livraison en cours",
        icon = Icons.Default.LocalShipping,
        iconColor = PachaEmerald,
        iconBgColor = PachaEmerald.copy(alpha = 0.12f)
    ),
    ORDER_DELIVERED(
        categoryLabel = "Livrée",
        icon = Icons.Default.CheckCircle,
        iconColor = PachaEmerald,
        iconBgColor = PachaEmerald.copy(alpha = 0.15f)
    ),
    ORDER_CANCELLED(
        categoryLabel = "Annulée",
        icon = Icons.Default.Cancel,
        iconColor = Color(0xFFDC2626),
        iconBgColor = Color(0xFFDC2626).copy(alpha = 0.12f)
    ),
    PROMO_ALERT(
        categoryLabel = "Offre Spéciale",
        icon = Icons.Default.Notifications,
        iconColor = PachaGold,
        iconBgColor = PachaGold.copy(alpha = 0.15f)
    )
}

data class AppNotification(
    val id: String,
    val userId: String = "user_default",
    val orderId: String? = null,
    val orderNumber: String? = null,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestampFormatted: String,
    val isRead: Boolean = false
)
