package com.example.data.local.repository

import com.example.data.local.dao.CartDao
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository pour les opérations sur le panier Room de SERVICE PACHA.
 * Gère l'ajout, la modification des quantités, la suppression et le calcul réactif du total en FCFA.
 */
class CartRepository(
    private val cartDao: CartDao
) {
    /**
     * Flux réactif de tous les articles enregistrés dans le panier.
     */
    val allCartItems: Flow<List<CartItemEntity>> = cartDao.getAllCartItems()

    /**
     * Flux réactif du montant total du panier en FCFA (somme des prix unitaires * quantité).
     */
    val totalCartPriceFcfa: Flow<Long> = cartDao.getTotalPriceFcfa().map { it ?: 0L }

    /**
     * Flux réactif du nombre total d'articles dans le panier.
     */
    val totalItemsCount: Flow<Int> = cartDao.getTotalItemsCount().map { it ?: 0 }

    /**
     * Ajoute un produit (issu de ProductEntity) au panier Room.
     * Si le produit est déjà présent, incrémente sa quantité.
     */
    suspend fun addToCart(
        product: ProductEntity,
        quantity: Int = 1,
        selectedColor: String? = null,
        selectedOption: String? = null
    ) {
        val existing = cartDao.findItemByProductId(product.id)
        if (existing != null) {
            val updated = existing.copy(
                quantity = existing.quantity + quantity
            )
            cartDao.updateItem(updated)
        } else {
            val newItem = CartItemEntity.fromProduct(product, quantity, selectedColor, selectedOption)
            cartDao.insertItem(newItem)
        }
    }

    /**
     * Ajoute un produit (issu du modèle domaine Product) au panier Room.
     */
    suspend fun addToCart(
        product: Product,
        quantity: Int = 1,
        selectedColor: String? = null,
        selectedOption: String? = null
    ) {
        val existing = cartDao.findItemByProductId(product.id)
        if (existing != null) {
            val updated = existing.copy(
                quantity = existing.quantity + quantity
            )
            cartDao.updateItem(updated)
        } else {
            val newItem = CartItemEntity.fromDomain(product, quantity, selectedColor, selectedOption)
            cartDao.insertItem(newItem)
        }
    }

    /**
     * Met à jour la quantité d'un article du panier.
     * Si la quantité devient <= 0, supprime l'article.
     */
    suspend fun updateQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            cartDao.deleteById(cartItemId)
        } else {
            val item = cartDao.getCartItemById(cartItemId)
            if (item != null) {
                cartDao.updateItem(item.copy(quantity = newQuantity))
            }
        }
    }

    /**
     * Supprime un article spécifique du panier.
     */
    suspend fun removeFromCart(cartItemId: String) {
        cartDao.deleteById(cartItemId)
    }

    /**
     * Vide complètement le panier d'achat.
     */
    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
