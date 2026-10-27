# SERVICE PACHA - Marketplace & App Mobile Android

[![Build and Publish Debug APK](https://github.com/Pacha255/service-pacha-apk/actions/workflows/build-apk.yml/badge.svg)](https://github.com/Pacha255/service-pacha-apk/actions/workflows/build-apk.yml)

Application Android officielle pour **SERVICE PACHA**, la plateforme e-commerce de référence (Téléphonie, Électroménager, Alimentaire, Mode, TV & Son).

---

## 🌟 Fonctionnalités Principales

- **Base de Données Locale Room (SQLite)** :
  - Persistence hors-ligne du catalogue de produits et du panier d'achat.
  - Gestion intégrale des montants en **Francs CFA (FCFA)** avec formatage monétaire clair.
- **Gestion Avancée du Panier (`CartItemEntity`)** :
  - Ajout/retrait dynamique d'articles.
  - Ajustement des quantités et calcul automatique du montant total cumulé en FCFA.
- **Filtrage par Catégorie** :
  - Recherche et filtrage instantanés : **Alimentaire**, **Électronique**, **Mode**, **Électroménager**.
- **Paiements Locaux & Mobile Money** :
  - Prise en charge des moyens de paiement : Wave, Orange Money, MTN MoMo, Moov Money, Carte bancaire et Règlement à la livraison.

---

## 🚀 Téléchargement Direct de l'APK (GitHub Actions & Releases)

L'application intègre un workflow **GitHub Actions** automatisé (`.github/workflows/build-apk.yml`).

### Comment télécharger l'APK :

1. **Depuis la section Releases** :
   - Rendez-vous sur [Releases de Pacha255/service-pacha-apk](https://github.com/Pacha255/service-pacha-apk/releases).
   - Téléchargez le fichier **`app-debug.apk`** de la dernière version.

2. **Depuis les Artefacts GitHub Actions** :
   - Accédez à l'onglet [Actions](https://github.com/Pacha255/service-pacha-apk/actions).
   - Cliquez sur le dernier build exécuté avec succès.
   - Téléchargez l'artefact **`app-debug`**.

---

## 🛠️ Structure du Projet

- **`app/src/main/java/com/example/data/local/`** :
  - `entity/ProductEntity.kt` & `CartItemEntity.kt` (Entités Room)
  - `dao/ProductDao.kt` & `CartDao.kt` (Interfaces de requêtes SQL réactives `Flow`)
  - `repository/` (Couche de dépôt de données)
  - `PachaDatabase.kt` (Base SQLite Room v3)
- **`app/src/main/java/com/example/ui/`** :
  - `ProductCatalogViewModel.kt` (Gestion réactive des états UI et filtres par catégorie)
  - `screens/ProductCatalogScreen.kt` (Interface Compose avec panier Room et fiches produits)
- **`.github/workflows/build-apk.yml`** :
  - Automation de compilation de l'APK de débogage et publication automatique sur GitHub Releases.

---

## 📄 Licence & Crédits

- **Développeur / Propriétaire** : SERVICE PACHA (`servicepacha43@gmail.com`)
- **Dépôt GitHub** : `Pacha255/service-pacha-apk`
