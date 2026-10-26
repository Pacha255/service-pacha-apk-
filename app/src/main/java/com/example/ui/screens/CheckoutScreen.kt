package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import com.example.ui.components.CitySelectionDialog
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentGateway
import com.example.data.model.PaymentStatus
import com.example.data.repository.MarketplaceRepository
import com.example.ui.MarketplaceViewModel
import com.example.ui.theme.PachaBorder
import com.example.ui.theme.PachaEmerald
import com.example.ui.theme.PachaEmeraldSoft
import com.example.ui.theme.PachaGold
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val couponPercent by viewModel.couponDiscountPercent.collectAsState()
    val paymentStatus by viewModel.paymentStatus.collectAsState()
    val paymentProcessingMessage by viewModel.paymentProcessingMessage.collectAsState()

    BackHandler(enabled = paymentStatus != PaymentStatus.PROCESSING) {
        viewModel.navigateBack()
    }

    var recipientName by remember { mutableStateOf(userProfile.fullName) }
    var phone by remember { mutableStateOf(userProfile.phone) }
    var neighborhood by remember { mutableStateOf("") }
    var city by remember { mutableStateOf(userProfile.currentCity) }
    var showCheckoutCityDialog by remember { mutableStateOf(false) }
    var addressStepSubmitted by remember { mutableStateOf(false) }
    var addressValidationError by remember { mutableStateOf<String?>(null) }

    var selectedGateway by remember { mutableStateOf(PaymentGateway.WAVE) }
    var mobileMoneyPhone by remember { mutableStateOf(userProfile.phone) }
    var orangeOtpCode by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("4532 8912 3456 7890") }
    var cardExpiry by remember { mutableStateOf("12/28") }
    var cardCvv by remember { mutableStateOf("381") }
    var cashPreparationNote by remember { mutableStateOf("Prévoir l'appoint en espèces") }

    val subtotal = cartItems.sumOf { it.totalPriceFcfa }
    val discount = if (couponPercent > 0) (subtotal * couponPercent) / 100 else 0
    val shipping = if (subtotal > 150000 || subtotal == 0L) 0L else 2000L
    val total = subtotal - discount + shipping

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Règlement Sécurisé",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "SERVICE PACHA Pay • Chiffrement 256 bits",
                            color = PachaGold,
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { if (paymentStatus != PaymentStatus.PROCESSING) viewModel.navigateBack() },
                        modifier = Modifier.testTag("checkout_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PachaOrange)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 14.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Total TTC à payer", fontSize = 11.sp, color = PachaTextSecondary)
                        Text(
                            text = MarketplaceRepository.formatFcfa(total),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PachaOrange
                        )
                    }

                    Button(
                        onClick = {
                            val payerIdentifier = when (selectedGateway) {
                                PaymentGateway.CREDIT_CARD -> cardNumber
                                PaymentGateway.CASH_ON_DELIVERY -> "Espèces / Mobile Money à la livraison"
                                else -> mobileMoneyPhone
                            }
                            viewModel.processPaymentAndPlaceOrder(
                                gateway = selectedGateway,
                                payerIdentifier = payerIdentifier,
                                address = neighborhood,
                                neighborhood = neighborhood,
                                city = city,
                                instructions = "",
                                phone = phone,
                                recipientName = recipientName
                            )
                        },
                        enabled = paymentStatus != PaymentStatus.PROCESSING && cartItems.isNotEmpty() && addressStepSubmitted,
                        colors = ButtonDefaults.buttonColors(containerColor = PachaOrange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("confirm_order_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (selectedGateway == PaymentGateway.CASH_ON_DELIVERY) "Valider la commande" else "Payer ${MarketplaceRepository.formatFcfa(total)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
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
            // 1. Order Summary Pill
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Panier (${cartItems.size} articles)", fontSize = 12.sp, color = PachaTextSecondary)
                        Text(text = MarketplaceRepository.formatFcfa(subtotal), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    if (discount > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Remise code promo (-$couponPercent%)", fontSize = 12.sp, color = PachaOrange)
                            Text(text = "-${MarketplaceRepository.formatFcfa(discount)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PachaOrange)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Frais de livraison express", fontSize = 12.sp, color = PachaTextSecondary)
                        Text(
                            text = if (shipping == 0L) "Gratuit (Offert)" else MarketplaceRepository.formatFcfa(shipping),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (shipping == 0L) PachaEmerald else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    HorizontalDivider(color = PachaBorder)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Montant Net", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            text = MarketplaceRepository.formatFcfa(total),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            color = PachaOrange
                        )
                    }
                }
            }

            // 2. Delivery Address Form & Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (!addressStepSubmitted) PachaOrange else PachaEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (!addressStepSubmitted) "1. Adresse de livraison (Obligatoire)" else "1. Adresse validée",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (!addressStepSubmitted) PachaOrange else PachaEmerald
                            )
                        }
                        if (addressStepSubmitted) {
                            androidx.compose.material3.TextButton(onClick = { addressStepSubmitted = false }) {
                                Text("Modifier", fontSize = 12.sp, color = PachaOrange)
                            }
                        }
                    }

                    if (!addressStepSubmitted) {
                        OutlinedTextField(
                            value = recipientName,
                            onValueChange = { recipientName = it },
                            label = { Text("1. Nom et prénom *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("checkout_recipient_name"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PachaOrange)
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("2. Numéro de téléphone *") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("checkout_phone"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PachaOrange)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = city,
                                onValueChange = {},
                                label = { Text("3. Ville *") },
                                singleLine = true,
                                readOnly = true,
                                modifier = Modifier.weight(1f).testTag("checkout_city").clickable { showCheckoutCityDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PachaOrange),
                                trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Rechercher", tint = PachaOrange) }
                            )

                            // City Selection Dialog
                            CitySelectionDialog(
                                show = showCheckoutCityDialog,
                                currentCity = city,
                                onDismiss = { showCheckoutCityDialog = false },
                                onCitySelected = { cityName, countryName ->
                                    city = cityName
                                    showCheckoutCityDialog = false
                                }
                            )
                            OutlinedTextField(
                                value = neighborhood,
                                onValueChange = { neighborhood = it },
                                label = { Text("4. Quartier *") },
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("checkout_neighborhood"),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PachaOrange)
                            )
                        }

                        if (addressValidationError != null) {
                            Text(
                                text = addressValidationError!!,
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                if (recipientName.isBlank() || phone.isBlank() || city.isBlank() || neighborhood.isBlank()) {
                                    addressValidationError = "Tous les champs sont obligatoires."
                                } else if (phone.count { it.isDigit() } < 8) {
                                    addressValidationError = "Veuillez entrer un numéro de téléphone valide."
                                } else {
                                    addressValidationError = null
                                    addressStepSubmitted = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PachaOrange),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("continue_address_btn")
                        ) {
                            Text(text = "Continuer", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(PachaEmerald.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                                .border(1.dp, PachaEmerald.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "Récapitulatif de l'adresse de livraison :", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = PachaOrange)
                            HorizontalDivider(color = PachaBorder)
                            Text(text = "• Nom et prénom : $recipientName", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text(text = "• Téléphone : $phone", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text(text = "• Ville : $city", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text(text = "• Quartier : $neighborhood", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            // 3. Mobile Money & Payment Method Selection Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null,
                                tint = PachaOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Choisissez votre mode de paiement",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Gateway list
                    PaymentGateway.entries.forEach { gateway ->
                        val isSelected = selectedGateway == gateway
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clickable { selectedGateway = gateway }
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) gateway.brandColor else PachaBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .testTag("pay_gateway_${gateway.id}"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) gateway.brandColor.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedGateway = gateway },
                                        colors = RadioButtonDefaults.colors(selectedColor = gateway.brandColor)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(gateway.brandColor, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = gateway.displayName,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = if (isSelected) PachaOrange else MaterialTheme.colorScheme.onSurface
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = gateway.brandColor.copy(alpha = 0.15f)
                                            ) {
                                                Text(
                                                    text = gateway.feeDescription,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = gateway.brandColor,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = gateway.operatorSubtext,
                                            fontSize = 11.sp,
                                            color = PachaTextSecondary
                                        )
                                    }
                                }

                                // Expandable Operator Dedicated Fields
                                AnimatedVisibility(
                                    visible = isSelected,
                                    enter = expandVertically() + fadeIn(),
                                    exit = shrinkVertically() + fadeOut()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp, start = 8.dp, end = 8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        HorizontalDivider(color = PachaBorder)

                                        when (gateway) {
                                            PaymentGateway.WAVE, PaymentGateway.MTN_MOMO, PaymentGateway.MOOV_MONEY -> {
                                                Text(
                                                    text = "Numéro de compte ${gateway.displayName} :",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                OutlinedTextField(
                                                    value = mobileMoneyPhone,
                                                    onValueChange = { mobileMoneyPhone = it },
                                                    leadingIcon = {
                                                        Icon(
                                                            imageVector = Icons.Default.Phone,
                                                            contentDescription = null,
                                                            tint = gateway.brandColor,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                                    singleLine = true,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = gateway.brandColor)
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = gateway.brandColor.copy(alpha = 0.08f),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.Info, contentDescription = null, tint = gateway.brandColor, modifier = Modifier.size(16.dp))
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(
                                                            text = gateway.ussdInstruction,
                                                            fontSize = 11.sp,
                                                            color = PachaOrange,
                                                            lineHeight = 15.sp
                                                        )
                                                    }
                                                }
                                            }

                                            PaymentGateway.ORANGE_MONEY -> {
                                                Text(
                                                    text = "Numéro Orange Money :",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                OutlinedTextField(
                                                    value = mobileMoneyPhone,
                                                    onValueChange = { mobileMoneyPhone = it },
                                                    leadingIcon = {
                                                        Icon(Icons.Default.Phone, contentDescription = null, tint = gateway.brandColor, modifier = Modifier.size(18.dp))
                                                    },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                                    singleLine = true,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = gateway.brandColor)
                                                )
                                                OutlinedTextField(
                                                    value = orangeOtpCode,
                                                    onValueChange = { orangeOtpCode = it },
                                                    label = { Text("Code d'autorisation (Composez #144#391#)") },
                                                    placeholder = { Text("Ex: 4892") },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                    singleLine = true,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = gateway.brandColor)
                                                )
                                            }

                                            PaymentGateway.CREDIT_CARD -> {
                                                OutlinedTextField(
                                                    value = cardNumber,
                                                    onValueChange = { cardNumber = it },
                                                    label = { Text("Numéro de carte Visa / Mastercard") },
                                                    leadingIcon = {
                                                        Icon(Icons.Default.CreditCard, contentDescription = null, tint = PachaOrange)
                                                    },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                    singleLine = true,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PachaOrange)
                                                )
                                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    OutlinedTextField(
                                                        value = cardExpiry,
                                                        onValueChange = { cardExpiry = it },
                                                        label = { Text("MM/AA") },
                                                        singleLine = true,
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                    OutlinedTextField(
                                                        value = cardCvv,
                                                        onValueChange = { cardCvv = it },
                                                        label = { Text("CVV") },
                                                        singleLine = true,
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                }
                                            }

                                            PaymentGateway.CASH_ON_DELIVERY -> {
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = PachaEmeraldSoft,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PachaEmerald, modifier = Modifier.size(20.dp))
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Column {
                                                            Text(
                                                                text = "Règlement sécurisé lors de la remise en main propre",
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 12.sp,
                                                                color = PachaEmerald
                                                            )
                                                            Text(
                                                                text = "Vous pouvez régler au livreur en espèces ou via Wave / Orange Money / MTN MoMo après ouverture et inspection du colis.",
                                                                fontSize = 11.sp,
                                                                color = PachaOrange,
                                                                lineHeight = 15.sp
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Trust & Security Badges
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = PachaOrange.copy(alpha = 0.05f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = PachaOrange,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Transactions sécurisées selon les normes bancaires et opérateurs télécoms d'Afrique. Aucune donnée sensible n'est stockée en clair.",
                        fontSize = 11.sp,
                        color = PachaOrange,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Payment Processing Modal
    if (paymentStatus == PaymentStatus.PROCESSING || paymentStatus == PaymentStatus.FAILED) {
        AlertDialog(
            onDismissRequest = { if (paymentStatus == PaymentStatus.FAILED) viewModel.resetPaymentStatus() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (paymentStatus == PaymentStatus.FAILED) Icons.Default.Cancel else Icons.Default.Lock, 
                        contentDescription = null, 
                        tint = if (paymentStatus == PaymentStatus.FAILED) Color.Red else selectedGateway.brandColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (paymentStatus == PaymentStatus.FAILED) "Erreur de paiement" else "Validation du paiement",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (paymentStatus == PaymentStatus.PROCESSING) {
                        CircularProgressIndicator(
                            color = selectedGateway.brandColor,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(46.dp)
                        )
                        Text(
                            text = paymentProcessingMessage,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = PachaOrange,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = "Montant : ${MarketplaceRepository.formatFcfa(total)}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = selectedGateway.brandColor
                        )
                    } else {
                         Text(
                            text = "Le paiement a échoué. Veuillez réessayer.",
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }
            },
            confirmButton = {
                if (paymentStatus == PaymentStatus.FAILED) {
                    Button(onClick = { viewModel.resetPaymentStatus() }) {
                        Text("Réessayer")
                    }
                }
            }
        )
    }
}
