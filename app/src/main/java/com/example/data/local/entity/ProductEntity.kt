package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Product
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

/**
 * Entité Room représentant un produit dans le catalogue local de SERVICE PACHA.
 * Contient le nom, le prix en Francs CFA (FCFA), la description complète,
 * et le champ 'category' (ex: Alimentaire, Électronique, Mode).
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val priceFcfa: Long,
    val description: String,
    val category: String = "Électronique", // Alimentaire, Électronique, Mode, Électroménager, etc.
    val originalPriceFcfa: Long = 0L,
    val categoryId: String = "general",
    val categoryName: String = "Général",
    val brand: String = "SERVICE PACHA",
    val stockCount: Int = 10,
    val isFlashSale: Boolean = false,
    val isPopular: Boolean = false,
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Formate le prix avec séparateur de milliers et devise FCFA (ex: "750 000 FCFA").
     */
    fun formattedPrice(): String {
        val formatter = NumberFormat.getNumberInstance(Locale.FRENCH)
        return "${formatter.format(priceFcfa)} FCFA"
    }

    /**
     * Pourcentage de réduction si un prix d'origine est spécifié.
     */
    val discountPercent: Int
        get() = if (originalPriceFcfa > priceFcfa && originalPriceFcfa > 0) {
            (((originalPriceFcfa - priceFcfa).toDouble() / originalPriceFcfa) * 100).toInt()
        } else 0

    /**
     * Convertit cette entité Room vers le modèle de domaine Product.
     */
    fun toDomainProduct(): Product {
        return Product(
            id = id,
            title = name,
            description = description,
            priceFcfa = priceFcfa,
            originalPriceFcfa = originalPriceFcfa,
            categoryId = categoryId,
            brand = brand,
            stockCount = stockCount,
            isFlashSale = isFlashSale,
            isPopular = isPopular,
            isOfficialStore = true,
            features = listOf(
                "Catégorie : $category",
                "Prix en FCFA garanti",
                "Fournisseur officiel SERVICE PACHA",
                "Garantie et service après-vente inclus"
            ),
            sellerName = "SERVICE PACHA Officiel"
        )
    }

    companion object {
        /**
         * Convertit un produit existant en entité Room.
         */
        fun fromDomainProduct(product: Product): ProductEntity {
            val detectedCategory = when (product.categoryId) {
                "smartphones", "tv_audio", "computing", "accessories" -> "Électronique"
                "appliances" -> "Électroménager"
                "fashion", "beauty" -> "Mode"
                "grocery" -> "Alimentaire"
                else -> "Électronique"
            }
            return ProductEntity(
                id = product.id.ifEmpty { UUID.randomUUID().toString() },
                name = product.title,
                priceFcfa = product.priceFcfa,
                description = product.description,
                category = detectedCategory,
                originalPriceFcfa = product.originalPriceFcfa,
                categoryId = product.categoryId,
                categoryName = detectedCategory,
                brand = product.brand.ifEmpty { "SERVICE PACHA" },
                stockCount = product.stockCount,
                isFlashSale = product.isFlashSale,
                isPopular = product.isPopular,
                isAvailable = product.isAvailable
            )
        }
    }
}
