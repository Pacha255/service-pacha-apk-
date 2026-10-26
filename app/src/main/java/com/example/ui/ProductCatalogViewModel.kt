package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.PachaDatabase
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.repository.CartRepository
import com.example.data.local.repository.ProductCatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

/**
 * Options de tri pour le catalogue de produits.
 */
enum class CatalogSortOption(val label: String) {
    NAME_ASC("Nom (A-Z)"),
    PRICE_ASC("Prix croissant (FCFA)"),
    PRICE_DESC("Prix décroissant (FCFA)"),
    NEWEST("Plus récents")
}

/**
 * Filtres internes pour la composition réactive.
 */
private data class CatalogFilterParams(
    val query: String,
    val category: String?,
    val sort: CatalogSortOption,
    val minPrice: Long?,
    val maxPrice: Long?
)

/**
 * État de l'interface utilisateur pour la gestion du catalogue Room SERVICE PACHA.
 */
data class ProductCatalogUiState(
    val products: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val sortOption: CatalogSortOption = CatalogSortOption.NAME_ASC,
    val minPriceFcfa: Long? = null,
    val maxPriceFcfa: Long? = null,
    val userMessage: String? = null,
    val totalProductsCount: Int = 0,
    val averagePriceFcfa: Long = 0L,
    val cartCount: Int = 0,
    val cartTotalFcfa: Long = 0L
)

/**
 * ViewModel pour gérer le catalogue de produits et le panier d'achat Room de l'application SERVICE PACHA.
 * Gère les noms, prix en FCFA, descriptions, champ 'category' (Alimentaire, Électronique, Mode, etc.)
 * et le filtrage réactif des produits par catégorie.
 */
class ProductCatalogViewModel(
    private val repository: ProductCatalogRepository,
    private val cartRepository: CartRepository? = null
) : ViewModel() {

    val predefinedCategories = listOf(
        "Toutes",
        "Alimentaire",
        "Électronique",
        "Mode",
        "Électroménager"
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _sortOption = MutableStateFlow(CatalogSortOption.NAME_ASC)
    val sortOption = _sortOption.asStateFlow()

    private val _minPriceFcfa = MutableStateFlow<Long?>(null)
    private val _maxPriceFcfa = MutableStateFlow<Long?>(null)

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage = _userMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    // ==========================================
    // ÉTATS RÉACTIFS DU PANIER ROOM
    // ==========================================

    val cartItems: StateFlow<List<CartItemEntity>> = (cartRepository?.allCartItems
        ?: MutableStateFlow(emptyList()))
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val cartTotalFcfa: StateFlow<Long> = (cartRepository?.totalCartPriceFcfa
        ?: MutableStateFlow(0L))
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )

    val cartItemCount: StateFlow<Int> = (cartRepository?.totalItemsCount
        ?: MutableStateFlow(0))
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    init {
        viewModelScope.launch {
            repository.ensureDataPopulated()
        }
    }

    private val filterParamsFlow = combine(
        _searchQuery,
        _selectedCategory,
        _sortOption,
        _minPriceFcfa,
        _maxPriceFcfa
    ) { query, category, sort, minPrice, maxPrice ->
        CatalogFilterParams(query, category, sort, minPrice, maxPrice)
    }

    /**
     * Flux combiné de l'état UI, réagissant en temps réel aux données de la base Room
     * et aux filtres (catégorie sélectionnée, recherche, tri, tranches de prix en FCFA).
     */
    val uiState: StateFlow<ProductCatalogUiState> = combine(
        repository.allProducts,
        filterParamsFlow,
        cartItemCount,
        cartTotalFcfa,
        _userMessage
    ) { all, filters, count, total, msg ->
        var filtered = all

        // 1. Filtrage par recherche textuelle (nom, description ou catégorie)
        if (filters.query.isNotBlank()) {
            val q = filters.query.trim().lowercase(Locale.FRENCH)
            filtered = filtered.filter {
                it.name.lowercase(Locale.FRENCH).contains(q) ||
                        it.description.lowercase(Locale.FRENCH).contains(q) ||
                        it.category.lowercase(Locale.FRENCH).contains(q) ||
                        it.brand.lowercase(Locale.FRENCH).contains(q)
            }
        }

        // 2. Filtrage par catégorie (ex: Alimentaire, Électronique, Mode, Électroménager)
        val category = filters.category
        if (category != null && category.isNotBlank() &&
            !category.equals("all", ignoreCase = true) &&
            !category.equals("Toutes", ignoreCase = true)
        ) {
            filtered = filtered.filter {
                it.category.equals(category, ignoreCase = true) ||
                        it.categoryName.equals(category, ignoreCase = true) ||
                        it.categoryId.equals(category, ignoreCase = true)
            }
        }

        // 3. Filtrage par tranche de prix en FCFA
        val minPrice = filters.minPrice
        if (minPrice != null) {
            filtered = filtered.filter { it.priceFcfa >= minPrice }
        }
        val maxPrice = filters.maxPrice
        if (maxPrice != null) {
            filtered = filtered.filter { it.priceFcfa <= maxPrice }
        }

        // 4. Tri
        val sorted = when (filters.sort) {
            CatalogSortOption.NAME_ASC -> filtered.sortedBy { it.name.lowercase(Locale.FRENCH) }
            CatalogSortOption.PRICE_ASC -> filtered.sortedBy { it.priceFcfa }
            CatalogSortOption.PRICE_DESC -> filtered.sortedByDescending { it.priceFcfa }
            CatalogSortOption.NEWEST -> filtered.sortedByDescending { it.createdAt }
        }

        val totalCount = all.size
        val avgPrice = if (all.isNotEmpty()) all.map { it.priceFcfa }.average().toLong() else 0L

        ProductCatalogUiState(
            products = sorted,
            isLoading = false,
            searchQuery = filters.query,
            selectedCategory = filters.category,
            sortOption = filters.sort,
            minPriceFcfa = filters.minPrice,
            maxPriceFcfa = filters.maxPrice,
            userMessage = msg,
            totalProductsCount = totalCount,
            averagePriceFcfa = avgPrice,
            cartCount = count,
            cartTotalFcfa = total
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProductCatalogUiState(isLoading = true)
    )

    // ==========================================
    // FONCTION DE FILTRAGE PAR CATÉGORIE
    // ==========================================

    /**
     * Filtre les produits du catalogue pour afficher uniquement ceux d'une catégorie donnée.
     * Exemple : filterByCategory("Alimentaire"), filterByCategory("Électronique"), filterByCategory("Mode").
     * Si category vaut null ou "Toutes", réinitialise pour afficher tous les produits.
     */
    fun filterByCategory(category: String?) {
        if (category == null || category.isBlank() ||
            category.equals("Toutes", ignoreCase = true) ||
            category.equals("all", ignoreCase = true)
        ) {
            _selectedCategory.value = null
        } else {
            _selectedCategory.value = category.trim()
        }
    }

    /**
     * Alias pour compatibilité UI.
     */
    fun onCategorySelected(category: String?) {
        filterByCategory(category)
    }

    /**
     * Récupère directement un flux Flow filtré par catégorie depuis le repository Room.
     */
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> {
        return repository.getProductsByCategory(category)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSortOptionSelected(option: CatalogSortOption) {
        _sortOption.value = option
    }

    fun onPriceRangeFilter(min: Long?, max: Long?) {
        _minPriceFcfa.value = min
        _maxPriceFcfa.value = max
    }

    fun resetFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
        _sortOption.value = CatalogSortOption.NAME_ASC
        _minPriceFcfa.value = null
        _maxPriceFcfa.value = null
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    // ==========================================
    // GESTION DU PANIER ROOM
    // ==========================================

    fun addToCart(
        product: ProductEntity,
        quantity: Int = 1,
        selectedColor: String? = null,
        selectedOption: String? = null
    ) {
        if (cartRepository == null) return
        viewModelScope.launch {
            try {
                cartRepository.addToCart(product, quantity, selectedColor, selectedOption)
                _userMessage.value = "« ${product.name} » ajouté au panier (${formatFcfa(product.priceFcfa * quantity)})"
            } catch (e: Exception) {
                _userMessage.value = "Erreur lors de l'ajout au panier: ${e.localizedMessage}"
            }
        }
    }

    fun removeFromCart(cartItemId: String) {
        if (cartRepository == null) return
        viewModelScope.launch {
            try {
                cartRepository.removeFromCart(cartItemId)
                _userMessage.value = "Article retiré du panier."
            } catch (e: Exception) {
                _userMessage.value = "Erreur lors du retrait: ${e.localizedMessage}"
            }
        }
    }

    fun updateCartQuantity(cartItemId: String, newQuantity: Int) {
        if (cartRepository == null) return
        viewModelScope.launch {
            try {
                cartRepository.updateQuantity(cartItemId, newQuantity)
            } catch (e: Exception) {
                _userMessage.value = "Erreur de mise à jour: ${e.localizedMessage}"
            }
        }
    }

    fun clearCart() {
        if (cartRepository == null) return
        viewModelScope.launch {
            try {
                cartRepository.clearCart()
                _userMessage.value = "Le panier a été vidé."
            } catch (e: Exception) {
                _userMessage.value = "Erreur: ${e.localizedMessage}"
            }
        }
    }

    // ==========================================
    // OPÉRATIONS CRUD SUR LE CATALOGUE ROOM
    // ==========================================

    /**
     * Ajoute un produit dans la base de données Room avec son nom, prix en FCFA,
     * description et catégorie (ex: Alimentaire, Électronique, Mode).
     */
    fun addProduct(
        name: String,
        priceFcfa: Long,
        description: String,
        category: String = "Électronique",
        originalPriceFcfa: Long = 0L,
        brand: String = "SERVICE PACHA",
        stockCount: Int = 10,
        isFlashSale: Boolean = false,
        isPopular: Boolean = false
    ) {
        if (name.isBlank()) {
            _userMessage.value = "Erreur: Le nom du produit ne peut pas être vide."
            return
        }
        if (priceFcfa <= 0) {
            _userMessage.value = "Erreur: Le prix en FCFA doit être supérieur à zéro."
            return
        }

        viewModelScope.launch {
            try {
                val newProduct = ProductEntity(
                    id = "pacha_${UUID.randomUUID().toString().take(8)}",
                    name = name.trim(),
                    priceFcfa = priceFcfa,
                    description = description.trim(),
                    category = category.trim().ifEmpty { "Électronique" },
                    originalPriceFcfa = originalPriceFcfa,
                    categoryId = category.trim().lowercase(Locale.FRENCH),
                    categoryName = category.trim(),
                    brand = brand.trim().ifEmpty { "SERVICE PACHA" },
                    stockCount = stockCount,
                    isFlashSale = isFlashSale,
                    isPopular = isPopular,
                    isAvailable = stockCount > 0,
                    createdAt = System.currentTimeMillis()
                )
                repository.insertProduct(newProduct)
                _userMessage.value = "Produit « $name » ($category • ${formatFcfa(priceFcfa)}) ajouté avec succès !"
            } catch (e: Exception) {
                _userMessage.value = "Échec de l'ajout: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Met à jour un produit existant dans la base de données Room.
     */
    fun updateProduct(
        id: String,
        name: String,
        priceFcfa: Long,
        description: String,
        category: String = "Électronique",
        originalPriceFcfa: Long = 0L,
        brand: String = "SERVICE PACHA",
        stockCount: Int = 10,
        isFlashSale: Boolean = false,
        isPopular: Boolean = false
    ) {
        if (name.isBlank()) {
            _userMessage.value = "Erreur: Le nom du produit ne peut pas être vide."
            return
        }
        if (priceFcfa <= 0) {
            _userMessage.value = "Erreur: Le prix en FCFA doit être supérieur à zéro."
            return
        }

        viewModelScope.launch {
            try {
                val updatedProduct = ProductEntity(
                    id = id,
                    name = name.trim(),
                    priceFcfa = priceFcfa,
                    description = description.trim(),
                    category = category.trim().ifEmpty { "Électronique" },
                    originalPriceFcfa = originalPriceFcfa,
                    categoryId = category.trim().lowercase(Locale.FRENCH),
                    categoryName = category.trim(),
                    brand = brand.trim().ifEmpty { "SERVICE PACHA" },
                    stockCount = stockCount,
                    isFlashSale = isFlashSale,
                    isPopular = isPopular,
                    isAvailable = stockCount > 0
                )
                repository.updateProduct(updatedProduct)
                _userMessage.value = "Produit « $name » ($category) mis à jour avec succès !"
            } catch (e: Exception) {
                _userMessage.value = "Échec de la modification: ${e.localizedMessage}"
            }
        }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteById(id)
                _userMessage.value = "Produit supprimé du catalogue."
            } catch (e: Exception) {
                _userMessage.value = "Erreur lors de la suppression: ${e.localizedMessage}"
            }
        }
    }

    fun resetToDefaultCatalog() {
        viewModelScope.launch {
            try {
                repository.ensureDataPopulated()
                _userMessage.value = "Catalogue SERVICE PACHA rechargé."
            } catch (e: Exception) {
                _userMessage.value = "Erreur: ${e.localizedMessage}"
            }
        }
    }

    fun formatFcfa(amount: Long): String {
        val formatter = NumberFormat.getNumberInstance(Locale.FRENCH)
        return "${formatter.format(amount)} FCFA"
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val db = PachaDatabase.getDatabase(context.applicationContext)
                    val repository = ProductCatalogRepository(db.productDao())
                    val cartRepository = CartRepository(db.cartDao())
                    return ProductCatalogViewModel(repository, cartRepository) as T
                }
            }
        }
    }
}
