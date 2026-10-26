package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PredefinedCategories
import com.example.ui.MarketplaceViewModel
import com.example.ui.PriceFilter
import com.example.ui.Screen
import com.example.ui.SortOption
import com.example.ui.components.ProductCard
import com.example.ui.theme.PachaGold
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    initialQuery: String,
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf(initialQuery) }
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }
    var selectedPriceFilter by remember { mutableStateOf(PriceFilter.ALL) }
    var selectedSortOption by remember { mutableStateOf(SortOption.POPULARITY) }
    var onlyInStock by remember { mutableStateOf(false) }
    var onlyFavorites by remember { mutableStateOf(false) }

    var showSortMenu by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val allProducts by viewModel.products.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()

    val filteredProducts = remember(
        allProducts,
        query,
        selectedCategoryId,
        selectedPriceFilter,
        selectedSortOption,
        onlyInStock,
        onlyFavorites
    ) {
        val filtered = allProducts.filter { product ->
            val matchesQuery = query.isBlank() ||
                    product.title.contains(query, ignoreCase = true) ||
                    product.brand.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    (query.equals("promo", ignoreCase = true) && product.isFlashSale)

            val matchesCategory = selectedCategoryId == null || product.categoryId == selectedCategoryId
            val matchesPrice = product.priceFcfa in selectedPriceFilter.minFcfa..selectedPriceFilter.maxFcfa
            val matchesStock = !onlyInStock || product.isAvailable
            val matchesFav = !onlyFavorites || product.isFavorite

            matchesQuery && matchesCategory && matchesPrice && matchesStock && matchesFav
        }

        when (selectedSortOption) {
            SortOption.POPULARITY -> filtered.sortedByDescending { if (it.isPopular) 1 else 0 }
            SortOption.PRICE_ASC -> filtered.sortedBy { it.priceFcfa }
            SortOption.PRICE_DESC -> filtered.sortedByDescending { it.priceFcfa }
            SortOption.DISCOUNT -> filtered.sortedByDescending { it.discountPercent }
            SortOption.RATING -> filtered.sortedByDescending { it.rating }
        }
    }

    val popularSearchTags = listOf("Samsung", "iPhone", "Bazin", "Airfryer", "TV LG", "Solaire", "PS5", "Sneakers", "Riz")

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        topBar = {
            Surface(
                color = PachaOrange,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("search_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }

                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = {
                            Text("Rechercher un produit, marque...", color = Color(0xFF94A3B8), fontSize = 13.sp)
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Effacer", tint = PachaOrange)
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { 
                            viewModel.addRecentSearch(query)
                            focusManager.clearFocus() 
                        }),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = PachaGold,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("search_input_field")
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    // Sort Button
                    Box {
                        IconButton(
                            onClick = { showSortMenu = true },
                            modifier = Modifier.testTag("sort_menu_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Trier",
                                tint = PachaGold
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
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category filter chips row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategoryId == null,
                        onClick = { selectedCategoryId = null },
                        label = { Text("Tous rayons", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PachaOrange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(PredefinedCategories.allCategories) { cat ->
                    FilterChip(
                        selected = selectedCategoryId == cat.id,
                        onClick = {
                            selectedCategoryId = if (selectedCategoryId == cat.id) null else cat.id
                        },
                        label = { Text(cat.name, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PachaOrange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Price filter chips row (Filtre par prix)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = null,
                            tint = PachaTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Prix :",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PachaTextSecondary
                        )
                    }
                }

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

            // Secondary filters: En stock & Favoris toggles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = onlyInStock,
                        onClick = { onlyInStock = !onlyInStock },
                        label = { Text("En stock", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF059669),
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = onlyFavorites,
                        onClick = { onlyFavorites = !onlyFavorites },
                        leadingIcon = {
                            Icon(
                                imageVector = if (onlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        label = { Text("Favoris", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Red,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                // Reset filters if any active
                if (selectedCategoryId != null || selectedPriceFilter != PriceFilter.ALL || onlyInStock || onlyFavorites) {
                    Text(
                        text = "Réinitialiser",
                        fontSize = 11.sp,
                        color = PachaOrange,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                selectedCategoryId = null
                                selectedPriceFilter = PriceFilter.ALL
                                onlyInStock = false
                                onlyFavorites = false
                            }
                            .padding(4.dp)
                    )
                }
            }

            // Quick suggestion keywords if query is empty
            if (query.isEmpty() && selectedCategoryId == null && selectedPriceFilter == PriceFilter.ALL) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    
                    if (recentSearches.isNotEmpty()) {
                        Text(
                            text = "Recherches récentes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = PachaOrange
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(recentSearches) { tag ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = PachaOrange.copy(alpha = 0.1f),
                                    modifier = Modifier.clickable { query = tag }
                                ) {
                                    Text(
                                        text = tag,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = PachaOrange,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Text(
                        text = "Recherches populaires",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = PachaOrange
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(popularSearchTags) { tag ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable { query = tag }
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Results count and active sorting info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredProducts.size} article(s) trouvé(s)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PachaOrange
                )
                Text(
                    text = "Tri : ${selectedSortOption.label}",
                    fontSize = 11.sp,
                    color = PachaTextSecondary
                )
            }

            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = PachaTextSecondary,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Aucun produit ne correspond à ces critères",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Essayez d'ajuster votre fourchette de prix ou d'élargir la catégorie.",
                            fontSize = 12.sp,
                            color = PachaTextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                query = ""
                                selectedCategoryId = null
                                selectedPriceFilter = PriceFilter.ALL
                                onlyInStock = false
                                onlyFavorites = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PachaOrange),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Effacer tous les filtres", fontSize = 12.sp)
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                    modifier = Modifier.fillMaxSize().testTag("search_results_list")
                ) {
                    val chunked = filteredProducts.chunked(2)
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
