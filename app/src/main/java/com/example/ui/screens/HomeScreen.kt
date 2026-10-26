package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.PredefinedCategories
import com.example.data.model.Product
import com.example.ui.MarketplaceViewModel
import com.example.ui.Screen
import com.example.ui.SortOption
import com.example.ui.components.CategoryProductVisual
import com.example.ui.components.ProductCard
import com.example.ui.theme.PachaBorder
import com.example.ui.theme.PachaEmerald
import com.example.ui.theme.PachaGold
import com.example.ui.theme.PachaGoldDark
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaOrangeDark
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaOrangeSoft
import com.example.ui.theme.PachaTextSecondary

@Composable
fun HomeScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()
    val flashSaleSeconds by viewModel.flashSaleSecondsRemaining.collectAsState()
    val selectedSortOption by viewModel.selectedSortOption.collectAsState()

    val flashProducts = products.filter { it.isFlashSale }
    val popularProducts = products.filter { it.isPopular }
    var selectedFilterCategory by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Convert seconds to HH:MM:SS
    val hours = flashSaleSeconds / 3600
    val minutes = (flashSaleSeconds % 3600) / 60
    val seconds = flashSaleSeconds % 60
    val timerString = String.format(java.util.Locale.US, "%02dh : %02dm : %02ds", hours, minutes, seconds)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_scroll"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. Hero Promotional Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { viewModel.navigateTo(Screen.Search("promo")) }
                    .testTag("hero_promo_banner"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF97316)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFF6600), Color(0xFFEA580C))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.Black.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "🔥 JUSQU'À -40% DE RÉDUCTION",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Les Jours Fous SERVICE PACHA",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Smartphones, Mode, High-Tech et Électroménager au meilleur prix",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // 2. Categories Scrollable Row
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nos Rayons",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Voir tout",
                        color = PachaOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { viewModel.navigateTo(Screen.Categories) }
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(PredefinedCategories.allCategories) { category ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(74.dp)
                                .clickable {
                                    viewModel.navigateTo(Screen.CategoryProducts(category.id))
                                }
                                .testTag("cat_chip_${category.id}")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        color = Color(category.bannerColorHex).copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = category.name,
                                    tint = Color(category.bannerColorHex),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = category.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // 2.5. Promotions & Bons Plans Section
        item {
            val promoProducts = products.filter { it.discountPercent > 0 }
            if (promoProducts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PachaOrangeSoft
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = PachaOrange,
                                    modifier = Modifier.padding(6.dp).size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Promotions & Bons Plans",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Les meilleures réductions du moment",
                                    fontSize = 12.sp,
                                    color = PachaTextSecondary
                                )
                            }
                        }
                        Text(
                            text = "Voir tout",
                            color = PachaOrange,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { viewModel.navigateTo(Screen.Search("promo")) }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(promoProducts) { prod ->
                            ProductCard(
                                product = prod,
                                onProductClick = { viewModel.navigateTo(Screen.ProductDetail(prod.id)) },
                                onAddToCart = { viewModel.addToCart(prod) },
                                onToggleFavorite = { viewModel.toggleFavorite(prod.id) },
                                modifier = Modifier.width(170.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. Flash Sales Section (Ventes Flash) with Timer
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = PachaOrangeSoft
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(PachaOrange, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Ventes Flash",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = PachaOrange
                                )
                                Text(
                                    text = "Offres limitées dans le temps",
                                    fontSize = 11.sp,
                                    color = PachaTextSecondary
                                )
                            }
                        }

                        // Countdown Timer Pill
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PachaOrange
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = PachaGold,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = timerString,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Horizontal Flash Products
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(flashProducts) { prod ->
                            ProductCard(
                                product = prod,
                                onProductClick = { viewModel.navigateTo(Screen.ProductDetail(prod.id)) },
                                onAddToCart = { viewModel.addToCart(prod) },
                                onToggleFavorite = { viewModel.toggleFavorite(prod.id) },
                                modifier = Modifier.width(180.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. Secondary Lifestyle Promotion Banner
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.navigateTo(Screen.CategoryProducts("fashion")) }
                    .testTag("secondary_fashion_banner"),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.img_banner_lifestyle),
                        contentDescription = "Collection Mode Pacha",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xDD0F172A),
                                        Color(0x990F172A),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "MODE & ÉLÉGANCE",
                            color = PachaGold,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Bazin, Sneakers & Parfums",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Découvrir la collection",
                                    color = PachaOrange,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = PachaOrange,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Trust & Guarantees Strip
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TrustItem(
                        icon = Icons.Default.VerifiedUser,
                        title = "100% Authentique",
                        tint = PachaEmerald
                    )
                    TrustItem(
                        icon = Icons.Default.LocalShipping,
                        title = "Livraison Rapide",
                        tint = PachaOrange
                    )
                    TrustItem(
                        icon = Icons.Default.Security,
                        title = "Paiement Fiable",
                        tint = PachaGoldDark
                    )
                }
            }
        }

        // 6. Popular Products Grid Header & Filter Chips
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Produits Populaires",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Les meilleures ventes en Côte d'Ivoire & Afrique",
                            fontSize = 12.sp,
                            color = PachaTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Bar for Product List
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Rechercher un produit par nom...", fontSize = 13.sp) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = PachaOrange) },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Effacer")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("product_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Sort options chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SortOption.entries.filter { it != SortOption.POPULARITY }.forEach { sortOption ->
                        val isSelected = selectedSortOption == sortOption
                        Surface(
                            onClick = { viewModel.setSortOption(sortOption) },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) PachaOrange else MaterialTheme.colorScheme.surface,
                            border = if (isSelected) null else BorderStroke(1.dp, PachaBorder),
                        ) {
                            Text(
                                text = sortOption.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color.Black,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                val filterTabs = listOf(
                    null to "Tous",
                    "smartphones" to "Électronique",
                    "fashion" to "Mode",
                    "appliances" to "Maison",
                    "beauty" to "Beauté",
                    "groceries" to "Supermarché"
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterTabs) { (catId, label) ->
                        val isSelected = selectedFilterCategory == catId
                        Surface(
                            onClick = { selectedFilterCategory = catId },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) PachaOrange else MaterialTheme.colorScheme.surface,
                            border = if (isSelected) null else BorderStroke(1.dp, PachaBorder),
                            modifier = Modifier.testTag("filter_chip_$catId")
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color.Black,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // 7. Popular Products in Pairs (2 columns) - Filtered & Sorted
        val filteredCategoryProducts = if (selectedFilterCategory == null) {
            popularProducts
        } else {
            popularProducts.filter { it.categoryId == selectedFilterCategory }
        }

        val filteredProducts = if (searchQuery.isBlank()) {
            filteredCategoryProducts
        } else {
            filteredCategoryProducts.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.brand.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true)
            }
        }

        val sortedProducts = when (selectedSortOption) {
            SortOption.PRICE_ASC -> filteredProducts.sortedBy { it.priceFcfa }
            SortOption.PRICE_DESC -> filteredProducts.sortedByDescending { it.priceFcfa }
            else -> filteredProducts
        }
        val chunkedProducts = sortedProducts.chunked(2)
        items(chunkedProducts) { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProductCard(
                    product = pair[0],
                    onProductClick = { viewModel.navigateTo(Screen.ProductDetail(pair[0].id)) },
                    onAddToCart = { viewModel.addToCart(pair[0]) },
                    onToggleFavorite = { viewModel.toggleFavorite(pair[0].id) },
                    modifier = Modifier.weight(1f)
                )

                if (pair.size > 1) {
                    ProductCard(
                        product = pair[1],
                        onProductClick = { viewModel.navigateTo(Screen.ProductDetail(pair[1].id)) },
                        onAddToCart = { viewModel.addToCart(pair[1]) },
                        onToggleFavorite = { viewModel.toggleFavorite(pair[1].id) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun TrustItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tint: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
