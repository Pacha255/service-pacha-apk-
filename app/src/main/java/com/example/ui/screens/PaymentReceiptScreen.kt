package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.MarketplaceRepository
import com.example.ui.MarketplaceViewModel
import com.example.ui.Screen
import com.example.ui.components.CategoryProductVisual
import com.example.ui.theme.PachaBorder
import com.example.ui.theme.PachaEmerald
import com.example.ui.theme.PachaEmeraldSoft
import com.example.ui.theme.PachaGold
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentReceiptScreen(
    orderId: String,
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val order = viewModel.getOrderById(orderId)
    val receipt by viewModel.lastReceipt.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    if (order == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Reçu de commande introuvable")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reçu de Paiement Numérique",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PachaOrange)
            )
        },
        containerColor = Color(0xFFF8FAFC),
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.navigateTo(Screen.OrderTracking(order.id)) },
                        colors = ButtonDefaults.buttonColors(containerColor = PachaOrange),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Suivre la livraison en direct", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { viewModel.navigateTo(Screen.Home) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = PachaOrange)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Retour à l'accueil", fontWeight = FontWeight.Bold, color = PachaOrange)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Success Header Animation Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = PachaEmeraldSoft
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(PachaEmerald, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Paiement Confirmé avec Succès !",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = PachaEmerald
                    )
                    Text(
                        text = "Merci pour votre confiance sur SERVICE PACHA",
                        fontSize = 12.sp,
                        color = PachaOrange
                    )
                }
            }

            // Digital Invoice Paper Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PachaBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Header of the receipt
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = PachaGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SERVICE PACHA PAY",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = PachaOrange
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PachaEmeraldSoft
                        ) {
                            Text(
                                text = "APPROUVÉ",
                                color = PachaEmerald,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = PachaBorder)

                    // Transaction details
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "N° de Commande", fontSize = 12.sp, color = Color.Black)
                        Text(text = order.orderNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Réf. Transaction", fontSize = 12.sp, color = Color.Black)
                        Text(text = order.transactionId, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Moyen de règlement", fontSize = 12.sp, color = Color.Black)
                        Text(text = order.paymentMethodName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Date & Heure", fontSize = 12.sp, color = Color.Black)
                        Text(text = receipt?.timestampFormatted ?: order.dateFormatted, fontSize = 12.sp, color = Color.Black)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Bénéficiaire", fontSize = 12.sp, color = Color.Black)
                        Text(text = "SERVICE PACHA e.commerce", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    }

                    HorizontalDivider(color = PachaBorder)

                    // Purchased Items
                    Text(
                        text = "Articles facturés (${order.items.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.Black
                    )

                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                CategoryProductVisual(categoryId = item.product.categoryId, title = item.product.title, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = item.product.title, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.Black)
                                    Text(text = "Qté : ${item.quantity}", fontSize = 11.sp, color = Color.Black)
                                }
                            }
                            Text(text = MarketplaceRepository.formatFcfa(item.totalPriceFcfa), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                        }
                    }

                    HorizontalDivider(color = PachaBorder)

                    // Totals
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Sous-total", fontSize = 12.sp, color = Color.Black)
                        Text(text = MarketplaceRepository.formatFcfa(order.subtotalFcfa), fontSize = 12.sp, color = Color.Black)
                    }
                    if (order.discountFcfa > 0) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Remise coupon", fontSize = 12.sp, color = Color.Black)
                            Text(text = "-${MarketplaceRepository.formatFcfa(order.discountFcfa)}", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Frais de livraison", fontSize = 12.sp, color = Color.Black)
                        Text(text = if (order.shippingFeeFcfa == 0L) "Gratuit" else MarketplaceRepository.formatFcfa(order.shippingFeeFcfa), fontSize = 12.sp, color = Color.Black)
                    }

                    HorizontalDivider(color = PachaOrange, thickness = 1.5.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Total Débité TTC", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.Black)
                        Text(
                            text = MarketplaceRepository.formatFcfa(order.totalFcfa),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            // Quick Receipt Actions: Share / Download
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { /* simulated */ },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Télécharger PDF", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = { /* simulated */ },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Partager reçu", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
