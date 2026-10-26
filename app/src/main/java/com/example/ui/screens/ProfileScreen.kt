package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.ui.components.CitySelectionDialog
import com.example.ui.components.PachaBrandLogo
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MarketplaceViewModel
import com.example.ui.Screen
import com.example.ui.theme.PachaBorder
import com.example.ui.theme.PachaEmerald
import com.example.ui.theme.PachaEmeraldSoft
import com.example.ui.theme.PachaGold
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaTextPrimary
import com.example.ui.theme.PachaTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val products by viewModel.products.collectAsState()
    val favoritesCount = products.count { it.isFavorite }

    var showCityDialog by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PachaBrandLogo(logoSize = 28.dp, showTagline = false, titleColor = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• Mon Compte",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 17.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PachaOrange)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("profile_screen_list"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. User Header Profile Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PachaOrange),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(PachaGold, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = PachaOrange,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = userProfile.fullName,
                                        color = Color.White,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = userProfile.email,
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color.White.copy(alpha = 0.15f)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.WorkspacePremium,
                                                contentDescription = null,
                                                tint = PachaGold,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = userProfile.memberTier,
                                                color = PachaGold,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            // Bouton Modifier profil
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.22f),
                                modifier = Modifier
                                    .clickable { showEditProfileDialog = true }
                                    .testTag("edit_profile_btn")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Modifier",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Modifier",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Stats row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ProfileStatItem(
                                value = orders.size.toString(),
                                label = "Commandes",
                                onClick = { viewModel.navigateTo(Screen.Orders) }
                            )
                            ProfileStatItem(
                                value = favoritesCount.toString(),
                                label = "Favoris",
                                onClick = { viewModel.navigateTo(Screen.Favorites) }
                            )
                            ProfileStatItem(
                                value = "${userProfile.pachaPoints}",
                                label = "Points Pacha",
                                onClick = {}
                            )
                        }
                    }
                }
            }

            // 2. Console Administrateur Unique (Accès Réservé Propriétaire)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(Screen.AdminDashboard) }
                        .testTag("admin_portal_card"),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFEA580C), Color(0xFFC2410C))
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(PachaGold, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = PachaOrange,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Console Administrateur Unique",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = PachaGold
                                        ) {
                                            Text(
                                                text = "PROPRIÉTAIRE",
                                                color = PachaOrange,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Gestion des produits, stocks, commandes et ventes",
                                        fontSize = 11.sp,
                                        color = Color(0xFFCBD5E1)
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // 2.5 Room Database Product Catalog Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(Screen.ProductCatalog) }
                        .testTag("room_catalog_entry"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(PachaEmeraldSoft, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = PachaEmerald,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Catalogue Local Room (FCFA)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = PachaTextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = PachaEmeraldSoft
                                    ) {
                                        Text(
                                            text = "ROOM SQLITE",
                                            color = PachaEmerald,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Base Room : Noms, Prix en FCFA, descriptions et CRUD",
                                    fontSize = 11.sp,
                                    color = PachaTextSecondary
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = PachaTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // 3. User Preferences & Settings Group
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        ProfileMenuRow(
                            icon = Icons.Default.LocationOn,
                            title = "Zone de livraison",
                            subtitle = "${userProfile.currentCity} (${userProfile.currentCountry})",
                            onClick = { showCityDialog = true }
                        )
                        HorizontalDivider(color = PachaBorder)
                        ProfileMenuRow(
                            icon = Icons.Default.ReceiptLong,
                            title = "Historique des commandes",
                            subtitle = "${orders.size} commande(s) enregistrée(s)",
                            onClick = { viewModel.navigateTo(Screen.Orders) }
                        )
                        HorizontalDivider(color = PachaBorder)
                        ProfileMenuRow(
                            icon = Icons.Default.Favorite,
                            title = "Mes articles favoris",
                            subtitle = "$favoritesCount produit(s) sauvegardé(s)",
                            onClick = { viewModel.navigateTo(Screen.Favorites) }
                        )
                        HorizontalDivider(color = PachaBorder)
                        ProfileMenuRow(
                            icon = Icons.Default.AccountBalanceWallet,
                            title = "Moyens de paiement préparés",
                            subtitle = "Wave, Orange Money, MTN, Moov, Espèces",
                            onClick = { viewModel.navigateTo(Screen.PaymentMethods) }
                        )
                        HorizontalDivider(color = PachaBorder)
                        ProfileMenuRow(
                            icon = Icons.Default.Discount,
                            title = "Bons d'achat et coupons",
                            subtitle = "Code actif : PACHA10, BIENVENUE",
                            onClick = { viewModel.navigateTo(Screen.Cart) }
                        )
                        HorizontalDivider(color = PachaBorder)
                        ProfileMenuRow(
                            icon = Icons.Default.Notifications,
                            title = "Centre de notifications",
                            subtitle = "Suivi des commandes, livraisons et paiements",
                            onClick = { viewModel.navigateTo(Screen.Notifications) }
                        )
                    }
                }
            }

            // 4. Support and About Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        ProfileMenuRow(
                            icon = Icons.AutoMirrored.Filled.Help,
                            title = "Assistance & Support Client",
                            subtitle = "Support 7j/7 par WhatsApp & Téléphone",
                            onClick = { showSupportDialog = true }
                        )
                        HorizontalDivider(color = PachaBorder)
                        ProfileMenuRow(
                            icon = Icons.Default.Security,
                            title = "Sécurité & Protection Acheteur",
                            subtitle = "Transactions sécurisées et garanties",
                            onClick = {}
                        )
                        HorizontalDivider(color = PachaBorder)
                        ProfileMenuRow(
                            icon = Icons.Default.Info,
                            title = "À propos de SERVICE PACHA",
                            subtitle = "Version 1.0.0 • Conçu pour Google Play Store",
                            onClick = {}
                        )
                    }
                }
            }

            // 5. Version label
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SERVICE PACHA Marketplace Android",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PachaOrange
                    )
                    Text(
                        text = "Édition Afrique de l'Ouest & Centrale • Prix en FCFA",
                        fontSize = 11.sp,
                        color = PachaTextSecondary
                    )
                }
            }
        }
    }

    // City Selection Dialog
    CitySelectionDialog(
        show = showCityDialog,
        currentCity = userProfile.currentCity,
        onDismiss = { showCityDialog = false },
        onCitySelected = { cityName, countryName ->
            viewModel.updateCity(cityName, countryName)
        }
    )

    // Edit Profile Information Dialog
    if (showEditProfileDialog) {
        var editName by remember(userProfile.fullName) { mutableStateOf(userProfile.fullName) }
        var editPhone by remember(userProfile.phone) { mutableStateOf(userProfile.phone) }
        var editEmail by remember(userProfile.email) { mutableStateOf(userProfile.email) }
        val defaultAddress = userProfile.addresses.firstOrNull()
        var editStreet by remember(defaultAddress?.street) { mutableStateOf(defaultAddress?.street ?: "") }
        var editNeighborhood by remember(defaultAddress?.neighborhood) { mutableStateOf(defaultAddress?.neighborhood ?: "") }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = PachaOrange,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "Modifier mes informations",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nom complet") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Numéro de téléphone") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Adresse Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    OutlinedTextField(
                        value = editStreet,
                        onValueChange = { editStreet = it },
                        label = { Text("Adresse / Rue de livraison") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    OutlinedTextField(
                        value = editNeighborhood,
                        onValueChange = { editNeighborhood = it },
                        label = { Text("Quartier / Commune") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateUserProfileDetails(
                            fullName = editName,
                            phone = editPhone,
                            email = editEmail,
                            addressStreet = editStreet,
                            neighborhood = editNeighborhood
                        )
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PachaOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Enregistrer les modifications")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    // Customer Support Dialog
    if (showSupportDialog) {
        AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = null,
                    tint = PachaEmerald,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Assistance SERVICE PACHA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Notre équipe dédiée est à votre disposition 7j/7 pour le suivi de vos colis et retours.",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "📧 Email : servicepacha43@gmail.com", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    Text(text = "📞 Téléphone : +225 07 00 00 00 00", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    Text(text = "💬 WhatsApp : +225 05 00 00 00 00", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showSupportDialog = false }) {
                    Text("Fermer")
                }
            }
        )
    }
}

@Composable
private fun ProfileStatItem(
    value: String,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )
        Text(
            text = label,
            color = Color(0xFFCBD5E1),
            fontSize = 11.sp
        )
    }
}

@Composable
private fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PachaOrange,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = PachaTextSecondary
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(12.dp)
        )
    }
}
