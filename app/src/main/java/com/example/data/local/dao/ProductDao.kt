package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface DAO pour la gestion des produits dans la base de données Room.
 * Fournit des flux réactifs Flow pour l'affichage en temps réel dans Compose
 * et des fonctions suspendues pour les opérations CRUD.
 */
@Dao
interface ProductDao {

    /**
     * Récupère tous les produits du catalogue, triés par nom alphabétique.
     */
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    /**
     * Récupère un produit spécifique par son identifiant unique.
     */
    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: String): Flow<ProductEntity?>

    /**
     * Recherche synchrone / unique d'un produit par ID.
     */
    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun findProductById(id: String): ProductEntity?

    /**
     * Recherche de produits par mot-clé dans le nom ou la description.
     */
    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    /**
     * Filtre les produits par catégorie (ex: Alimentaire, Électronique, Mode).
     */
    @Query("SELECT * FROM products WHERE category = :category OR categoryId = :category ORDER BY name ASC")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    /**
     * Récupère la liste de toutes les catégories distinctes présentes dans la base Room.
     */
    @Query("SELECT DISTINCT category FROM products ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>

    /**
     * Filtre les produits par tranche de prix en FCFA.
     */
    @Query("SELECT * FROM products WHERE priceFcfa BETWEEN :minPrice AND :maxPrice ORDER BY priceFcfa ASC")
    fun getProductsByPriceRange(minPrice: Long, maxPrice: Long): Flow<List<ProductEntity>>

    /**
     * Récupère les produits triés par prix croissant (du moins cher au plus cher).
     */
    @Query("SELECT * FROM products ORDER BY priceFcfa ASC")
    fun getProductsSortedByPriceAsc(): Flow<List<ProductEntity>>

    /**
     * Récupère les produits triés par prix décroissant (du plus cher au moins cher).
     */
    @Query("SELECT * FROM products ORDER BY priceFcfa DESC")
    fun getProductsSortedByPriceDesc(): Flow<List<ProductEntity>>

    /**
     * Insère un nouveau produit ou met à jour un produit existant.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    /**
     * Insère une liste de produits en une seule transaction.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    /**
     * Met à jour un produit existant.
     */
    @Update
    suspend fun updateProduct(product: ProductEntity)

    /**
     * Supprime un produit du catalogue.
     */
    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    /**
     * Supprime un produit par son identifiant.
     */
    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * Retourne le nombre total de produits enregistrés dans le catalogue.
     */
    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    /**
     * Vide complètement la table des produits.
     */
    @Query("DELETE FROM products")
    suspend fun clearAll()
}
