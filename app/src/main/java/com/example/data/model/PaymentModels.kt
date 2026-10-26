package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.MoovColor
import com.example.ui.theme.MtnColor
import com.example.ui.theme.OrangeMoneyColor
import com.example.ui.theme.PachaEmerald
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.WaveColor

enum class PaymentStatus {
    IDLE,
    PROCESSING,
    SUCCESS,
    FAILED
}

enum class PaymentGateway(
    val id: String,
    val displayName: String,
    val operatorSubtext: String,
    val brandColor: Color,
    val feeDescription: String,
    val ussdInstruction: String,
    val securityNotice: String
) {
    WAVE(
        id = "wave",
        displayName = "Wave Mobile Money",
        operatorSubtext = "Paiement 100% sans frais, validation instantanée",
        brandColor = WaveColor,
        feeDescription = "0% de frais de transaction",
        ussdInstruction = "Validez la notification Push sur l'application Wave ou confirmez avec votre code secret.",
        securityNotice = "Transaction sécurisée et chiffrée de bout en bout avec Wave."
    ),
    ORANGE_MONEY(
        id = "orange_money",
        displayName = "Orange Money",
        operatorSubtext = "Code OTP ou validation USSD #144#",
        brandColor = OrangeMoneyColor,
        feeDescription = "Frais réseau opérateur standard",
        ussdInstruction = "Générez votre code d'autorisation en composant le #144#391# ou validez le SMS reçu.",
        securityNotice = "Paiement direct sécurisé par Orange Money Web Gateway."
    ),
    MTN_MOMO(
        id = "mtn_momo",
        displayName = "MTN MoMo",
        operatorSubtext = "Invite automatique sur votre téléphone *133#",
        brandColor = MtnColor,
        feeDescription = "0% de frais sur SERVICE PACHA",
        ussdInstruction = "Un prompt USSD apparaîtra sur votre écran. Entrez votre code PIN MoMo à 4 chiffres.",
        securityNotice = "Certifié par MTN Mobile Financial Services."
    ),
    MOOV_MONEY(
        id = "moov_money",
        displayName = "Moov Money Flooz",
        operatorSubtext = "Confirmation par code secret *155#",
        brandColor = MoovColor,
        feeDescription = "Frais réduits",
        ussdInstruction = "Composez le *155# pour autoriser le débit SERVICE PACHA.",
        securityNotice = "Paiement garanti par Moov Africa."
    ),
    CASH_ON_DELIVERY(
        id = "cash_on_delivery",
        displayName = "Paiement à la Livraison",
        operatorSubtext = "Réglez en espèces ou Mobile Money au livreur",
        brandColor = PachaEmerald,
        feeDescription = "Sans frais supplémentaires",
        ussdInstruction = "Vérifiez votre colis lors de la remise en main propre avant de payer le livreur.",
        securityNotice = "Protection Acheteur garantie : remboursement si non conforme."
    ),
    CREDIT_CARD(
        id = "credit_card",
        displayName = "Carte Bancaire Visa / Mastercard",
        operatorSubtext = "Cartes bancaires locales et internationales",
        brandColor = PachaOrange,
        feeDescription = "Sécurisé 3D-Secure",
        ussdInstruction = "Une vérification 3D-Secure par SMS de votre banque peut vous être demandée.",
        securityNotice = "Norme bancaire PCI-DSS et cryptage SSL 256 bits."
    )
}

data class TransactionReceipt(
    val transactionId: String,
    val orderNumber: String,
    val paymentGateway: PaymentGateway,
    val amountFcfa: Long,
    val payerPhoneOrCard: String,
    val timestampFormatted: String,
    val status: String = "APPROUVÉ",
    val authorizationCode: String
)
