package com.example.data.local.repository

import com.example.data.local.dao.ProductDao
import com.example.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

/**
 * Repository gérant l'accès aux données du catalogue de produits Room de SERVICE PACHA.
 * Fait l'intermédiaire entre le ProductDao et le ProductCatalogViewModel.
 */
class ProductCatalogRepository(
    private val productDao: ProductDao
) {

    /**
     * Flux réactif de tous les produits du catalogue.
     */
    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()

    /**
     * Recherche de produits par texte (nom ou description).
     */
    fun searchProducts(query: String): Flow<List<ProductEntity>> {
        return if (query.isBlank()) {
            productDao.getAllProducts()
        } else {
            productDao.searchProducts(query.trim())
        }
    }

    /**
     * Récupère un produit par son identifiant.
     */
    fun getProductById(id: String): Flow<ProductEntity?> {
        return productDao.getProductById(id)
    }

    /**
     * Récupère les produits d'une catégorie donnée (ex: Alimentaire, Électronique, Mode).
     */
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> {
        return productDao.getProductsByCategory(category)
    }

    /**
     * Récupère toutes les catégories distinctes.
     */
    fun getAllCategories(): Flow<List<String>> {
        return productDao.getAllCategories()
    }

    /**
     * Filtre les produits par tranche de prix en FCFA.
     */
    fun getProductsByPriceRange(minPrice: Long, maxPrice: Long): Flow<List<ProductEntity>> {
        return productDao.getProductsByPriceRange(minPrice, maxPrice)
    }

    /**
     * Produits triés par prix croissant.
     */
    fun getProductsSortedByPriceAsc(): Flow<List<ProductEntity>> {
        return productDao.getProductsSortedByPriceAsc()
    }

    /**
     * Produits triés par prix décroissant.
     */
    fun getProductsSortedByPriceDesc(): Flow<List<ProductEntity>> {
        return productDao.getProductsSortedByPriceDesc()
    }

    /**
     * Insère un nouveau produit dans la base Room.
     */
    suspend fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    /**
     * Met à jour un produit existant.
     */
    suspend fun updateProduct(product: ProductEntity) {
        productDao.updateProduct(product)
    }

    /**
     * Supprime un produit du catalogue.
     */
    suspend fun deleteProduct(product: ProductEntity) {
        productDao.deleteProduct(product)
    }

    /**
     * Supprime un produit par ID.
     */
    suspend fun deleteById(id: String) {
        productDao.deleteById(id)
    }

    /**
     * Vérifie si la table est vide et insère les données de base si nécessaire.
     */
    suspend fun ensureDataPopulated() {
        val count = productDao.getProductCount()
        if (count == 0) {
            com.example.data.local.PachaDatabase.populateInitialCatalog(productDao)
        }
    }
}
