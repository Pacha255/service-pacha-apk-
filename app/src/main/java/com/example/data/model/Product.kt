package com.example.data.model

enum class StockAvailability(val label: String, val colorHex: Long) {
    IN_STOCK("En stock", 0xFF059669),
    LIMITED_STOCK("Stock limité", 0xFFD97706),
    OUT_OF_STOCK("Rupture de stock", 0xFFDC2626)
}

data class Product(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val priceFcfa: Long = 0,
    val originalPriceFcfa: Long = 0,
    val categoryId: String = "",
    val brand: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val stockCount: Int = 0,
    val isFlashSale: Boolean = false,
    val isPopular: Boolean = false,
    val isOfficialStore: Boolean = false,
    val deliveryEstimateDays: Int = 2,
    val features: List<String> = emptyList(),
    val availableColors: List<String> = emptyList(),
    val availableOptions: List<String> = emptyList(), // e.g. "128 Go", "256 Go" or "M", "L", "XL"
    val sellerName: String = "SERVICE PACHA Officiel",
    val sellerRating: Float = 4.8f,
    val warrantyInfo: String = "Garantie officielle 1 an",
    val isFavorite: Boolean = false
) {
    val discountPercent: Int
        get() = if (originalPriceFcfa > priceFcfa) {
            (((originalPriceFcfa - priceFcfa).toDouble() / originalPriceFcfa) * 100).toInt()
        } else 0

    val availability: StockAvailability
        get() = when {
            stockCount <= 0 -> StockAvailability.OUT_OF_STOCK
            stockCount <= 5 -> StockAvailability.LIMITED_STOCK
            else -> StockAvailability.IN_STOCK
        }

    val isAvailable: Boolean
        get() = stockCount > 0
}
