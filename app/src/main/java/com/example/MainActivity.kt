package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.repository.MarketplaceRepository
import com.example.ui.MarketplaceViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.components.CitySelectionDialog
import com.example.ui.components.PachaBottomNav
import com.example.ui.components.PachaTopBar
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.PaymentMethodsScreen
import com.example.ui.screens.PaymentReceiptScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.ProductCatalogViewModel
import com.example.ui.screens.ProductCatalogScreen
import androidx.compose.ui.platform.LocalContext
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaTextSecondary
import com.example.ui.theme.ServicePachaTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: MarketplaceViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MarketplaceViewModel(MarketplaceRepository(applicationContext)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ServicePachaTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MarketplaceViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val cartCount by viewModel.cartCount.collectAsState()
    val unreadNotifsCount by viewModel.unreadNotificationsCount.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val context = LocalContext.current
    val productCatalogViewModel: ProductCatalogViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = ProductCatalogViewModel.provideFactory(context)
    )

    val snackbarHostState = remember { SnackbarHostState() }
    var showTopCityDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = currentScreen !is Screen.Home) {
        viewModel.navigateBack()
    }

    // Listen to toasts from ViewModel
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // The 5-button bottom navigation bar is visible on all main application screens
    val showBottomNav = currentScreen !is Screen.Checkout &&
            currentScreen !is Screen.PaymentReceipt

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentScreen is Screen.Home) {
                PachaTopBar(
                    currentCity = userProfile.currentCity,
                    currentCountry = userProfile.currentCountry,
                    cartItemCount = cartCount,
                    unreadNotificationCount = unreadNotifsCount,
                    onCityClick = { showTopCityDialog = true },
                    onSearchClick = { viewModel.navigateTo(Screen.Search()) },
                    onCartClick = { viewModel.navigateTo(Screen.Cart) },
                    onNotificationClick = {
                        viewModel.navigateTo(Screen.Notifications)
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                PachaBottomNav(
                    currentScreen = currentScreen,
                    cartItemCount = cartCount,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> {
                    HomeScreen(viewModel = viewModel)
                }
                is Screen.Categories -> {
                    CategoriesScreen(viewModel = viewModel)
                }
                is Screen.CategoryProducts -> {
                    CategoriesScreen(
                        viewModel = viewModel,
                        initialSelectedCategoryId = screen.categoryId
                    )
                }
                is Screen.Cart -> {
                    CartScreen(viewModel = viewModel)
                }
                is Screen.Checkout -> {
                    CheckoutScreen(viewModel = viewModel)
                }
                is Screen.Orders -> {
                    OrdersScreen(viewModel = viewModel)
                }
                is Screen.OrderTracking -> {
                    OrderTrackingScreen(orderId = screen.orderId, viewModel = viewModel)
                }
                is Screen.Profile -> {
                    ProfileScreen(viewModel = viewModel)
                }
                is Screen.Auth -> {
                    AuthScreen(viewModel = viewModel)
                }
                is Screen.ProductDetail -> {
                    ProductDetailScreen(productId = screen.productId, viewModel = viewModel)
                }
                is Screen.Search -> {
                    SearchScreen(initialQuery = screen.initialQuery, viewModel = viewModel)
                }
                is Screen.Favorites -> {
                    FavoritesScreen(viewModel = viewModel)
                }
                is Screen.PaymentMethods -> {
                    PaymentMethodsScreen(viewModel = viewModel)
                }
                is Screen.PaymentReceipt -> {
                    PaymentReceiptScreen(orderId = screen.orderId, viewModel = viewModel)
                }
                is Screen.AdminDashboard -> {
                    AdminDashboardScreen(viewModel = viewModel)
                }
                is Screen.Notifications -> {
                    NotificationsScreen(viewModel = viewModel)
                }
                is Screen.ProductCatalog -> {
                    ProductCatalogScreen(
                        viewModel = productCatalogViewModel,
                        onBack = { viewModel.navigateBack() }
                    )
                }
            }
        }
    }

    CitySelectionDialog(
        show = showTopCityDialog,
        currentCity = userProfile.currentCity,
        onDismiss = { showTopCityDialog = false },
        onCitySelected = { city, country ->
            viewModel.updateCity(city, country)
        }
    )
}
