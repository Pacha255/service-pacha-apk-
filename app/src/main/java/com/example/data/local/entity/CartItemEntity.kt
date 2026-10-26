package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.CartItem
import com.example.data.model.Product
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

/**
 * Entité Room représentant un article dans le panier d'achat de SERVICE PACHA.
 * Stocke les détails du produit, la quantité sélectionnée, et permet le calcul du total en FCFA.
 */
@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val productName: String,
    val priceFcfa: Long,
    val originalPriceFcfa: Long = 0L,
    val quantity: Int = 1,
    val description: String = "",
    val categoryId: String = "general",
    val brand: String = "SERVICE PACHA",
    val selectedColor: String? = null,
    val selectedOption: String? = null,
    val addedAt: Long = System.currentTimeMillis()
) {
    /**
     * Calcule le total en FCFA pour cette ligne du panier (prix unitaire * quantité).
     */
    val totalPriceFcfa: Long
        get() = priceFcfa * quantity

    /**
     * Prix total formaté en FCFA (ex: "750 000 FCFA").
     */
    fun formattedTotal(): String {
        val formatter = NumberFormat.getNumberInstance(Locale.FRENCH)
        return "${formatter.format(totalPriceFcfa)} FCFA"
    }

    /**
     * Prix unitaire formaté en FCFA.
     */
    fun formattedUnitPrice(): String {
        val formatter = NumberFormat.getNumberInstance(Locale.FRENCH)
        return "${formatter.format(priceFcfa)} FCFA"
    }

    /**
     * Conversion vers le modèle de domaine CartItem.
     */
    fun toDomainCartItem(): CartItem {
        return CartItem(
            id = id,
            product = Product(
                id = productId,
                title = productName,
                description = description,
                priceFcfa = priceFcfa,
                originalPriceFcfa = originalPriceFcfa,
                categoryId = categoryId,
                brand = brand
            ),
            quantity = quantity,
            selectedColor = selectedColor,
            selectedOption = selectedOption
        )
    }

    companion object {
        fun fromProduct(
            product: ProductEntity,
            quantity: Int = 1,
            selectedColor: String? = null,
            selectedOption: String? = null
        ): CartItemEntity {
            return CartItemEntity(
                id = "cart_${product.id}",
                productId = product.id,
                productName = product.name,
                priceFcfa = product.priceFcfa,
                originalPriceFcfa = product.originalPriceFcfa,
                quantity = quantity,
                description = product.description,
                categoryId = product.categoryId,
                brand = product.brand,
                selectedColor = selectedColor,
                selectedOption = selectedOption,
                addedAt = System.currentTimeMillis()
            )
        }

        fun fromDomain(
            product: Product,
            quantity: Int = 1,
            selectedColor: String? = null,
            selectedOption: String? = null
        ): CartItemEntity {
            return CartItemEntity(
                id = "cart_${product.id}",
                productId = product.id,
                productName = product.title,
                priceFcfa = product.priceFcfa,
                originalPriceFcfa = product.originalPriceFcfa,
                quantity = quantity,
                description = product.description,
                categoryId = product.categoryId,
                brand = product.brand,
                selectedColor = selectedColor,
                selectedOption = selectedOption,
                addedAt = System.currentTimeMillis()
            )
        }
    }
}
