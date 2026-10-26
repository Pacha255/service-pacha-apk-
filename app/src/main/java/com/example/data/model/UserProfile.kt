package com.example.data.model

data class DeliveryAddress(
    val id: String,
    val title: String, // e.g. "Maison", "Bureau"
    val fullName: String,
    val street: String,
    val neighborhood: String,
    val city: String,
    val country: String,
    val phone: String,
    val isDefault: Boolean = false
)

data class SavedPaymentWallet(
    val id: String,
    val gateway: PaymentGateway,
    val identifier: String, // e.g. "+225 07 88 99 00 11" or "•••• 4821"
    val title: String,
    val isDefault: Boolean = false
)

data class UserProfile(
    val isLoggedIn: Boolean = false,
    val fullName: String = "Client SERVICE PACHA",
    val email: String = "servicepacha43@gmail.com",
    val phone: String = "+225 07 88 99 00 11",
    val currentCity: String = "Abidjan",
    val currentCountry: String = "Côte d'Ivoire",
    val memberTier: String = "Membre Pacha VIP Gold",
    val pachaPoints: Int = 1250,
    val addresses: List<DeliveryAddress> = listOf(
        DeliveryAddress(
            id = "addr_1",
            title = "Résidence Principale",
            fullName = "Client SERVICE PACHA",
            street = "Boulevard Latrille, Résidence les Palmes",
            neighborhood = "Cocody Deux-Plateaux",
            city = "Abidjan",
            country = "Côte d'Ivoire",
            phone = "+225 07 88 99 00 11",
            isDefault = true
        ),
        DeliveryAddress(
            id = "addr_2",
            title = "Bureau / Lieu de travail",
            fullName = "Client SERVICE PACHA",
            street = "Avenue Chardy, Immeuble Alpha",
            neighborhood = "Plateau",
            city = "Abidjan",
            country = "Côte d'Ivoire",
            phone = "+225 05 11 22 33 44",
            isDefault = false
        )
    ),
    val savedWallets: List<SavedPaymentWallet> = listOf(
        SavedPaymentWallet(
            id = "w_wave_1",
            gateway = PaymentGateway.WAVE,
            identifier = "+225 07 88 99 00 11",
            title = "Compte Wave Principal",
            isDefault = true
        ),
        SavedPaymentWallet(
            id = "w_om_1",
            gateway = PaymentGateway.ORANGE_MONEY,
            identifier = "+225 07 00 11 22 33",
            title = "Compte Orange Money",
            isDefault = false
        ),
        SavedPaymentWallet(
            id = "w_mtn_1",
            gateway = PaymentGateway.MTN_MOMO,
            identifier = "+225 05 44 55 66 77",
            title = "Compte MTN MoMo",
            isDefault = false
        ),
        SavedPaymentWallet(
            id = "w_moov_1",
            gateway = PaymentGateway.MOOV_MONEY,
            identifier = "+225 01 22 33 44 55",
            title = "Compte Moov Flooz",
            isDefault = false
        ),
        SavedPaymentWallet(
            id = "w_card_1",
            gateway = PaymentGateway.CREDIT_CARD,
            identifier = "•••• 5412 (Visa Platinum)",
            title = "Carte Bancaire Visa",
            isDefault = false
        )
    )
)
