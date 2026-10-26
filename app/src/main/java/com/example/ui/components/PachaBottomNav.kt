package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.PachaOrange

@Composable
fun PachaBottomNav(
    currentScreen: Screen,
    cartItemCount: Int,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color(0xFFF1F5F9)),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .testTag("bottom_nav_bar"),
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            // 1. Accueil (Home)
            val isHome = currentScreen is Screen.Home
            NavigationBarItem(
                selected = isHome,
                onClick = { onNavigate(Screen.Home) },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Accueil",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Accueil",
                        fontSize = 11.sp,
                        fontWeight = if (isHome) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PachaOrange,
                    selectedTextColor = PachaOrange,
                    indicatorColor = Color(0xFFFFEDE0), // warm Jumia-inspired soft orange pill
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B)
                ),
                modifier = Modifier.testTag("nav_item_home")
            )

            // 2. Rayon (Catégories de produits)
            val isCategories = currentScreen is Screen.Categories || currentScreen is Screen.CategoryProducts
            NavigationBarItem(
                selected = isCategories,
                onClick = { onNavigate(Screen.Categories) },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (isCategories) Icons.Filled.Category else Icons.Outlined.Category,
                        contentDescription = "Rayon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Rayon",
                        fontSize = 11.sp,
                        fontWeight = if (isCategories) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PachaOrange,
                    selectedTextColor = PachaOrange,
                    indicatorColor = Color(0xFFFFEDE0),
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B)
                ),
                modifier = Modifier.testTag("nav_item_categories")
            )

            // 3. Panier (Cart)
            val isCart = currentScreen is Screen.Cart
            NavigationBarItem(
                selected = isCart,
                onClick = { onNavigate(Screen.Cart) },
                alwaysShowLabel = true,
                icon = {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge(
                                    containerColor = Color(0xFFDC2626), // vibrant alert red
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = if (cartItemCount > 99) "99+" else cartItemCount.toString(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isCart) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                            contentDescription = "Panier",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = "Panier",
                        fontSize = 11.sp,
                        fontWeight = if (isCart) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PachaOrange,
                    selectedTextColor = PachaOrange,
                    indicatorColor = Color(0xFFFFEDE0),
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B)
                ),
                modifier = Modifier.testTag("nav_item_cart")
            )

            // 4. Commandes (Orders)
            val isOrders = currentScreen is Screen.Orders || currentScreen is Screen.OrderTracking
            NavigationBarItem(
                selected = isOrders,
                onClick = { onNavigate(Screen.Orders) },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (isOrders) Icons.Filled.Inventory2 else Icons.Outlined.Inventory2,
                        contentDescription = "Commandes",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Commandes",
                        fontSize = 11.sp,
                        fontWeight = if (isOrders) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PachaOrange,
                    selectedTextColor = PachaOrange,
                    indicatorColor = Color(0xFFFFEDE0),
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B)
                ),
                modifier = Modifier.testTag("nav_item_orders")
            )

            // 5. Compte (Profile)
            val isProfile = currentScreen is Screen.Profile ||
                    currentScreen is Screen.Auth ||
                    currentScreen is Screen.PaymentMethods ||
                    currentScreen is Screen.AdminDashboard ||
                    currentScreen is Screen.Favorites ||
                    currentScreen is Screen.Notifications
            NavigationBarItem(
                selected = isProfile,
                onClick = { onNavigate(Screen.Profile) },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (isProfile) Icons.Filled.Person else Icons.Outlined.Person,
                        contentDescription = "Compte",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Compte",
                        fontSize = 11.sp,
                        fontWeight = if (isProfile) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PachaOrange,
                    selectedTextColor = PachaOrange,
                    indicatorColor = Color(0xFFFFEDE0),
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B)
                ),
                modifier = Modifier.testTag("nav_item_profile")
            )
        }
    }
}
