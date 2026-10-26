package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.CartDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.ProductEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base de données Room locale pour l'application SERVICE PACHA.
 * Stocke le catalogue de produits et le panier d'achat avec prix en FCFA.
 */
@Database(
    entities = [ProductEntity::class, CartItemEntity::class],
    version = 3,
    exportSchema = false
)
abstract class PachaDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: PachaDatabase? = null

        fun getDatabase(context: Context): PachaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PachaDatabase::class.java,
                    "service_pacha_catalog.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(PachaDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class PachaDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialCatalog(database.productDao())
                    }
                }
            }
        }

        /**
         * Pré-remplit le catalogue avec les articles phares de SERVICE PACHA en FCFA
         * classés par catégories (Électronique, Alimentaire, Mode, Électroménager).
         */
        suspend fun populateInitialCatalog(productDao: ProductDao) {
            val initialProducts = listOf(
                // 1. ÉLECTRONIQUE
                ProductEntity(
                    id = "pacha_p1",
                    name = "Samsung Galaxy S24 Ultra 5G",
                    priceFcfa = 750000L,
                    originalPriceFcfa = 820000L,
                    description = "Écran Dynamic AMOLED 2X 6.8\" 120Hz, Processeur Snapdragon 8 Gen 3, S-Pen intégré, Quad Caméra 200 MP avec Zoom Optique 10x et IA Galaxy.",
                    category = "Électronique",
                    categoryId = "smartphones",
                    categoryName = "Électronique",
                    brand = "Samsung",
                    stockCount = 18,
                    isFlashSale = true,
                    isPopular = true
                ),
                ProductEntity(
                    id = "pacha_p3",
                    name = "Tecno Camon 30 Pro 5G",
                    priceFcfa = 195000L,
                    originalPriceFcfa = 225000L,
                    description = "Capteur Sony IMX890 50MP OIS, Écran AMOLED 144Hz 6.78\", Processeur MediaTek Dimensity 8200 Ultimate, Charge Rapide 70W.",
                    category = "Électronique",
                    categoryId = "smartphones",
                    categoryName = "Électronique",
                    brand = "Tecno",
                    stockCount = 25,
                    isFlashSale = false,
                    isPopular = true
                ),
                ProductEntity(
                    id = "pacha_p4",
                    name = "Apple iPhone 15 Pro Max",
                    priceFcfa = 890000L,
                    originalPriceFcfa = 950000L,
                    description = "Design en Titane de qualité aérospatiale, puce A17 Pro, bouton Action personnalisable, système photo avec Zoom 5x.",
                    category = "Électronique",
                    categoryId = "smartphones",
                    categoryName = "Électronique",
                    brand = "Apple",
                    stockCount = 8,
                    isFlashSale = false,
                    isPopular = true
                ),
                ProductEntity(
                    id = "pacha_p5",
                    name = "Smart TV LG 55\" OLED 4K 120Hz",
                    priceFcfa = 480000L,
                    originalPriceFcfa = 550000L,
                    description = "Pixels auto-émissifs pour des noirs infinis, processeur α9 AI Gen6 4K, Dolby Vision & Atmos, streaming et jeu fluide.",
                    category = "Électronique",
                    categoryId = "tv_audio",
                    categoryName = "Électronique",
                    brand = "LG",
                    stockCount = 10,
                    isFlashSale = false,
                    isPopular = true
                ),

                // 2. ALIMENTAIRE
                ProductEntity(
                    id = "pacha_p9",
                    name = "Sac de Riz Parfumé Jasmin Royal 25 kg",
                    priceFcfa = 22500L,
                    originalPriceFcfa = 26000L,
                    description = "Riz blanc parfumé 1er choix aux grains longs et tendres. Cuisson facile, parfum authentique, idéal pour vos repas familiaux.",
                    category = "Alimentaire",
                    categoryId = "grocery",
                    categoryName = "Alimentaire",
                    brand = "Jasmin Royal",
                    stockCount = 40,
                    isFlashSale = true,
                    isPopular = true
                ),
                ProductEntity(
                    id = "pacha_p10",
                    name = "Bidon d'Huile Végétale Raffinée Dinor 5L",
                    priceFcfa = 7800L,
                    originalPriceFcfa = 9500L,
                    description = "Huile raffinée 100% végétale enrichie en vitamine A. Sans cholestérol, garantie pour fritures dorées et assaisonnements.",
                    category = "Alimentaire",
                    categoryId = "grocery",
                    categoryName = "Alimentaire",
                    brand = "Dinor",
                    stockCount = 50,
                    isFlashSale = false,
                    isPopular = true
                ),
                ProductEntity(
                    id = "pacha_p11",
                    name = "Carton Lait Concentré Sucré Bonnet Rouge (48 boîtes)",
                    priceFcfa = 26500L,
                    originalPriceFcfa = 29000L,
                    description = "Lait concentré sucré riche en calcium et vitamines. Idéal pour petits déjeuners, desserts, cafés et thés gourmands.",
                    category = "Alimentaire",
                    categoryId = "grocery",
                    categoryName = "Alimentaire",
                    brand = "Bonnet Rouge",
                    stockCount = 20,
                    isFlashSale = true,
                    isPopular = false
                ),

                // 3. MODE
                ProductEntity(
                    id = "pacha_p8",
                    name = "Pack 3 Chemises Slim Fit Lin & Coton",
                    priceFcfa = 28000L,
                    originalPriceFcfa = 35000L,
                    description = "Confection premium en tissu respirant lin et coton, coupe ajustée moderne, boutons nacrés, idéale pour le bureau et les sorties.",
                    category = "Mode",
                    categoryId = "fashion",
                    categoryName = "Mode",
                    brand = "Pacha Collection",
                    stockCount = 30,
                    isFlashSale = true,
                    isPopular = true
                ),
                ProductEntity(
                    id = "pacha_p12",
                    name = "Ensemble Bazin Riche Brodé Pacha Couture",
                    priceFcfa = 45000L,
                    originalPriceFcfa = 55000L,
                    description = "Tissu Bazin Riche de qualité supérieure teinté artisanalement avec broderies soignées au fil d'or, taille M/L/XL.",
                    category = "Mode",
                    categoryId = "fashion",
                    categoryName = "Mode",
                    brand = "Pacha Couture",
                    stockCount = 14,
                    isFlashSale = false,
                    isPopular = true
                ),
                ProductEntity(
                    id = "pacha_p13",
                    name = "Sneakers Cuir Blanc Urbain Sport",
                    priceFcfa = 25000L,
                    originalPriceFcfa = 32000L,
                    description = "Baskets basses en cuir pleine fleur, semelle amortissante confort toute la journée, finitions soignées.",
                    category = "Mode",
                    categoryId = "fashion",
                    categoryName = "Mode",
                    brand = "Pacha Sport",
                    stockCount = 22,
                    isFlashSale = false,
                    isPopular = true
                ),

                // 4. ÉLECTROMÉNAGER
                ProductEntity(
                    id = "pacha_p2",
                    name = "Airfryer Friteuse sans Huile Philips XXL",
                    priceFcfa = 85000L,
                    originalPriceFcfa = 110000L,
                    description = "Technologie Rapid Air pour des fritures croustillantes sans huile ajoutée. Capacité 6.2L (1.4kg), écran tactile numérique.",
                    category = "Électroménager",
                    categoryId = "appliances",
                    categoryName = "Électroménager",
                    brand = "Philips",
                    stockCount = 12,
                    isFlashSale = true,
                    isPopular = true
                ),
                ProductEntity(
                    id = "pacha_p6",
                    name = "Climatiseur Inverter Gree 1.5 CV",
                    priceFcfa = 230000L,
                    originalPriceFcfa = 270000L,
                    description = "Refroidissement rapide adapté aux climats tropicaux, faible consommation d'énergie (Classe A++), filtre antibactérien haute densité.",
                    category = "Électroménager",
                    categoryId = "appliances",
                    categoryName = "Électroménager",
                    brand = "Gree",
                    stockCount = 15,
                    isFlashSale = true,
                    isPopular = true
                ),
                ProductEntity(
                    id = "pacha_p7",
                    name = "Machine à Laver Midea 8 kg Frontale",
                    priceFcfa = 185000L,
                    originalPriceFcfa = 210000L,
                    description = "Moteur Inverter silencieux, 15 programmes de lavage dont express 15 min, tambour acier inoxydable anti-bactérien.",
                    category = "Électroménager",
                    categoryId = "appliances",
                    categoryName = "Électroménager",
                    brand = "Midea",
                    stockCount = 9,
                    isFlashSale = false,
                    isPopular = false
                )
            )
            productDao.insertAll(initialProducts)
        }
    }
}
