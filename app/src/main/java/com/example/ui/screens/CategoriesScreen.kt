package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PredefinedCategories
import com.example.ui.MarketplaceViewModel
import com.example.ui.PriceFilter
import com.example.ui.Screen
import com.example.ui.SortOption
import com.example.ui.components.PachaBrandLogo
import com.example.ui.components.ProductCard
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    viewModel: MarketplaceViewModel,
    initialSelectedCategoryId: String? = null,
    modifier: Modifier = Modifier
) {
    var selectedCategoryId by remember(initialSelectedCategoryId) {
        mutableStateOf(initialSelectedCategoryId)
    }

    var selectedPriceFilter by remember { mutableStateOf(PriceFilter.ALL) }
    var selectedSortOption by remember { mutableStateOf(SortOption.POPULARITY) }
    var onlyInStock by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    val products by viewModel.products.collectAsState()

    BackHandler(enabled = selectedCategoryId != null) {
        selectedCategoryId = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (selectedCategoryId != null) {
                        Column {
                            Text(
                                text = PredefinedCategories.allCategories.find { it.id == selectedCategoryId }?.name
                                    ?: "Rayon",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "SERVICE PACHA • Rayon officiel",
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PachaBrandLogo(logoSize = 28.dp, showTagline = false, titleColor = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "• Rayons",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 17.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (selectedCategoryId != null) {
                        IconButton(
                            onClick = { selectedCategoryId = null },
                            modifier = Modifier.testTag("cat_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Retour",
                                tint = Color.White
                            )
                        }
                    }
                },
                actions = {
                    if (selectedCategoryId != null) {
                        Box {
                            IconButton(onClick = { showSortMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = "Trier",
                                    tint = Color.White
                                )
                            }
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false }
                            ) {
                                SortOption.entries.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = option.label,
                                                fontWeight = if (selectedSortOption == option) FontWeight.Bold else FontWeight.Normal,
                                                color = if (selectedSortOption == option) PachaOrange else MaterialTheme.colorScheme.onSurface
                                            )
                                        },
                                        onClick = {
                                            selectedSortOption = option
                                            showSortMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PachaOrange)
            )
        }
    ) { paddingValues ->
        if (selectedCategoryId == null) {
            // Category Directory List
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .testTag("categories_list"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Explorez nos 10 rayons officiels",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Des milliers d'articles certifiés au meilleur prix en FCFA",
                        style = MaterialTheme.typography.bodySmall,
                        color = PachaTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(PredefinedCategories.allCategories) { category ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedCategoryId = category.id
                                selectedPriceFilter = PriceFilter.ALL
                            }
                            .testTag("category_card_${category.id}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            color = Color(category.bannerColorHex).copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = category.icon,
                                        contentDescription = category.name,
                                        tint = Color(category.bannerColorHex),
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = category.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "+${category.itemCount} produits disponibles",
                                        fontSize = 12.sp,
                                        color = PachaTextSecondary
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Ouvrir",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // Products inside Selected Category with Price and Stock Filters
            val categoryProducts = remember(products, selectedCategoryId, selectedPriceFilter, selectedSortOption, onlyInStock) {
                val filtered = products.filter {
                    it.categoryId == selectedCategoryId &&
                            it.priceFcfa in selectedPriceFilter.minFcfa..selectedPriceFilter.maxFcfa &&
                            (!onlyInStock || it.isAvailable)
                }
                when (selectedSortOption) {
                    SortOption.POPULARITY -> filtered.sortedByDescending { if (it.isPopular) 1 else 0 }
                    SortOption.PRICE_ASC -> filtered.sortedBy { it.priceFcfa }
                    SortOption.PRICE_DESC -> filtered.sortedByDescending { it.priceFcfa }
                    SortOption.DISCOUNT -> filtered.sortedByDescending { it.discountPercent }
                    SortOption.RATING -> filtered.sortedByDescending { it.rating }
                }
            }

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .testTag("category_products_list"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
            ) {
                item {
                    // Price filter row
                    Text(
                        text = "Filtrer par budget en FCFA :",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PachaTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(PriceFilter.entries) { priceRange ->
                            FilterChip(
                                selected = selectedPriceFilter == priceRange,
                                onClick = { selectedPriceFilter = priceRange },
                                label = { Text(priceRange.label, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PachaOrange,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${categoryProducts.size} article(s) disponible(s)",
                            fontWeight = FontWeight.SemiBold,
                            color = PachaOrange,
                            fontSize = 13.sp
                        )

                        FilterChip(
                            selected = onlyInStock,
                            onClick = { onlyInStock = !onlyInStock },
                            label = { Text("En stock", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF059669),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (categoryProducts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Aucun article dans cette tranche de prix",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Réinitialiser le filtre de prix",
                                    color = PachaOrange,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.clickable { selectedPriceFilter = PriceFilter.ALL }
                                )
                            }
                        }
                    }
                } else {
                    val chunked = categoryProducts.chunked(2)
                    items(chunked) { pair ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
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
        }
    }
}
