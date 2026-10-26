package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.ProductEntity
import com.example.ui.CatalogSortOption
import com.example.ui.ProductCatalogViewModel
import com.example.ui.components.PachaBrandLogo
import com.example.ui.theme.PachaBgLight
import com.example.ui.theme.PachaBorder
import com.example.ui.theme.PachaEmerald
import com.example.ui.theme.PachaEmeraldSoft
import com.example.ui.theme.PachaGreen
import com.example.ui.theme.PachaGreenDark
import com.example.ui.theme.PachaTextPrimary
import com.example.ui.theme.PachaTextSecondary
import com.example.ui.theme.PachaTextTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCatalogScreen(
    viewModel: ProductCatalogViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddDialog by remember { mutableStateOf(false) }
    var showCartDialog by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<ProductEntity?>(null) }
    var productToDelete by remember { mutableStateOf<ProductEntity?>(null) }
    var showSortMenu by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PachaBrandLogo(logoSize = 28.dp, showText = false)
                        Column {
                            Text(
                                text = "Catalogue Room (FCFA)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.White
                            )
                            Text(
                                text = "${uiState.totalProductsCount} articles • Service Pacha",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Bouton Panier Room avec badge réactif
                    IconButton(
                        onClick = { showCartDialog = true },
                        modifier = Modifier.testTag("room_cart_icon_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                if (uiState.cartCount > 0) {
                                    Badge(
                                        containerColor = Color(0xFFDC2626),
                                        contentColor = Color.White
                                    ) {
                                        Text("${uiState.cartCount}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Panier Room",
                                tint = Color.White
                            )
                        }
                    }

                    // Menu de tri
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Trier",
                                tint = Color.White
                            )
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            CatalogSortOption.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = option.label,
                                            fontWeight = if (uiState.sortOption == option) FontWeight.Bold else FontWeight.Normal,
                                            color = if (uiState.sortOption == option) PachaGreen else PachaTextPrimary
                                        )
                                    },
                                    onClick = {
                                        viewModel.onSortOptionSelected(option)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PachaGreen
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PachaGreen,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_product_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter un produit"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PachaBgLight)
        ) {
            // Bandeau d'info Panier Room si articles présents
            if (uiState.cartCount > 0) {
                Surface(
                    color = PachaEmeraldSoft,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCartDialog = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = PachaGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Panier actif : ${uiState.cartCount} article(s)",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = PachaGreenDark
                            )
                        }
                        Text(
                            text = "Total : ${viewModel.formatFcfa(uiState.cartTotalFcfa)} >",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = PachaGreenDark
                        )
                    }
                }
            }

            // Barre de recherche
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("catalog_search_input"),
                        placeholder = { Text("Rechercher nom, description, marque...", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = PachaGreen
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Effacer")
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Chips de filtrage rapide par catégorie (Alimentaire, Électronique, Mode, Électroménager)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.predefinedCategories) { cat ->
                            val isSelected = (cat == "Toutes" && uiState.selectedCategory == null) ||
                                    uiState.selectedCategory?.equals(cat, ignoreCase = true) == true
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.filterByCategory(if (cat == "Toutes") null else cat)
                                },
                                label = { Text(cat, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PachaGreen,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    // Statistique rapide
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${uiState.products.size} article(s) trouvé(s)",
                            fontSize = 12.sp,
                            color = PachaTextSecondary
                        )
                        if (uiState.averagePriceFcfa > 0) {
                            Text(
                                text = "Prix moyen: ${viewModel.formatFcfa(uiState.averagePriceFcfa)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PachaGreenDark
                            )
                        }
                    }
                }
            }

            // Liste des produits Room
            if (uiState.products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = PachaTextTertiary,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "Aucun produit trouvé",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = PachaTextPrimary
                        )
                        Text(
                            text = "Aucun article ne correspond à votre recherche ou la base Room est vide.",
                            fontSize = 13.sp,
                            color = PachaTextSecondary,
                            lineHeight = 18.sp
                        )
                        OutlinedButton(
                            onClick = { viewModel.resetToDefaultCatalog() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PachaGreen)
                        ) {
                            Icon(Icons.Default.Storage, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Recharger le catalogue par défaut")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.products, key = { it.id }) { product ->
                        ProductRoomItemCard(
                            product = product,
                            onEdit = { productToEdit = product },
                            onDelete = { productToDelete = product },
                            onAddToCart = { viewModel.addToCart(product, 1) }
                        )
                    }
                }
            }
        }
    }

    // Dialogue d'affichage et gestion du Panier Room
    if (showCartDialog) {
        RoomCartDialog(
            cartItems = cartItems,
            totalFcfa = uiState.cartTotalFcfa,
            formatFcfa = { viewModel.formatFcfa(it) },
            onUpdateQuantity = { id, qty -> viewModel.updateCartQuantity(id, qty) },
            onRemoveItem = { id -> viewModel.removeFromCart(id) },
            onClearCart = { viewModel.clearCart() },
            onDismiss = { showCartDialog = false }
        )
    }

    // Dialogue d'ajout de produit
    if (showAddDialog) {
        ProductFormDialog(
            title = "Ajouter un produit (Room)",
            initialName = "",
            initialPriceFcfa = "",
            initialOriginalPriceFcfa = "",
            initialDescription = "",
            initialBrand = "SERVICE PACHA",
            initialCategory = "Électronique",
            initialStock = "10",
            onDismiss = { showAddDialog = false },
            onConfirm = { name, price, origPrice, desc, brand, cat, stock ->
                viewModel.addProduct(
                    name = name,
                    priceFcfa = price,
                    originalPriceFcfa = origPrice,
                    description = desc,
                    brand = brand,
                    category = cat,
                    stockCount = stock
                )
                showAddDialog = false
            }
        )
    }

    // Dialogue de modification de produit
    productToEdit?.let { product ->
        ProductFormDialog(
            title = "Modifier le produit",
            initialName = product.name,
            initialPriceFcfa = product.priceFcfa.toString(),
            initialOriginalPriceFcfa = if (product.originalPriceFcfa > 0) product.originalPriceFcfa.toString() else "",
            initialDescription = product.description,
            initialBrand = product.brand,
            initialCategory = product.category,
            initialStock = product.stockCount.toString(),
            onDismiss = { productToEdit = null },
            onConfirm = { name, price, origPrice, desc, brand, cat, stock ->
                viewModel.updateProduct(
                    id = product.id,
                    name = name,
                    priceFcfa = price,
                    originalPriceFcfa = origPrice,
                    description = desc,
                    brand = brand,
                    category = cat,
                    stockCount = stock
                )
                productToEdit = null
            }
        )
    }

    // Dialogue de confirmation de suppression
    productToDelete?.let { product ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Supprimer de la base Room ?") },
            text = {
                Text("Voulez-vous vraiment supprimer « ${product.name} » (${product.formattedPrice()}) ? Cette action est irréversible.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProduct(product.id)
                        productToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Supprimer", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
private fun ProductRoomItemCard(
    product: ProductEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // En-tête : Catégorie + Marque + Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(PachaEmeraldSoft, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = product.category.ifEmpty { "Général" },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PachaEmerald
                        )
                    }
                    Text(
                        text = "• ${product.brand}",
                        fontSize = 12.sp,
                        color = PachaTextSecondary
                    )
                }

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Modifier",
                            tint = PachaGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Supprimer",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Nom du produit
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = PachaTextPrimary
            )

            // Prix en FCFA bien visible
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = product.formattedPrice(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = PachaGreen
                )

                if (product.originalPriceFcfa > product.priceFcfa) {
                    Text(
                        text = "${product.originalPriceFcfa} F",
                        fontSize = 12.sp,
                        color = PachaTextTertiary,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFDC2626), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-${product.discountPercent}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Description
            Text(
                text = product.description,
                fontSize = 13.sp,
                color = PachaTextSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            // Pied de carte : Stock et Bouton Ajouter au Panier
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (product.stockCount > 0) PachaEmerald else Color(0xFFDC2626),
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = if (product.stockCount > 0) "En stock (${product.stockCount})" else "Rupture de stock",
                        fontSize = 11.sp,
                        color = if (product.stockCount > 0) PachaEmerald else Color(0xFFDC2626),
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = onAddToCart,
                    enabled = product.stockCount > 0,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PachaGreen,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("add_to_cart_btn_${product.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ajouter", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Boîte de dialogue affichant le Panier Room de l'utilisateur avec calcul du montant total en FCFA.
 */
@Composable
private fun RoomCartDialog(
    cartItems: List<CartItemEntity>,
    totalFcfa: Long,
    formatFcfa: (Long) -> String,
    onUpdateQuantity: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onClearCart: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = PachaGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Panier Room (${cartItems.sumOf { it.quantity }})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                if (cartItems.isNotEmpty()) {
                    IconButton(onClick = onClearCart, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Vider le panier",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (cartItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.RemoveShoppingCart,
                                contentDescription = null,
                                tint = PachaTextTertiary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Votre panier Room est vide",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = PachaTextSecondary
                            )
                            Text(
                                text = "Ajoutez des articles depuis le catalogue ci-dessous.",
                                fontSize = 12.sp,
                                color = PachaTextTertiary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cartItems, key = { it.id }) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = PachaBgLight),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PachaBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.productName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = PachaTextPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${item.formattedUnitPrice()} × ${item.quantity}",
                                            fontSize = 11.sp,
                                            color = PachaTextSecondary
                                        )
                                        Text(
                                            text = item.formattedTotal(),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            color = PachaGreen
                                        )
                                    }

                                    // Contrôles de quantité (+ / -) et Suppression
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color.White,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, PachaBorder),
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clickable { onUpdateQuantity(item.id, item.quantity - 1) }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Remove,
                                                    contentDescription = "Moins",
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = "${item.quantity}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp)
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color.White,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, PachaBorder),
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clickable { onUpdateQuantity(item.id, item.quantity + 1) }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = "Plus",
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }

                                        IconButton(
                                            onClick = { onRemoveItem(item.id) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Supprimer",
                                                tint = Color(0xFFDC2626),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = PachaBorder, modifier = Modifier.padding(vertical = 4.dp))

                    // Calcul et affichage du total en FCFA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL DU PANIER :",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = PachaTextPrimary
                        )
                        Text(
                            text = formatFcfa(totalFcfa),
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = PachaGreen
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PachaGreen)
            ) {
                Text("Fermer", color = Color.White)
            }
        }
    )
}

@Composable
private fun ProductFormDialog(
    title: String,
    initialName: String,
    initialPriceFcfa: String,
    initialOriginalPriceFcfa: String,
    initialDescription: String,
    initialBrand: String,
    initialCategory: String,
    initialStock: String,
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        priceFcfa: Long,
        origPriceFcfa: Long,
        description: String,
        brand: String,
        categoryId: String,
        stockCount: Int
    ) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var priceFcfa by remember { mutableStateOf(initialPriceFcfa) }
    var origPriceFcfa by remember { mutableStateOf(initialOriginalPriceFcfa) }
    var description by remember { mutableStateOf(initialDescription) }
    var brand by remember { mutableStateOf(initialBrand) }
    var categoryId by remember { mutableStateOf(initialCategory) }
    var stockCount by remember { mutableStateOf(initialStock) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                errorMessage?.let {
                    Text(it, color = Color(0xFFDC2626), fontSize = 12.sp)
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom du produit *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Catégorie :", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PachaTextSecondary)
                val categoryOptions = listOf("Alimentaire", "Électronique", "Mode", "Électroménager")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categoryOptions) { cat ->
                        val isSelected = categoryId.equals(cat, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = { categoryId = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PachaGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = priceFcfa,
                        onValueChange = { priceFcfa = it.filter { char -> char.isDigit() } },
                        label = { Text("Prix (FCFA) *") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = origPriceFcfa,
                        onValueChange = { origPriceFcfa = it.filter { char -> char.isDigit() } },
                        label = { Text("Prix barré (FCFA)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description complète *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = brand,
                        onValueChange = { brand = it },
                        label = { Text("Marque") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = stockCount,
                        onValueChange = { stockCount = it.filter { char -> char.isDigit() } },
                        label = { Text("Stock") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedPrice = priceFcfa.toLongOrNull()
                    val parsedOrig = origPriceFcfa.toLongOrNull() ?: 0L
                    val parsedStock = stockCount.toIntOrNull() ?: 10

                    if (name.isBlank()) {
                        errorMessage = "Veuillez entrer un nom de produit."
                    } else if (parsedPrice == null || parsedPrice <= 0) {
                        errorMessage = "Veuillez entrer un prix en FCFA valide."
                    } else if (description.isBlank()) {
                        errorMessage = "Veuillez renseigner une description."
                    } else {
                        onConfirm(
                            name.trim(),
                            parsedPrice,
                            parsedOrig,
                            description.trim(),
                            brand.trim().ifEmpty { "SERVICE PACHA" },
                            categoryId,
                            parsedStock
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PachaGreen)
            ) {
                Text("Enregistrer", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
