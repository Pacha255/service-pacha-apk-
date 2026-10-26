package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppNotification
import com.example.data.model.CartItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.PaymentGateway
import com.example.data.model.PaymentStatus
import com.example.data.model.Product
import com.example.data.model.SavedPaymentWallet
import com.example.data.model.TransactionReceipt
import com.example.data.model.UserProfile
import com.example.data.repository.MarketplaceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Screen {
    data object Home : Screen()
    data object Categories : Screen()
    data object Cart : Screen()
    data object Orders : Screen()
    data object Profile : Screen()
    data object Favorites : Screen()
    data object PaymentMethods : Screen()
    data object Notifications : Screen()
    data object AdminDashboard : Screen()
    data class ProductDetail(val productId: String) : Screen()
    data class CategoryProducts(val categoryId: String) : Screen()
    data class Search(val initialQuery: String = "") : Screen()
    data object Checkout : Screen()
    data object Auth : Screen()
    data object ProductCatalog : Screen()
    data class OrderTracking(val orderId: String) : Screen()
    data class PaymentReceipt(val orderId: String) : Screen()
}

enum class PriceFilter(val label: String, val minFcfa: Long, val maxFcfa: Long) {
    ALL("Tous les prix", 0L, Long.MAX_VALUE),
    UNDER_50K("< 50 000 F", 0L, 50000L),
    FROM_50K_TO_150K("50k - 150k F", 50000L, 150000L),
    FROM_150K_TO_300K("150k - 300k F", 150000L, 300000L),
    FROM_300K_TO_600K("300k - 600k F", 300000L, 600000L),
    ABOVE_600K("> 600 000 F", 600000L, Long.MAX_VALUE)
}

enum class SortOption(val label: String) {
    POPULARITY("Popularité"),
    PRICE_ASC("Prix croissant"),
    PRICE_DESC("Prix décroissant"),
    DISCOUNT("Meilleures remises"),
    RATING("Mieux notés")
}

class MarketplaceViewModel(
    val repository: MarketplaceRepository
) : ViewModel() {

    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
    val currentScreen: StateFlow<Screen> = _screenStack.combine(_screenStack) { stack, _ ->
        stack.lastOrNull() ?: Screen.Home
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Screen.Home)

    val products: StateFlow<List<Product>> = repository.products
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
    val orders: StateFlow<List<Order>> = repository.orders
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val appliedCoupon: StateFlow<String?> = repository.appliedCoupon
    val couponDiscountPercent: StateFlow<Int> = repository.couponDiscountPercent

    val cartCount: StateFlow<Int> = repository.cartItems.combine(repository.cartItems) { items, _ ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val cartSubtotalFcfa: StateFlow<Long> = repository.cartItems.combine(repository.cartItems) { items, _ ->
        items.sumOf { it.totalPriceFcfa }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0L)

    val favoriteProducts: StateFlow<List<Product>> = repository.products.combine(repository.products) { list, _ ->
        list.filter { it.isFavorite }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Flash sale countdown timer
    private val _flashSaleSecondsRemaining = MutableStateFlow(5 * 3600 + 42 * 60 + 18)
    val flashSaleSecondsRemaining: StateFlow<Int> = _flashSaleSecondsRemaining.asStateFlow()

    // Notification toast events
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    // Search and filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    fun addRecentSearch(query: String) {
        if (query.isBlank()) return
        _recentSearches.update { list ->
            (listOf(query) + list.filter { it != query }).take(5)
        }
    }

    private val _selectedFilterCategory = MutableStateFlow<String?>(null)
    val selectedFilterCategory: StateFlow<String?> = _selectedFilterCategory.asStateFlow()

    private val _selectedPriceFilter = MutableStateFlow(PriceFilter.ALL)
    val selectedPriceFilter: StateFlow<PriceFilter> = _selectedPriceFilter.asStateFlow()

    private val _selectedSortOption = MutableStateFlow(SortOption.POPULARITY)
    val selectedSortOption: StateFlow<SortOption> = _selectedSortOption.asStateFlow()

    private val _inStockOnly = MutableStateFlow(false)
    val inStockOnly: StateFlow<Boolean> = _inStockOnly.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_flashSaleSecondsRemaining.value > 0) {
                    _flashSaleSecondsRemaining.value -= 1
                } else {
                    _flashSaleSecondsRemaining.value = 12 * 3600
                }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        val current = _screenStack.value
        val isRootTab = screen is Screen.Home || screen is Screen.Categories ||
                screen is Screen.Cart || screen is Screen.Orders || screen is Screen.Profile

        if (isRootTab) {
            _screenStack.value = listOf(screen)
        } else {
            _screenStack.value = current + screen
        }
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        return if (current.size > 1) {
            _screenStack.value = current.dropLast(1)
            true
        } else {
            false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterCategory(categoryId: String?) {
        _selectedFilterCategory.value = categoryId
    }

    fun setPriceFilter(priceFilter: PriceFilter) {
        _selectedPriceFilter.value = priceFilter
    }

    fun setSortOption(sortOption: SortOption) {
        _selectedSortOption.value = sortOption
    }

    fun toggleInStockOnly() {
        _inStockOnly.value = !_inStockOnly.value
    }

    fun resetFilters() {
        _selectedFilterCategory.value = null
        _selectedPriceFilter.value = PriceFilter.ALL
        _selectedSortOption.value = SortOption.POPULARITY
        _inStockOnly.value = false
    }

    fun addToCart(product: Product, quantity: Int = 1, color: String? = null, option: String? = null) {
        repository.addToCart(product, quantity, color, option)
        viewModelScope.launch {
            _toastMessage.emit("« ${product.title.take(24)}... » ajouté au panier !")
        }
    }

    fun updateCartQuantity(cartItemId: String, newQuantity: Int) {
        repository.updateCartQuantity(cartItemId, newQuantity)
    }

    fun loginOrRegister(fullName: String, phone: String, password: String) {
        repository.loginOrRegister(fullName, phone, password)
        viewModelScope.launch {
            _toastMessage.emit("Connexion réussie ! Bienvenue, $fullName")
        }
    }

    fun removeFromCart(cartItemId: String) {
        repository.removeFromCart(cartItemId)
        viewModelScope.launch {
            _toastMessage.emit("Article retiré du panier")
        }
    }

    fun toggleFavorite(productId: String) {
        repository.toggleFavorite(productId)
        val prod = getProductById(productId)
        if (prod != null) {
            val isNowFav = !prod.isFavorite
            viewModelScope.launch {
                if (isNowFav) {
                    _toastMessage.emit("Ajouté à vos favoris ❤️")
                } else {
                    _toastMessage.emit("Retiré des favoris")
                }
            }
        }
    }

    fun applyCoupon(code: String) {
        val success = repository.applyCoupon(code)
        viewModelScope.launch {
            if (success) {
                _toastMessage.emit("Code promo « $code » appliqué (-${repository.couponDiscountPercent.value}%) !")
            } else {
                _toastMessage.emit("Code promo invalide. Essayez PACHA10 ou BIENVENUE")
            }
        }
    }

    fun removeCoupon() {
        repository.removeCoupon()
    }

    fun addAllFavoritesToCart() {
        val favs = favoriteProducts.value
        if (favs.isEmpty()) return
        favs.forEach { product ->
            repository.addToCart(product, 1, null, null)
        }
        viewModelScope.launch {
            _toastMessage.emit("Tous les articles favoris ont été ajoutés au panier !")
        }
    }

    // Payment Processing State
    private val _paymentStatus = MutableStateFlow(PaymentStatus.IDLE)
    val paymentStatus: StateFlow<PaymentStatus> = _paymentStatus.asStateFlow()

    private val _paymentProcessingMessage = MutableStateFlow("")
    val paymentProcessingMessage: StateFlow<String> = _paymentProcessingMessage.asStateFlow()

    private val _lastReceipt = MutableStateFlow<TransactionReceipt?>(null)
    val lastReceipt: StateFlow<TransactionReceipt?> = _lastReceipt.asStateFlow()

    fun processPaymentAndPlaceOrder(
        gateway: PaymentGateway,
        payerIdentifier: String,
        address: String,
        neighborhood: String,
        city: String,
        instructions: String = "",
        phone: String,
        recipientName: String
    ) {
        viewModelScope.launch {
            _paymentStatus.value = PaymentStatus.PROCESSING
            _paymentProcessingMessage.value = "Connexion sécurisée aux serveurs ${gateway.displayName}..."
            delay(900)
            
            _paymentProcessingMessage.value = when (gateway) {
                PaymentGateway.WAVE -> "Validation du paiement instantané Wave (0% de frais)..."
                PaymentGateway.ORANGE_MONEY -> "Vérification de l'autorisation Orange Money #144#..."
                PaymentGateway.MTN_MOMO -> "Envoi du prompt USSD MTN MoMo *133# au $payerIdentifier..."
                PaymentGateway.MOOV_MONEY -> "Validation du code de confirmation Moov Flooz..."
                PaymentGateway.CREDIT_CARD -> "Authentification 3D-Secure bancaire PCI-DSS..."
                PaymentGateway.CASH_ON_DELIVERY -> "Enregistrement de la commande avec règlement au coursier..."
            }
            delay(1100)

            val txnId = "TXN-${gateway.id.uppercase().take(4)}-${(100000..999999).random()}"
            val authCode = "AUTH-${(1000..9999).random()}"

            val items = cartItems.value
            val subtotal = items.sumOf { it.totalPriceFcfa }
            val discount = if (couponDiscountPercent.value > 0) (subtotal * couponDiscountPercent.value) / 100 else 0
            val shipping = if (subtotal > 150000 || subtotal == 0L) 0L else 2000L
            val total = subtotal - discount + shipping

            val order = repository.placeOrder(
                paymentMethod = gateway.displayName,
                address = address,
                neighborhood = neighborhood,
                city = city,
                instructions = instructions,
                phone = phone,
                recipientName = recipientName,
                transactionId = txnId,
                isPaid = gateway != PaymentGateway.CASH_ON_DELIVERY,
                paymentGatewayId = gateway.id
            )

            if (order != null) {
                val receipt = TransactionReceipt(
                    transactionId = txnId,
                    orderNumber = order.orderNumber,
                    paymentGateway = gateway,
                    amountFcfa = total,
                    payerPhoneOrCard = payerIdentifier,
                    timestampFormatted = "Aujourd'hui à ${java.text.SimpleDateFormat("HH:mm", java.util.Locale.FRANCE).format(java.util.Date())}",
                    status = if (gateway == PaymentGateway.CASH_ON_DELIVERY) "EN ATTENTE (LIVRAISON)" else "APPROUVÉ & DÉBITÉ",
                    authorizationCode = authCode
                )
                _lastReceipt.value = receipt
                _paymentStatus.value = PaymentStatus.SUCCESS
                _paymentProcessingMessage.value = ""

                _toastMessage.emit("Paiement validé avec succès (${gateway.displayName}) !")
                navigateTo(Screen.PaymentReceipt(order.id))
            } else {
                _paymentStatus.value = PaymentStatus.FAILED
                _toastMessage.emit("Échec : panier vide ou commande invalide")
            }
        }
    }

    fun placeOrder(
        paymentMethod: String,
        address: String,
        neighborhood: String,
        city: String,
        instructions: String = "",
        phone: String,
        recipientName: String
    ): Order? {
        val order = repository.placeOrder(paymentMethod, address, neighborhood, city, instructions, phone, recipientName)
        if (order != null) {
            viewModelScope.launch {
                _toastMessage.emit("Commande #${order.orderNumber} enregistrée avec succès !")
            }
            navigateTo(Screen.OrderTracking(order.id))
        }
        return order
    }

    fun updateCity(city: String, country: String) {
        repository.updateSelectedCity(city, country)
        viewModelScope.launch {
            _toastMessage.emit("Zone de livraison changée : $city ($country)")
        }
    }

    fun updateUserProfileDetails(fullName: String, phone: String, email: String, addressStreet: String, neighborhood: String) {
        repository.updateUserProfileDetails(fullName, phone, email, addressStreet, neighborhood)
        viewModelScope.launch {
            _toastMessage.emit("Profil SERVICE PACHA mis à jour avec succès !")
        }
    }

    fun getProductById(id: String): Product? {
        return products.value.find { it.id == id }
    }

    fun getOrderById(id: String): Order? {
        return orders.value.find { it.id == id }
    }

    // EXCLUSIVITÉ ADMINISTRATEUR UNIQUE : Fonctions d'administration
    val totalRevenueFcfa: StateFlow<Long> = orders.combine(orders) { list, _ ->
        list.sumOf { it.totalFcfa }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0L)

    val lowStockProductsCount: StateFlow<Int> = products.combine(products) { list, _ ->
        list.count { it.stockCount <= 5 }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    private fun isAdmin(): Boolean {
        return userProfile.value.email == "servicepacha43@gmail.com"
    }

    fun adminAddProduct(product: Product) {
        if (!isAdmin()) return
        repository.adminAddProduct(product)
        viewModelScope.launch {
            _toastMessage.emit("Produit « ${product.title.take(20)}... » ajouté au catalogue officiel !")
        }
    }

    fun adminUpdateProduct(product: Product) {
        if (!isAdmin()) return
        repository.adminUpdateProduct(product)
        viewModelScope.launch {
            _toastMessage.emit("Produit « ${product.title.take(20)}... » mis à jour avec succès !")
        }
    }

    fun adminDeleteProduct(productId: String) {
        if (!isAdmin()) return
        repository.adminDeleteProduct(productId)
        viewModelScope.launch {
            _toastMessage.emit("Article retiré du catalogue officiel.")
        }
    }

    fun adminUpdateStock(productId: String, newStock: Int) {
        if (!isAdmin()) return
        repository.adminUpdateStock(productId, newStock)
        viewModelScope.launch {
            _toastMessage.emit("Stock mis à jour : $newStock unité(s)")
        }
    }

    fun adminUpdateOrderStatus(orderId: String, newStatus: OrderStatus) {
        if (!isAdmin()) return
        repository.adminUpdateOrderStatus(orderId, newStatus)
        viewModelScope.launch {
            _toastMessage.emit("Statut de la commande mis à jour : ${newStatus.label}")
        }
    }

    // GESTION DES NOTIFICATIONS (Étape 1)
    val notifications: StateFlow<List<AppNotification>> = repository.notifications

    val unreadNotificationsCount: StateFlow<Int> = notifications.combine(notifications) { list, _ ->
        list.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    fun markNotificationAsRead(id: String) {
        repository.markNotificationAsRead(id)
    }

    fun markAllNotificationsAsRead() {
        repository.markAllNotificationsAsRead()
        viewModelScope.launch {
            _toastMessage.emit("Toutes les notifications sont marquées comme lues.")
        }
    }

    fun clearAllNotifications() {
        repository.clearAllNotifications()
        viewModelScope.launch {
            _toastMessage.emit("Historique des notifications effacé.")
        }
    }

    fun resetPaymentStatus() {
        _paymentStatus.value = PaymentStatus.IDLE
    }
}
