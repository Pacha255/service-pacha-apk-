package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface DAO pour la gestion du panier dans la base de données Room.
 * Permet l'ajout, la modification des quantités, la suppression et le calcul du montant total en FCFA.
 */
@Dao
interface CartDao {

    /**
     * Observe tous les articles du panier en temps réel, ordonnés par date d'ajout.
     */
    @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    /**
     * Récupère un article spécifique du panier par son identifiant.
     */
    @Query("SELECT * FROM cart_items WHERE id = :id LIMIT 1")
    suspend fun getCartItemById(id: String): CartItemEntity?

    /**
     * Recherche si un produit donné est déjà présent dans le panier.
     */
    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun findItemByProductId(productId: String): CartItemEntity?

    /**
     * Insère ou remplace un article dans le panier.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartItemEntity)

    /**
     * Met à jour la quantité ou les options d'un article du panier.
     */
    @Update
    suspend fun updateItem(item: CartItemEntity)

    /**
     * Supprime un article du panier.
     */
    @Delete
    suspend fun deleteItem(item: CartItemEntity)

    /**
     * Supprime un article par son identifiant.
     */
    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * Supprime un produit du panier par son productId.
     */
    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteByProductId(productId: String)

    /**
     * Vide intégralement le panier.
     */
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    /**
     * Calcule automatiquement la somme totale en FCFA de tous les articles du panier (prix * quantité).
     */
    @Query("SELECT SUM(priceFcfa * quantity) FROM cart_items")
    fun getTotalPriceFcfa(): Flow<Long?>

    /**
     * Calcule le nombre total d'articles (somme des quantités).
     */
    @Query("SELECT SUM(quantity) FROM cart_items")
    fun getTotalItemsCount(): Flow<Int?>
}
