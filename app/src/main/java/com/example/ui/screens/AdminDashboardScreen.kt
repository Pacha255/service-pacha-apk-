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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.data.model.PredefinedCategories
import com.example.data.model.Product
import com.example.data.repository.MarketplaceRepository
import com.example.ui.MarketplaceViewModel
import com.example.ui.Screen
import com.example.ui.components.CategoryProductVisual
import com.example.ui.theme.PachaBorder
import com.example.ui.theme.PachaEmerald
import com.example.ui.theme.PachaEmeraldSoft
import com.example.ui.theme.PachaGold
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val totalRevenue by viewModel.totalRevenueFcfa.collectAsState()
    val lowStockCount by viewModel.lowStockProductsCount.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Ventes", "Produits", "Stocks", "Commandes", "Rayons")

    var showAddProductDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var deletingProduct by remember { mutableStateOf<Product?>(null) }

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = PachaGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Administration Unique",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            text = "Propriétaire : servicepacha43@gmail.com",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("admin_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.ProductCatalog) },
                        modifier = Modifier.testTag("admin_open_room_catalog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = "Catalogue Room",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PachaOrange)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Security Invariant Banner
            Surface(
                color = Color(0xFF0F172A),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = PachaEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sécurisé par Firebase Firestore • Aucun vendeur tiers autorisé",
                        color = Color(0xFFE2E8F0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Top Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = PachaOrange,
                edgePadding = 12.dp
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            // Tab Content
            when (selectedTabIndex) {
                0 -> AdminOverviewTab(
                    totalRevenue = totalRevenue,
                    totalOrders = orders.size,
                    totalProducts = products.size,
                    lowStockCount = lowStockCount,
                    onNavigateToProducts = { selectedTabIndex = 1 },
                    onNavigateToOrders = { selectedTabIndex = 3 }
                )
                1 -> AdminProductsTab(
                    products = products,
                    onAddProduct = { showAddProductDialog = true },
                    onEditProduct = { editingProduct = it },
                    onDeleteProduct = { deletingProduct = it }
                )
                2 -> AdminStockTab(
                    products = products,
                    onUpdateStock = { product, newStock -> viewModel.adminUpdateStock(product.id, newStock) }
                )
                3 -> AdminOrdersTab(
                    orders = orders,
                    onUpdateStatus = { orderId, newStatus -> viewModel.adminUpdateOrderStatus(orderId, newStatus) }
                )
                4 -> AdminCategoriesTab()
            }
        }
    }

    // Modal: Ajouter un produit
    if (showAddProductDialog) {
        AdminProductFormDialog(
            product = null,
            onDismiss = { showAddProductDialog = false },
            onConfirm = { newProd ->
                viewModel.adminAddProduct(newProd)
                showAddProductDialog = false
            }
        )
    }

    // Modal: Modifier un produit
    editingProduct?.let { prod ->
        AdminProductFormDialog(
            product = prod,
            onDismiss = { editingProduct = null },
            onConfirm = { updatedProd ->
                viewModel.adminUpdateProduct(updatedProd)
                editingProduct = null
            }
        )
    }

    // Modal: Confirmer suppression
    deletingProduct?.let { prod ->
        AlertDialog(
            onDismissRequest = { deletingProduct = null },
            title = { Text("Supprimer l'article", fontWeight = FontWeight.Bold) },
            text = { Text("Êtes-vous sûr de vouloir supprimer définitivement « ${prod.title} » du catalogue officiel ?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.adminDeleteProduct(prod.id)
                        deletingProduct = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingProduct = null }) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
private fun AdminOverviewTab(
    totalRevenue: Long,
    totalOrders: Int,
    totalProducts: Int,
    lowStockCount: Int,
    onNavigateToProducts: () -> Unit,
    onNavigateToOrders: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PachaOrange)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = PachaGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Chiffre d'Affaires Réalisé", color = Color.White, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = MarketplaceRepository.formatFcfa(totalRevenue),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Ventes totales enregistrées sur la marketplace",
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = Modifier.weight(1f).clickable { onNavigateToOrders() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = PachaOrange, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "$totalOrders", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PachaOrange)
                        Text(text = "Commandes", fontSize = 11.sp, color = PachaTextSecondary)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f).clickable { onNavigateToProducts() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Inventory, contentDescription = null, tint = PachaOrange, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "$totalProducts", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PachaOrange)
                        Text(text = "Articles actifs", fontSize = 11.sp, color = PachaTextSecondary)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "$lowStockCount", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                        Text(text = "Stocks critiques", fontSize = 11.sp, color = PachaTextSecondary)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Règles d'Administration Plateforme", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    HorizontalDivider(color = PachaBorder)
                    Text(text = "• Modèle Commercial : Vente directe par SERVICE PACHA uniquement.", fontSize = 12.sp, color = PachaOrange)
                    Text(text = "• Sécurité des Produits : Aucun client ne peut publier d'annonce ni modifier les stocks.", fontSize = 12.sp, color = PachaOrange)
                    Text(text = "• Paiements : Wave, Orange Money, MTN MoMo, Moov, Carte, Espèces à la livraison.", fontSize = 12.sp, color = PachaOrange)
                }
            }
        }
    }
}

@Composable
private fun AdminProductsTab(
    products: List<Product>,
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onDeleteProduct: (Product) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Button(
                onClick = onAddProduct,
                colors = ButtonDefaults.buttonColors(containerColor = PachaOrange),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ajouter un nouveau produit officiel", fontWeight = FontWeight.Bold)
            }
        }

        items(products) { product ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryProductVisual(categoryId = product.categoryId, title = product.title, modifier = Modifier.size(44.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                        Text(text = "${MarketplaceRepository.formatFcfa(product.priceFcfa)} • Stock : ${product.stockCount}", fontSize = 11.sp, color = PachaOrange)
                        Text(text = "Rayon : ${product.categoryId}", fontSize = 10.sp, color = PachaTextSecondary)
                    }

                    IconButton(onClick = { onEditProduct(product) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Modifier", tint = PachaOrange, modifier = Modifier.size(20.dp))
                    }

                    IconButton(onClick = { onDeleteProduct(product) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStockTab(
    products: List<Product>,
    onUpdateStock: (Product, Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Ajustement instantané des stocks en entrepôt",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(products) { product ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = product.title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, maxLines = 1)
                        Text(
                            text = "${product.availability.label} (${product.stockCount} unités)",
                            fontSize = 11.sp,
                            color = Color(product.availability.colorHex),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { onUpdateStock(product, product.stockCount - 1) },
                            shape = CircleShape,
                            modifier = Modifier.size(32.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("-", fontWeight = FontWeight.Bold)
                        }

                        Text(
                            text = "${product.stockCount}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            fontSize = 13.sp
                        )

                        OutlinedButton(
                            onClick = { onUpdateStock(product, product.stockCount + 5) },
                            shape = CircleShape,
                            modifier = Modifier.size(32.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("+5", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminOrdersTab(
    orders: List<com.example.data.model.Order>,
    onUpdateStatus: (String, OrderStatus) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Toutes les commandes reçues (${orders.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        items(orders) { order ->
            var showStatusMenu by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Cde #${order.orderNumber}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Box {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PachaOrange,
                                modifier = Modifier.clickable { showStatusMenu = true }
                            ) {
                                Text(
                                    text = "${order.status.label} ▾",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = showStatusMenu,
                                onDismissRequest = { showStatusMenu = false }
                            ) {
                                OrderStatus.entries.forEach { st ->
                                    DropdownMenuItem(
                                        text = { Text(st.label) },
                                        onClick = {
                                            onUpdateStatus(order.id, st)
                                            showStatusMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Text(text = "Client : ${order.recipientName} • ${order.phoneNumber}", fontSize = 11.sp)
                    Text(text = "Adresse : ${order.deliveryAddress}, ${order.deliveryNeighborhood} (${order.deliveryCity})", fontSize = 11.sp, color = PachaTextSecondary)
                    if (order.deliveryInstructions.isNotBlank()) {
                        Text(text = "Instructions : ${order.deliveryInstructions}", fontSize = 11.sp, color = PachaOrange, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "Règlement : ${order.paymentMethodName} • ${MarketplaceRepository.formatFcfa(order.totalFcfa)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PachaOrange)
                }
            }
        }
    }
}

@Composable
private fun AdminCategoriesTab() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Les 10 rayons officiels configurés",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        items(PredefinedCategories.allCategories) { cat ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(36.dp).background(Color(cat.bannerColorHex).copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(cat.icon, contentDescription = null, tint = Color(cat.bannerColorHex), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = cat.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "Identifiant système : ${cat.id}", fontSize = 11.sp, color = PachaTextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminProductFormDialog(
    product: Product?,
    onDismiss: () -> Unit,
    onConfirm: (Product) -> Unit
) {
    var title by remember { mutableStateOf(product?.title ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var priceStr by remember { mutableStateOf(product?.priceFcfa?.toString() ?: "50000") }
    var originalPriceStr by remember { mutableStateOf(product?.originalPriceFcfa?.toString() ?: "60000") }
    var brand by remember { mutableStateOf(product?.brand ?: "SERVICE PACHA") }
    var stockStr by remember { mutableStateOf(product?.stockCount?.toString() ?: "10") }
    var selectedCategoryId by remember { mutableStateOf(product?.categoryId ?: "smartphones") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (product == null) "Ajouter un Produit" else "Modifier le Produit", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Nom du produit") }, singleLine = true)
                OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Marque") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = priceStr, onValueChange = { priceStr = it }, label = { Text("Prix FCFA") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = stockStr, onValueChange = { stockStr = it }, label = { Text("Stock") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, maxLines = 3)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceStr.toLongOrNull() ?: 50000L
                    val origPrice = originalPriceStr.toLongOrNull() ?: price
                    val stock = stockStr.toIntOrNull() ?: 10
                    val newProduct = Product(
                        id = product?.id ?: "prod_${System.currentTimeMillis()}",
                        title = title.ifBlank { "Nouveau Produit" },
                        description = description.ifBlank { "Description officielle SERVICE PACHA" },
                        priceFcfa = price,
                        originalPriceFcfa = origPrice,
                        categoryId = selectedCategoryId,
                        brand = brand.ifBlank { "SERVICE PACHA" },
                        rating = product?.rating ?: 5.0f,
                        reviewCount = product?.reviewCount ?: 1,
                        stockCount = stock,
                        isOfficialStore = true,
                        sellerName = "SERVICE PACHA Officiel"
                    )
                    onConfirm(newProduct)
                },
                colors = ButtonDefaults.buttonColors(containerColor = PachaOrange)
            ) {
                Text("Enregistrer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
