package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PachaGold
import com.example.ui.theme.PachaOrange

@Composable
fun PachaBrandLogo(
    modifier: Modifier = Modifier,
    logoSize: Dp = 36.dp,
    titleColor: Color = Color.White,
    tagline: String? = "Marketplace Afrique",
    showTagline: Boolean = true,
    showText: Boolean = true,
    fontSize: TextUnit = 16.sp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // Logo Container - Preserves aspect ratio with ContentScale.Fit, never distorted
        Surface(
            shape = RoundedCornerShape(9.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.size(logoSize)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pacha_launcher_icon_1792883985504),
                    contentDescription = "Logo Officiel SERVICE PACHA",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .clip(RoundedCornerShape(7.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }

        if (showText) {
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "PACHA",
                        color = titleColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = fontSize,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SERVICE",
                        color = PachaGold,
                        fontWeight = FontWeight.Black,
                        fontSize = fontSize,
                        letterSpacing = 0.8.sp
                    )
                }
                if (showTagline && tagline != null) {
                    Text(
                        text = tagline,
                        color = titleColor.copy(alpha = 0.85f),
                        fontSize = (fontSize.value * 0.625f).sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
