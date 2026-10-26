package com.example.data.model

data class CartItem(
    val id: String,
    val product: Product,
    val quantity: Int = 1,
    val selectedColor: String? = null,
    val selectedOption: String? = null
) {
    val totalPriceFcfa: Long
        get() = product.priceFcfa * quantity
}
