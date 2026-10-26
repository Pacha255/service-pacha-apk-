package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.PachaOrangeSoft
import com.example.ui.theme.PachaTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val product = viewModel.getProductById(productId)
    val userProfile by viewModel.userProfile.collectAsState()

    BackHandler {
        viewModel.navigateBack()
    }

    if (product == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Produit introuvable")
        }
        return
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    val shareProduct: () -> Unit = {
        val shareText = "Découvrez ${product.title} à ${MarketplaceRepository.formatFcfa(product.priceFcfa)} sur SERVICE PACHA !\nCommandez dès maintenant sur l'application."
        val sendIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = android.content.Intent.createChooser(sendIntent, "Partager le produit via")
        context.startActivity(shareIntent)
    }

    var selectedColor by remember {
        mutableStateOf(product.availableColors.firstOrNull())
    }
    var selectedOption by remember {
        mutableStateOf(product.availableOptions.firstOrNull())
    }
    var quantity by remember {
        mutableIntStateOf(1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = product.brand,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("detail_back_btn")
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
                        onClick = shareProduct,
                        modifier = Modifier.testTag("detail_share_icon_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Partager",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = { viewModel.toggleFavorite(product.id) },
                        modifier = Modifier.testTag("detail_fav_btn")
                    ) {
                        Icon(
                            imageVector = if (product.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = if (product.isFavorite) Color.Red else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PachaOrange)
            )
        },
        bottomBar = {
            // Fixed Bottom Actions
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Prix total",
                            fontSize = 11.sp,
                            color = PachaTextSecondary
                        )
                        Text(
                            text = MarketplaceRepository.formatFcfa(product.priceFcfa * quantity),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PachaOrange
                        )
                    }

                    // Add to Cart
                    OutlinedButton(
                        onClick = {
                            viewModel.addToCart(product, quantity, selectedColor, selectedOption)
                        },
                        modifier = Modifier.testTag("detail_add_cart_btn"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = PachaOrange
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Panier",
                            color = PachaOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Buy Now (Direct checkout)
                    Button(
                        onClick = {
                            viewModel.addToCart(product, quantity, selectedColor, selectedOption)
                            if (userProfile.isLoggedIn) {
                                viewModel.navigateTo(Screen.Checkout)
                            } else {
                                viewModel.navigateTo(Screen.Auth)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PachaOrange),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("detail_buy_now_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Acheter",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Visual Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                CategoryProductVisual(
                    categoryId = product.categoryId,
                    title = product.title,
                    modifier = Modifier.size(140.dp)
                )

                if (product.discountPercent > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = PachaOrange
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "-${product.discountPercent}% RÉDUCTION",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (product.isOfficialStore) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = PachaOrange
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = PachaGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Boutique Officielle Certifiée",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Main Details
            Column(modifier = Modifier.padding(16.dp)) {
                // Title
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rating and Reviews Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = PachaGold.copy(alpha = 0.15f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = PachaGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = String.format(java.util.Locale.US, "%.1f", product.rating),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = PachaOrange
                            )
                        }
                    }

                    Text(
                        text = "${product.reviewCount} avis vérifiés",
                        fontSize = 12.sp,
                        color = PachaTextSecondary
                    )

                    Text(text = "•", color = PachaTextSecondary)

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(product.availability.colorHex).copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = when (product.availability) {
                                com.example.data.model.StockAvailability.LIMITED_STOCK -> "Stock limité : ${product.stockCount} restants"
                                com.example.data.model.StockAvailability.OUT_OF_STOCK -> "Rupture temporaire"
                                com.example.data.model.StockAvailability.IN_STOCK -> "En stock (${product.stockCount} disponibles)"
                            },
                            fontSize = 11.sp,
                            color = Color(product.availability.colorHex),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Price Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = MarketplaceRepository.formatFcfa(product.priceFcfa),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PachaOrange
                            )
                            if (product.originalPriceFcfa > product.priceFcfa) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = MarketplaceRepository.formatFcfa(product.originalPriceFcfa),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        ),
                                        color = PachaTextSecondary,
                                        fontSize = 13.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Économisez ${MarketplaceRepository.formatFcfa(product.originalPriceFcfa - product.priceFcfa)}",
                                        color = PachaOrange,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = shareProduct,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("detail_share_btn")
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = PachaOrange, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Partager cet article (WhatsApp, etc.)", color = PachaOrange, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { viewModel.toggleFavorite(product.id) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("detail_favorite_btn")
                ) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (product.isFavorite) Color.Red else PachaOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (product.isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                        color = if (product.isFavorite) Color.Red else PachaOrange,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Color Selector if available
                if (product.availableColors.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Couleur disponible : ${selectedColor ?: ""}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        product.availableColors.forEach { color ->
                            val isSelected = selectedColor == color
                            Surface(
                                modifier = Modifier
                                    .clickable { selectedColor = color }
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) PachaOrange else PachaBorder,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) PachaOrange.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                            ) {
                                Text(
                                    text = color,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) PachaOrange else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Options/Capacity/Sizes Selector
                if (product.availableOptions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Option / Variante : ${selectedOption ?: ""}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        product.availableOptions.forEach { opt ->
                            val isSelected = selectedOption == opt
                            Surface(
                                modifier = Modifier
                                    .clickable { selectedOption = opt }
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) PachaOrange else PachaBorder,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) PachaOrangeSoft else MaterialTheme.colorScheme.surface
                            ) {
                                Text(
                                    text = opt,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) PachaOrange else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Quantity Row
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Quantité :",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Moins", modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = quantity.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        IconButton(
                            onClick = { if (quantity < product.stockCount) quantity++ },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Plus", modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = PachaBorder)
                Spacer(modifier = Modifier.height(16.dp))

                // Delivery & Guarantees Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = PachaEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Livraison Express en ${product.deliveryEstimateDays} jours ouvrés",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Disponible à Abidjan, Douala, Dakar et principales villes",
                                    fontSize = 11.sp,
                                    color = PachaTextSecondary
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = PachaOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = product.warrantyInfo,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Retours gratuits sous 7 jours en cas de non-conformité",
                                    fontSize = 11.sp,
                                    color = PachaTextSecondary
                                )
                            }
                        }
                    }
                }

                // Seller Information
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PachaEmeraldSoft)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = PachaEmerald,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = product.sellerName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PachaOrange
                                )
                                Text(
                                    text = "Note vendeur : ${product.sellerRating}/5 • Vendeur vérifié",
                                    fontSize = 11.sp,
                                    color = PachaEmerald
                                )
                            }
                        }
                    }
                }

                // Features Checklist
                if (product.features.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Points Forts & Caractéristiques",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    product.features.forEach { feat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = PachaEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = feat,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Description
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Description détaillée",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = product.description,
                    fontSize = 13.sp,
                    color = PachaTextSecondary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
