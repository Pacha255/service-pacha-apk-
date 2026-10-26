package com.example.data.repository

import com.example.data.model.AppNotification
import com.example.data.model.CartItem
import com.example.data.model.NotificationType
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.Product
import com.example.data.model.TrackingStep
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale

import android.content.Context
import com.example.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

class MarketplaceRepository(context: Context? = null) {
    private val db = FirebaseFirestore.getInstance()
    private val productsCollection = db.collection("products")

    private val initialProducts = listOf<Product>(
        Product(
            id = "p1",
            title = "Samsung Galaxy S24 Ultra 5G",
            description = "Écran Dynamic AMOLED 2X 6.8\" 120Hz, Processeur Snapdragon 8 Gen 3, S-Pen intégré, Quad Caméra 200 MP avec Zoom Optique 10x et IA Galaxy.",
            priceFcfa = 750000,
            originalPriceFcfa = 820000,
            categoryId = "smartphones",
            brand = "Samsung",
            rating = 4.9f,
            reviewCount = 142,
            stockCount = 18,
            isFlashSale = true,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 1,
            features = listOf("Processeur Snapdragon 8 Gen 3", "200 MP Camera IA", "S-Pen Intégré", "Charge Ultra Rapide 45W"),
            availableColors = listOf("Gris Titane", "Noir Titane", "Violet Titane"),
            availableOptions = listOf("256 Go", "512 Go", "1 To")
        ),
        Product(
            id = "p2",
            title = "Airfryer Friteuse sans Huile Philips XXL",
            description = "Technologie Rapid Air pour des fritures croustillantes sans huile ajoutée. Capacité 6.2L (1.4kg), écran tactile numérique avec 7 programmes prédéfinis.",
            priceFcfa = 85000,
            originalPriceFcfa = 110000,
            categoryId = "appliances",
            brand = "Philips",
            rating = 4.8f,
            reviewCount = 98,
            stockCount = 12,
            isFlashSale = true,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 2,
            features = listOf("Capacité XXL 6.2 Litres", "Technologie Fat Removal", "Application NutriU", "Nettoyage facile au lave-vaisselle"),
            availableColors = listOf("Noir Laqué & Cuivre", "Blanc & Argent"),
            availableOptions = listOf("XL 6.2L", "XXL 8.3L Dual Basket")
        ),
        Product(
            id = "p3",
            title = "Tecno Camon 30 Pro 5G",
            description = "Capteur Sony IMX890 50MP OIS, Écran AMOLED 144Hz 6.78\", Processeur MediaTek Dimensity 8200 Ultimate, Charge Rapide 70W.",
            priceFcfa = 195000,
            originalPriceFcfa = 225000,
            categoryId = "smartphones",
            brand = "Tecno",
            rating = 4.7f,
            reviewCount = 210,
            stockCount = 25,
            isFlashSale = false,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 1,
            features = listOf("Capteur Sony IMX890 50MP", "Écran AMOLED 144Hz", "Processeur Dimensity 8200", "Flash LED Quadruple"),
            availableColors = listOf("Noir Alpin", "Vert Sahara", "Argent Céleste"),
            availableOptions = listOf("256 Go / 12 Go RAM", "512 Go / 12 Go RAM")
        ),
        Product(
            id = "p4",
            title = "Apple iPhone 15 Pro Max",
            description = "Design en Titane de qualité aérospatiale, puce A17 Pro, bouton Action personnalisable, système photo le plus puissant sur iPhone avec Zoom 5x.",
            priceFcfa = 890000,
            originalPriceFcfa = 950000,
            categoryId = "smartphones",
            brand = "Apple",
            rating = 4.9f,
            reviewCount = 320,
            stockCount = 8,
            isFlashSale = false,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 1,
            features = listOf("Boîtier en Titane", "Puce A17 Pro", "Zoom Optique 5x", "Bouton Action"),
            availableColors = listOf("Titane Naturel", "Titane Bleu", "Titane Noir"),
            availableOptions = listOf("256 Go", "512 Go", "1 To")
        ),
        Product(
            id = "p5",
            title = "Infinix Note 40 Pro+ 5G",
            description = "Charge MagCharge sans fil 20W, Charge filaire 100W All-Round FastCharge 2.0, Écran 3D Incurvé AMOLED 120Hz, Caméra 108MP OIS.",
            priceFcfa = 175000,
            originalPriceFcfa = 200000,
            categoryId = "smartphones",
            brand = "Infinix",
            rating = 4.6f,
            reviewCount = 185,
            stockCount = 30,
            isFlashSale = true,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 1,
            features = listOf("FastCharge 100W", "MagCharge Sans Fil", "Écran AMOLED Incurvé", "Haut-parleurs JBL"),
            availableColors = listOf("Vert Vintage", "Titan Gold"),
            availableOptions = listOf("256 Go / 12 Go RAM")
        ),
        Product(
            id = "p6",
            title = "MacBook Air M3 13.6\" (2024)",
            description = "Puce Apple M3 ultra-rapide, écran Liquid Retina 13.6 pouces, 18 heures d'autonomie, châssis en aluminium recyclé fin et léger.",
            priceFcfa = 820000,
            originalPriceFcfa = 890000,
            categoryId = "computing",
            brand = "Apple",
            rating = 4.9f,
            reviewCount = 76,
            stockCount = 7,
            isFlashSale = false,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 2,
            features = listOf("Puce Apple M3 8-Core", "Autonomie 18h", "Écran Liquid Retina", "Design Fanless Silencieux"),
            availableColors = listOf("Minuit", "Starlight", "Gris Sidéral"),
            availableOptions = listOf("256 Go SSD / 8 Go RAM", "512 Go SSD / 16 Go RAM")
        ),
        Product(
            id = "p7",
            title = "PC Portable HP Pavilion 15 Core i7",
            description = "Processeur Intel Core i7 13ème Gen, 16 Go RAM, 512 Go SSD NVMe, Écran FHD Anti-reflets 15.6\", Windows 11 Famille.",
            priceFcfa = 420000,
            originalPriceFcfa = 480000,
            categoryId = "computing",
            brand = "HP",
            rating = 4.7f,
            reviewCount = 112,
            stockCount = 14,
            isFlashSale = false,
            isPopular = false,
            isOfficialStore = true,
            deliveryEstimateDays = 2,
            features = listOf("Intel Core i7 13th Gen", "16 Go RAM DDR4", "SSD 512 Go Ultra-Rapide", "Audio par B&O"),
            availableColors = listOf("Argent Naturel", "Bleu Nuit"),
            availableOptions = listOf("Core i7 / 512 Go SSD", "Core i7 / 1 To SSD")
        ),
        Product(
            id = "p8",
            title = "Smart TV LG OLED 55\" 4K UHD 120Hz",
            description = "Processeur α9 AI Gen6 4K, Dolby Vision & Atmos, webOS 23, HDMI 2.1 complet pour PS5 et Xbox Series X.",
            priceFcfa = 680000,
            originalPriceFcfa = 790000,
            categoryId = "tv_audio",
            brand = "LG",
            rating = 4.9f,
            reviewCount = 64,
            stockCount = 5,
            isFlashSale = true,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 2,
            features = listOf("Dalle OLED Pixels Auto-Émetteurs", "Taux de rafraîchissement 120Hz", "Dolby Vision IQ & Atmos", "Magic Remote Inclus"),
            availableColors = listOf("Noir Métallisé"),
            availableOptions = listOf("55 pouces", "65 pouces")
        ),
        Product(
            id = "p9",
            title = "Smart TV Samsung 43\" Crystal UHD 4K",
            description = "Processeur Crystal 4K, HDR10+, Smart Hub Tizen OS, Design fin sans bordures 3 côtés, Q-Symphony audio.",
            priceFcfa = 210000,
            originalPriceFcfa = 250000,
            categoryId = "tv_audio",
            brand = "Samsung",
            rating = 4.6f,
            reviewCount = 140,
            stockCount = 20,
            isFlashSale = false,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 2,
            features = listOf("Résolution 4K UHD", "Smart Hub Tizen", "Compatible Gaming Hub", "Design Slim Look"),
            availableColors = listOf("Noir Anthracite"),
            availableOptions = listOf("43 pouces", "50 pouces")
        ),
        Product(
            id = "p10",
            title = "Kit Solaire Domestique PachaSolar 500W + Batterie Lithium",
            description = "Panneau solaire monocristallin 500W, Onduleur hybride 1000VA, Batterie Lithium LiFePO4 1.2kWh, 4 ampoules LED offertes.",
            priceFcfa = 350000,
            originalPriceFcfa = 420000,
            categoryId = "solar_brico",
            brand = "PACHA SOLAR",
            rating = 4.9f,
            reviewCount = 88,
            stockCount = 15,
            isFlashSale = true,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 2,
            features = listOf("Batterie Lithium longue durée (6000 cycles)", "Alimente Télé, Frigo, Éclairage & Ventilateurs", "Garantie 5 ans", "Installation assistée"),
            availableColors = listOf("Bleu Solaire & Blanc"),
            availableOptions = listOf("Pack Standard 500W", "Pack Premium 1000W")
        ),
        Product(
            id = "p11",
            title = "Console Sony PlayStation 5 Slim Édition Standard",
            description = "Design plus fin, lecteur de disque Blu-ray Ultra HD, SSD 1 To, manette sans fil DualSense avec retours haptiques.",
            priceFcfa = 390000,
            originalPriceFcfa = 440000,
            categoryId = "gaming",
            brand = "Sony",
            rating = 4.9f,
            reviewCount = 275,
            stockCount = 10,
            isFlashSale = false,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 1,
            features = listOf("SSD Ultra-Rapide 1 To", "Audio 3D Tempest", "4K 120 FPS", "Manette DualSense"),
            availableColors = listOf("Blanc & Noir"),
            availableOptions = listOf("Édition Standard avec Lecteur", "Édition Digitale")
        ),
        Product(
            id = "p12",
            title = "Réfrigérateur Combiné Nasco 320 Litres No Frost",
            description = "Inverter Technology économique, Froid ventilé No Frost anti-givre, Distributeur d'eau fraîche en façade, Finition Inox Brossé.",
            priceFcfa = 265000,
            originalPriceFcfa = 310000,
            categoryId = "appliances",
            brand = "Nasco",
            rating = 4.7f,
            reviewCount = 94,
            stockCount = 11,
            isFlashSale = false,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 2,
            features = listOf("Distributeur d'eau intégré", "Technologie No Frost", "Économie d'énergie A+", "Éclairage LED intérieur"),
            availableColors = listOf("Inox Gris", "Noir Brossé"),
            availableOptions = listOf("320 Litres", "400 Litres")
        ),
        Product(
            id = "p13",
            title = "Climatiseur Split Smart Inverter Midea 1.5 CV",
            description = "Technologie Inverter économisant jusqu'à 60% d'électricité, Gaz R32 écologique, Filtre Haute Densité Anti-poussière, Refroidissement Ultra-Rapide Flash Cooling.",
            priceFcfa = 185000,
            originalPriceFcfa = 220000,
            categoryId = "appliances",
            brand = "Midea",
            rating = 4.8f,
            reviewCount = 160,
            stockCount = 16,
            isFlashSale = true,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 2,
            features = listOf("Economie Inverter 60%", "Refroidissement Turbo 30s", "Anti-Corrosion Gold Fin", "Silence absolu 21dB"),
            availableColors = listOf("Blanc Pur"),
            availableOptions = listOf("1.5 CV", "2 CV")
        ),
        Product(
            id = "p14",
            title = "Baskets Nike Air Max 270 Homme",
            description = "Unité Max Air 270 sous le talon pour un confort exceptionnel toute la journée. Tige en tissu respirant ajusté et semelle en caoutchouc résistant.",
            priceFcfa = 65000,
            originalPriceFcfa = 80000,
            categoryId = "fashion",
            brand = "Nike",
            rating = 4.8f,
            reviewCount = 130,
            stockCount = 22,
            isFlashSale = false,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 1,
            features = listOf("Amorti Max Air 270", "Empeigne en Mesh Ultra-Aéré", "Maintien dynamique"),
            availableColors = listOf("Noir Intégral", "Blanc & Rouge", "Gris Loup"),
            availableOptions = listOf("40", "41", "42", "43", "44")
        ),
        Product(
            id = "p15",
            title = "Robe d'Événement Boubou Soie de Luxe Pacha Fashion",
            description = "Tissu soie de haute qualité lavé à la main, broderies dorées faites à la main par des maîtres artisans d'Abidjan. Élégance et prestige garanti.",
            priceFcfa = 48000,
            originalPriceFcfa = 65000,
            categoryId = "fashion",
            brand = "SERVICE PACHA Luxury",
            rating = 4.9f,
            reviewCount = 52,
            stockCount = 18,
            isFlashSale = true,
            isPopular = false,
            isOfficialStore = true,
            deliveryEstimateDays = 1,
            features = listOf("Broderie artisanale dorée", "Soie fluide respirante", "Design exclusif SERVICE PACHA"),
            availableColors = listOf("Bleu Royal & Or", "Vert Émeraude", "Bordeaux"),
            availableOptions = listOf("Taille Unique (M à XXL)", "Sur-Mesure")
        ),
        Product(
            id = "p16",
            title = "Parfum de Luxe Oud Impérial 100ml Eau de Parfum",
            description = "Fragrance orientale intense aux notes de Bois d'Oud précieux, Ambre chaud, Rose de Damas et Vanille de Madagascar. Sillage longue durée 24h.",
            priceFcfa = 45000,
            originalPriceFcfa = 60000,
            categoryId = "beauty",
            brand = "PACHA Parfums",
            rating = 4.9f,
            reviewCount = 110,
            stockCount = 35,
            isFlashSale = false,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 1,
            features = listOf("Extrait de Parfum concentré", "Ingrédients 100% rares", "Flacon gravé à l'or fin"),
            availableColors = listOf("Flacon Noir & Or"),
            availableOptions = listOf("100 ml", "Coffret Cadeau 100ml + Spray Voyage")
        ),
        Product(
            id = "p17",
            title = "Sac de Riz Parfumè Jasmin Dinor 25 Kg",
            description = "Riz supérieur 100% brisures parfumées, grain fin et léger, parfum jasmin naturel envoûtant. Qualité certifiée pour toute la famille.",
            priceFcfa = 18500,
            originalPriceFcfa = 21000,
            categoryId = "groceries",
            brand = "Dinor",
            rating = 4.8f,
            reviewCount = 410,
            stockCount = 100,
            isFlashSale = false,
            isPopular = true,
            isOfficialStore = true,
            deliveryEstimateDays = 1,
            features = listOf("100% Riz Parfumè", "Sac renforcé 25 Kg", "Idéal pour grands repas"),
            availableColors = listOf("Sac Rouge Dinor"),
            availableOptions = listOf("25 Kg", "50 Kg")
        ),
        Product(
            id = "p18",
            title = "Batterie Auto Bosch S4 12V 74Ah 680A",
            description = "Technologie Bosch PowerFrame assurant un démarrage à froid optimal et une résistance maximale aux fortes chaleurs tropicales.",
            priceFcfa = 58000,
            originalPriceFcfa = 68000,
            categoryId = "auto_moto",
            brand = "Bosch",
            rating = 4.8f,
            reviewCount = 45,
            stockCount = 14,
            isFlashSale = false,
            isPopular = false,
            isOfficialStore = true,
            deliveryEstimateDays = 1,
            features = listOf("Grille PowerFrame renforcée", "Sans entretien", "Haute résistance thermique"),
            availableColors = listOf("Gris & Bleu Bosch"),
            availableOptions = listOf("12V 74Ah", "12V 100Ah")
        )
    )

    private val _products = MutableStateFlow<List<Product>>(initialProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        loadProductsFromFirestore()
    }

    private fun loadProductsFromFirestore() {
        try {
            productsCollection.addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null && !snapshot.isEmpty) {
                    val firestoreList = snapshot.toObjects(Product::class.java)
                    if (firestoreList.isNotEmpty()) {
                        _products.value = firestoreList
                    }
                }
            }
        } catch (e: Exception) {
            // Keep initialProducts if Firestore is not provisioned or offline
        }
    }

    private fun getSafeProduct(index: Int): Product {
        return initialProducts.getOrElse(index) { initialProducts.first() }
    }

    private val _cartItems = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(
                id = "cart_item_1",
                product = getSafeProduct(0), // Samsung Galaxy S24 Ultra
                quantity = 1,
                selectedColor = "Gris Titane",
                selectedOption = "512 Go"
            ),
            CartItem(
                id = "cart_item_2",
                product = getSafeProduct(1), // Airfryer Philips
                quantity = 1,
                selectedColor = "Noir Laqué & Cuivre",
                selectedOption = "XL 6.2L"
            )
        )
    )
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(
        listOf(
            Order(
                id = "ord_001",
                orderNumber = "PACHA-89421",
                dateFormatted = "Aujourd'hui à 14:20",
                status = OrderStatus.DELIVERING,
                items = listOf(
                    CartItem(
                        id = "ci_hist_1",
                        product = getSafeProduct(2), // Tecno Camon 30
                        quantity = 1,
                        selectedColor = "Noir Alpin",
                        selectedOption = "256 Go"
                    )
                ),
                subtotalFcfa = 195000,
                shippingFeeFcfa = 2000,
                discountFcfa = 10000,
                totalFcfa = 187000,
                recipientName = "Client SERVICE PACHA",
                deliveryAddress = "Cocody Deux-Plateaux, Résidence les Palmes",
                deliveryNeighborhood = "Deux-Plateaux Vallons",
                deliveryCity = "Abidjan",
                deliveryInstructions = "Portail bleu, sonner au numéro 14",
                phoneNumber = "+225 07 88 99 00 11",
                paymentMethodName = "Wave Mobile Money",
                trackingSteps = listOf(
                    TrackingStep(
                        title = "Commande validée",
                        description = "Votre paiement Wave a été confirmé avec succès.",
                        time = "14:20",
                        isCompleted = true,
                        isCurrent = false
                    ),
                    TrackingStep(
                        title = "Préparation au centre logistique",
                        description = "Le colis a été emballé et vérifié par l'équipe SERVICE PACHA.",
                        time = "15:45",
                        isCompleted = true,
                        isCurrent = false
                    ),
                    TrackingStep(
                        title = "En cours de livraison avec coursier",
                        description = "Le livreur Pacha Express Mamadou est en route vers votre adresse.",
                        time = "17:10",
                        isCompleted = true,
                        isCurrent = true
                    ),
                    TrackingStep(
                        title = "Livraison terminée",
                        description = "Colis remis en main propre contre signature.",
                        time = "Estimée à 18:30",
                        isCompleted = false,
                        isCurrent = false
                    )
                )
            ),
            Order(
                id = "ord_002",
                orderNumber = "PACHA-77290",
                dateFormatted = "28 Septembre 2026",
                status = OrderStatus.DELIVERED,
                items = listOf(
                    CartItem(
                        id = "ci_hist_2",
                        product = getSafeProduct(13), // Sneakers Nike
                        quantity = 1,
                        selectedColor = "Noir Intégral",
                        selectedOption = "42"
                    ),
                    CartItem(
                        id = "ci_hist_3",
                        product = getSafeProduct(15), // Parfum Oud
                        quantity = 1,
                        selectedColor = "Flacon Noir & Or",
                        selectedOption = "100 ml"
                    )
                ),
                subtotalFcfa = 110000,
                shippingFeeFcfa = 2000,
                discountFcfa = 5000,
                totalFcfa = 107000,
                recipientName = "Client SERVICE PACHA",
                deliveryAddress = "Cocody Deux-Plateaux, Boulevard Latrille",
                deliveryNeighborhood = "Angré 8ème Tranche",
                deliveryCity = "Abidjan",
                deliveryInstructions = "Laisser le colis au gardien",
                phoneNumber = "+225 07 88 99 00 11",
                paymentMethodName = "Orange Money",
                trackingSteps = listOf(
                    TrackingStep(
                        title = "Commande confirmée",
                        description = "Paiement Orange Money reçu.",
                        time = "28 Sept, 09:12",
                        isCompleted = true,
                        isCurrent = false
                    ),
                    TrackingStep(
                        title = "Expédiée du Hub",
                        description = "Colis transféré au coursier.",
                        time = "28 Sept, 11:30",
                        isCompleted = true,
                        isCurrent = false
                    ),
                    TrackingStep(
                        title = "Livrée",
                        description = "Colis remis à Abidjan avec succès.",
                        time = "28 Sept, 16:45",
                        isCompleted = true,
                        isCurrent = true
                    )
                )
            )
        )
    )
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(
        listOf(
            AppNotification(
                id = "notif_welcome",
                title = "Bienvenue sur SERVICE PACHA 🎉",
                message = "Profitez de nos offres exceptionnelles et de la livraison express partout en Côte d'Ivoire !",
                type = NotificationType.PROMO_ALERT,
                timestampFormatted = "Aujourd'hui à 08:30",
                isRead = false
            ),
            AppNotification(
                id = "notif_past_1",
                orderId = "ord_past_1",
                orderNumber = "PACHA-89210",
                title = "Paiement confirmé (Wave)",
                message = "Votre commande #PACHA-89210 a été confirmée et est en cours d'acheminement.",
                type = NotificationType.PAYMENT_CONFIRMED,
                timestampFormatted = "Hier à 14:32",
                isRead = true
            )
        )
    )
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<String?>(null)
    val appliedCoupon: StateFlow<String?> = _appliedCoupon.asStateFlow()

    private val _couponDiscountPercent = MutableStateFlow(0)
    val couponDiscountPercent: StateFlow<Int> = _couponDiscountPercent.asStateFlow()

    fun addToCart(product: Product, quantity: Int = 1, color: String? = null, option: String? = null) {
        _cartItems.update { current ->
            val existingIndex = current.indexOfFirst {
                it.product.id == product.id && it.selectedColor == color && it.selectedOption == option
            }
            if (existingIndex >= 0) {
                current.mapIndexed { index, item ->
                    if (index == existingIndex) item.copy(quantity = item.quantity + quantity) else item
                }
            } else {
                current + CartItem(
                    id = "cart_${System.currentTimeMillis()}",
                    product = product,
                    quantity = quantity,
                    selectedColor = color ?: product.availableColors.firstOrNull(),
                    selectedOption = option ?: product.availableOptions.firstOrNull()
                )
            }
        }
    }

    fun updateCartQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(cartItemId)
        } else {
            _cartItems.update { list ->
                list.map { if (it.id == cartItemId) it.copy(quantity = newQuantity) else it }
            }
        }
    }

    fun removeFromCart(cartItemId: String) {
        _cartItems.update { list -> list.filterNot { it.id == cartItemId } }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun toggleFavorite(productId: String) {
        _products.update { list: List<Product> ->
            list.map {
                if (it.id == productId) it.copy(isFavorite = !it.isFavorite) else it
            }
        }
    }

    fun applyCoupon(code: String): Boolean {
        val clean = code.trim().uppercase()
        return when (clean) {
            "PACHA10" -> {
                _appliedCoupon.value = "PACHA10"
                _couponDiscountPercent.value = 10
                true
            }
            "BIENVENUE" -> {
                _appliedCoupon.value = "BIENVENUE"
                _couponDiscountPercent.value = 15
                true
            }
            "PACHAVIP" -> {
                _appliedCoupon.value = "PACHAVIP"
                _couponDiscountPercent.value = 20
                true
            }
            else -> false
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        _couponDiscountPercent.value = 0
    }

    fun placeOrder(
        paymentMethod: String,
        address: String,
        neighborhood: String,
        city: String,
        instructions: String = "",
        phone: String,
        recipientName: String,
        transactionId: String = "TXN-${(100000..999999).random()}",
        isPaid: Boolean = true,
        paymentGatewayId: String = "wave"
    ): Order? {
        val items = _cartItems.value
        if (items.isEmpty()) return null

        val subtotal = items.sumOf { it.totalPriceFcfa }
        val discount = if (_couponDiscountPercent.value > 0) {
            (subtotal * _couponDiscountPercent.value) / 100
        } else 0
        val shippingFee = if (subtotal > 150000) 0L else 2000L
        val total = subtotal - discount + shippingFee

        val newOrder = Order(
            id = "ord_${System.currentTimeMillis()}",
            orderNumber = "PACHA-${(10000..99999).random()}",
            dateFormatted = "À l'instant",
            status = OrderStatus.CONFIRMED,
            items = items,
            subtotalFcfa = subtotal,
            shippingFeeFcfa = shippingFee,
            discountFcfa = discount,
            totalFcfa = total,
            recipientName = recipientName,
            deliveryAddress = address,
            deliveryNeighborhood = neighborhood,
            deliveryCity = city,
            deliveryInstructions = instructions,
            phoneNumber = phone,
            paymentMethodName = paymentMethod,
            trackingSteps = listOf(
                TrackingStep(
                    title = if (isPaid) "Paiement validé ($paymentMethod)" else "Commande enregistrée (À payer à la livraison)",
                    description = if (isPaid) "Transaction $transactionId confirmée avec succès." else "Préparez le montant exact pour le livreur.",
                    time = "À l'instant",
                    isCompleted = true,
                    isCurrent = true
                ),
                TrackingStep(
                    title = "Préparation des articles",
                    description = "Les articles sont en cours d'emballage par les vendeurs certifiés.",
                    time = "En attente",
                    isCompleted = false,
                    isCurrent = false
                ),
                TrackingStep(
                    title = "Expédition",
                    description = "Prise en charge par le service de livraison SERVICE PACHA Express.",
                    time = "Prévue demain",
                    isCompleted = false,
                    isCurrent = false
                ),
                TrackingStep(
                    title = "Livraison à domicile",
                    description = "Remise de votre colis contre vérification.",
                    time = "Prévue sous 48h",
                    isCompleted = false,
                    isCurrent = false
                )
            ),
            transactionId = transactionId,
            isPaid = isPaid,
            paymentGatewayId = paymentGatewayId
        )

        _orders.update { listOf(newOrder) + it }
        clearCart()
        removeCoupon()

        // Émission automatique des notifications liées à la commande
        val notifOrder = AppNotification(
            id = "notif_ord_${System.currentTimeMillis()}",
            orderId = newOrder.id,
            orderNumber = newOrder.orderNumber,
            title = "Nouvelle commande #${newOrder.orderNumber}",
            message = "Votre commande d'un montant de ${formatFcfa(total)} est confirmée. Nos équipes préparent vos articles.",
            type = NotificationType.NEW_ORDER,
            timestampFormatted = "À l'instant",
            isRead = false
        )
        val notifPayment = if (isPaid) {
            AppNotification(
                id = "notif_pay_${System.currentTimeMillis() + 1}",
                orderId = newOrder.id,
                orderNumber = newOrder.orderNumber,
                title = "Paiement validé ($paymentMethod)",
                message = "Votre paiement a été approuvé avec succès (Réf: $transactionId).",
                type = NotificationType.PAYMENT_CONFIRMED,
                timestampFormatted = "À l'instant",
                isRead = false
            )
        } else null

        _notifications.update { current ->
            listOfNotNull(notifPayment, notifOrder) + current
        }

        return newOrder
    }

    fun updateSelectedCity(city: String, country: String) {
        _userProfile.update { it.copy(currentCity = city, currentCountry = country) }
    }

    fun updateUserProfileDetails(fullName: String, phone: String, email: String, addressStreet: String, neighborhood: String) {
        _userProfile.update { current ->
            val updatedAddresses = if (current.addresses.isNotEmpty()) {
                current.addresses.mapIndexed { index, addr ->
                    if (index == 0) {
                        addr.copy(
                            fullName = fullName.ifBlank { addr.fullName },
                            phone = phone.ifBlank { addr.phone },
                            street = addressStreet.ifBlank { addr.street },
                            neighborhood = neighborhood.ifBlank { addr.neighborhood }
                        )
                    } else addr
                }
            } else {
                listOf(
                    com.example.data.model.DeliveryAddress(
                        id = "addr_main",
                        title = "Adresse Principale",
                        fullName = fullName,
                        street = addressStreet,
                        neighborhood = neighborhood,
                        city = current.currentCity,
                        country = current.currentCountry,
                        phone = phone,
                        isDefault = true
                    )
                )
            }
            current.copy(
                fullName = fullName.ifBlank { current.fullName },
                phone = phone.ifBlank { current.phone },
                email = email.ifBlank { current.email },
                addresses = updatedAddresses
            )
        }
    }

    fun loginOrRegister(fullName: String, phone: String, password: String) {
        _userProfile.update {
            it.copy(
                isLoggedIn = true,
                fullName = fullName.ifBlank { it.fullName },
                phone = phone.ifBlank { it.phone }
            )
        }
    }

    // ADMINISTRATEUR UNIQUE : Fonctions exclusives de gestion
    fun adminAddProduct(product: Product) {
        productsCollection.document(product.id).set(product)
    }

    fun adminUpdateProduct(updatedProduct: Product) {
        productsCollection.document(updatedProduct.id).set(updatedProduct)
    }

    fun adminDeleteProduct(productId: String) {
        productsCollection.document(productId).delete()
    }

    fun adminUpdateStock(productId: String, newStock: Int) {
        productsCollection.document(productId).update("stockCount", newStock.coerceAtLeast(0))
    }

    fun adminUpdateOrderStatus(orderId: String, newStatus: OrderStatus) {
        var updatedOrderRef: Order? = null
        _orders.update { list ->
            list.map { order ->
                if (order.id == orderId) {
                    val updatedSteps = order.trackingSteps.mapIndexed { idx, step ->
                        when (newStatus) {
                            OrderStatus.CONFIRMED -> step.copy(isCompleted = idx == 0, isCurrent = idx == 0)
                            OrderStatus.PROCESSING -> step.copy(isCompleted = idx <= 1, isCurrent = idx == 1)
                            OrderStatus.SHIPPED -> step.copy(isCompleted = idx <= 2, isCurrent = idx == 2)
                            OrderStatus.DELIVERING -> step.copy(isCompleted = idx <= 2, isCurrent = idx == 2)
                            OrderStatus.DELIVERED -> step.copy(isCompleted = true, isCurrent = idx == 3)
                            OrderStatus.CANCELLED -> step.copy(isCompleted = false, isCurrent = false)
                        }
                    }
                    val changed = order.copy(status = newStatus, trackingSteps = updatedSteps)
                    updatedOrderRef = changed
                    changed
                } else order
            }
        }

        // Notification d'état de commande liée à l'utilisateur
        updatedOrderRef?.let { ord ->
            val notifType = when (newStatus) {
                OrderStatus.CONFIRMED -> NotificationType.NEW_ORDER
                OrderStatus.PROCESSING -> NotificationType.ORDER_PROCESSING
                OrderStatus.SHIPPED -> NotificationType.ORDER_SHIPPED
                OrderStatus.DELIVERING -> NotificationType.ORDER_DELIVERING
                OrderStatus.DELIVERED -> NotificationType.ORDER_DELIVERED
                OrderStatus.CANCELLED -> NotificationType.ORDER_CANCELLED
            }
            val notifMessage = when (newStatus) {
                OrderStatus.CONFIRMED -> "Votre commande #${ord.orderNumber} a été enregistrée avec succès."
                OrderStatus.PROCESSING -> "Votre commande #${ord.orderNumber} est désormais en préparation par notre équipe."
                OrderStatus.SHIPPED -> "Votre commande #${ord.orderNumber} a été expédiée et remise à notre livreur."
                OrderStatus.DELIVERING -> "Le livreur est actuellement en route vers votre adresse (${ord.deliveryAddress})."
                OrderStatus.DELIVERED -> "Votre commande #${ord.orderNumber} vous a été remise en main propre. Merci !"
                OrderStatus.CANCELLED -> "Votre commande #${ord.orderNumber} a été annulée."
            }
            val statusNotification = AppNotification(
                id = "notif_status_${System.currentTimeMillis()}",
                orderId = ord.id,
                orderNumber = ord.orderNumber,
                title = "Statut : ${newStatus.label}",
                message = notifMessage,
                type = notifType,
                timestampFormatted = "À l'instant",
                isRead = false
            )
            _notifications.update { listOf(statusNotification) + it }
        }
    }

    fun markNotificationAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun markAllNotificationsAsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
    }

    fun clearAllNotifications() {
        _notifications.value = emptyList()
    }

    companion object {
        private val numberFormat = NumberFormat.getInstance(Locale.FRANCE)

        fun formatFcfa(amount: Long): String {
            return "${numberFormat.format(amount)} FCFA"
        }
    }
}
