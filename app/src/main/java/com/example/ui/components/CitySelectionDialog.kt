package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PachaOrange
import com.example.ui.theme.PachaTextSecondary

val ivoryCoastCitiesWithCountries = listOf(
    Pair("Abidjan", "Côte d'Ivoire"),
    Pair("Bouaké", "Côte d'Ivoire"),
    Pair("Korhogo", "Côte d'Ivoire"),
    Pair("Sinématiali", "Côte d'Ivoire"),
    Pair("Ferkessédougou", "Côte d'Ivoire"),
    Pair("Katiola", "Côte d'Ivoire"),
    Pair("Boundiali", "Côte d'Ivoire"),
    Pair("Odienné", "Côte d'Ivoire"),
    Pair("Séguéla", "Côte d'Ivoire"),
    Pair("Man", "Côte d'Ivoire"),
    Pair("Daloa", "Côte d'Ivoire"),
    Pair("Yamoussoukro", "Côte d'Ivoire"),
    Pair("San-Pédro", "Côte d'Ivoire"),
    Pair("Gagnoa", "Côte d'Ivoire"),
    Pair("Divo", "Côte d'Ivoire"),
    Pair("Soubré", "Côte d'Ivoire"),
    Pair("Abengourou", "Côte d'Ivoire"),
    Pair("Agnibilékrou", "Côte d'Ivoire"),
    Pair("Bondoukou", "Côte d'Ivoire"),
    Pair("Daoukro", "Côte d'Ivoire"),
    Pair("Grand-Bassam", "Côte d'Ivoire"),
    Pair("Bingerville", "Côte d'Ivoire"),
    Pair("Dabou", "Côte d'Ivoire"),
    Pair("Anyama", "Côte d'Ivoire"),
    Pair("Adzopé", "Côte d'Ivoire"),
    Pair("Aboisso", "Côte d'Ivoire"),
    Pair("Tiassalé", "Côte d'Ivoire"),
    Pair("Toumodi", "Côte d'Ivoire"),
    Pair("Issia", "Côte d'Ivoire"),
    Pair("Duékoué", "Côte d'Ivoire"),
    Pair("Guiglo", "Côte d'Ivoire"),
    Pair("Danané", "Côte d'Ivoire"),
    Pair("Touba", "Côte d'Ivoire"),
    Pair("Bouna", "Côte d'Ivoire"),
    Pair("Doropo", "Côte d'Ivoire"),
    Pair("Téhini", "Côte d'Ivoire"),
    Pair("Kong", "Côte d'Ivoire"),
    Pair("M'Bengué", "Côte d'Ivoire"),
    Pair("Dikodougou", "Côte d'Ivoire"),
    Pair("Niakaramandougou", "Côte d'Ivoire"),
    Pair("Sinfra", "Côte d'Ivoire"),
    Pair("Lakota", "Côte d'Ivoire"),
    Pair("Oumé", "Côte d'Ivoire"),
    Pair("Jacqueville", "Côte d'Ivoire"),
    Pair("Grand-Lahou", "Côte d'Ivoire"),
    Pair("Sassandra", "Côte d'Ivoire"),
    Pair("Tabou", "Côte d'Ivoire"),
    Pair("Dimbokro", "Côte d'Ivoire"),
    Pair("Bongouanou", "Côte d'Ivoire"),
    Pair("Guéyo", "Côte d'Ivoire"),
    Pair("Buyo", "Côte d'Ivoire"),
    Pair("Zuénoula", "Côte d'Ivoire"),
    Pair("Vavoua", "Côte d'Ivoire"),
    Pair("Kounahiri", "Côte d'Ivoire"),
    Pair("Sakassou", "Côte d'Ivoire"),
    Pair("Béoumi", "Côte d'Ivoire"),
    Pair("Botro", "Côte d'Ivoire"),
    Pair("Diabo", "Côte d'Ivoire"),
    Pair("Mankono", "Côte d'Ivoire"),
    Pair("Koro", "Côte d'Ivoire"),
    Pair("Madinani", "Côte d'Ivoire"),
    Pair("Minignan", "Côte d'Ivoire"),
    Pair("Ouangolodougou", "Côte d'Ivoire"),
    Pair("Tengrela", "Côte d'Ivoire"),
    Pair("Kouibly", "Côte d'Ivoire"),
    Pair("Bangolo", "Côte d'Ivoire"),
    Pair("Facobly", "Côte d'Ivoire"),
    Pair("Bin-Houyé", "Côte d'Ivoire"),
    Pair("Zouan-Hounien", "Côte d'Ivoire"),
    Pair("Sipilou", "Côte d'Ivoire"),
    Pair("Biankouma", "Côte d'Ivoire"),
    Pair("Kouassi-Kouassikro", "Côte d'Ivoire"),
    Pair("Prikro", "Côte d'Ivoire"),
    Pair("M'Bahiakro", "Côte d'Ivoire"),
    Pair("Arrah", "Côte d'Ivoire"),
    Pair("Bocanda", "Côte d'Ivoire"),
    Pair("Djékanou", "Côte d'Ivoire"),
    Pair("Tiebissou", "Côte d'Ivoire"),
    Pair("Dakar", "Sénégal"),
    Pair("Douala", "Cameroun"),
    Pair("Yaoundé", "Cameroun"),
    Pair("Bamako", "Mali"),
    Pair("Cotonou", "Bénin"),
    Pair("Lomé", "Togo")
).sortedBy { it.first }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionDialog(
    show: Boolean,
    currentCity: String,
    onDismiss: () -> Unit,
    onCitySelected: (String, String) -> Unit
) {
    if (show) {
        var searchQuery by remember { mutableStateOf("") }
        val filteredCities = remember(searchQuery) {
            if (searchQuery.isBlank()) {
                ivoryCoastCitiesWithCountries
            } else {
                ivoryCoastCitiesWithCountries.filter {
                    it.first.contains(searchQuery, ignoreCase = true) ||
                    it.second.contains(searchQuery, ignoreCase = true)
                }
            }
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Choisir la ville de livraison",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Rechercher une ville...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = PachaOrange)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("city_search_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PachaOrange)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filteredCities) { (cityName, countryName) ->
                            val isSelected = cityName == currentCity
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCitySelected(cityName, countryName)
                                        onDismiss()
                                    },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) PachaOrange.copy(alpha = 0.1f) else Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = cityName,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) PachaOrange else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = countryName,
                                        fontSize = 12.sp,
                                        color = PachaTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Fermer", color = PachaOrange, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
