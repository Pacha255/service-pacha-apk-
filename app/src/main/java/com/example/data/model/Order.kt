package com.example.data.model

enum class OrderStatus(val label: String) {
    CONFIRMED("Confirmée"),
    PROCESSING("En préparation"),
    SHIPPED("Expédiée"),
    DELIVERING("En cours de livraison"),
    DELIVERED("Livrée"),
    CANCELLED("Annulée")
}

data class TrackingStep(
    val title: String,
    val description: String,
    val time: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

data class Order(
    val id: String,
    val orderNumber: String,
    val dateFormatted: String,
    val status: OrderStatus,
    val items: List<CartItem>,
    val subtotalFcfa: Long,
    val shippingFeeFcfa: Long,
    val discountFcfa: Long,
    val totalFcfa: Long,
    val recipientName: String,
    val deliveryAddress: String,
    val deliveryNeighborhood: String,
    val deliveryCity: String,
    val deliveryInstructions: String = "",
    val phoneNumber: String,
    val paymentMethodName: String,
    val trackingSteps: List<TrackingStep>,
    val transactionId: String = "TXN-${System.currentTimeMillis() % 1000000}",
    val isPaid: Boolean = true,
    val paymentGatewayId: String = "wave"
)
