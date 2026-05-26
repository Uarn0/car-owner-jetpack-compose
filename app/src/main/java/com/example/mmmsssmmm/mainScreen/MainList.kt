package com.example.mmmsssmmm.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mmmsssmmm.data.fulldetails.FullVehicleDetails
import com.example.mmmsssmmm.ui.vehicles.CarFilter

@Composable
fun MainList(
    vehicles: List<FullVehicleDetails>,
    currentFilter: CarFilter, // ДОДАНО
    onFilterSelected: (CarFilter) -> Unit, // ДОДАНО
    onDetailsClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onDelete: (Long) -> Unit,
    onStatsClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SmallFloatingActionButton(onClick = onStatsClick) {
                    Icon(Icons.Default.QueryStats, contentDescription = "See Stats")
                }

                FloatingActionButton(onClick = onAddClick) {
                    Icon(Icons.Default.Add, contentDescription = "Add Vehicle")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf(
                    CarFilter.ALL to "Всі авто",
                    CarFilter.JDM to "Тільки JDM (IN)",
                    CarFilter.MODERN_OR_LARGE to "Рік > 15 або Бак > 60 (OR)",
                    CarFilter.WITH_SERVICE to "Були на СТО (Підзапит)"
                )

                items(filters) { (filter, label) ->
                    FilterChip(
                        selected = currentFilter == filter,
                        onClick = { onFilterSelected(filter) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = vehicles,
                    key = { it.vehicle.globalVehicleId }
                ) { details ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = "No Image",
                                    modifier = Modifier.size(100.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "${details.brandName} ${details.modelName}",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        text = "${details.typeName} • ${details.vehicle.manufactureYear} рік",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Держ. номер: ${details.vehicle.plateNumber}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(onClick = { onDelete(details.vehicle.globalVehicleId) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                                    Spacer(Modifier.width(4.dp))
                                    Text("Видалити")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { onDetailsClick(details.vehicle.globalVehicleId) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Details")
                                    Spacer(Modifier.width(4.dp))
                                    Text("Деталі")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}