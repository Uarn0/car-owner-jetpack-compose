package com.example.mmmsssmmm.subEventsListScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mmmsssmmm.domain.item.VehicleHistoryItem

@Composable
fun SubEventsListScreen(
    subEvents: List<VehicleHistoryItem>,
    onAddSubEventClick: () -> Unit,
    onDeleteSubEventClick: (VehicleHistoryItem) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        if (subEvents.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ця подія порожня. Додайте заправку, поїздку або сервіс.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 120.dp, start = 16.dp, end = 16.dp),
                contentPadding = PaddingValues(top = 16.dp)
            ) {
                items(
                    items = subEvents,
                ) { event ->
                    when (event) {
                        is VehicleHistoryItem.Trip -> {
                            TripCard(
                                trip = event,
                                onDelete = { onDeleteSubEventClick(event) }
                            )
                        }

                        is VehicleHistoryItem.Fueling -> {
                            FuelingCard(
                                fueling = event,
                                onDelete = { onDeleteSubEventClick(event) }
                            )
                        }

                        is VehicleHistoryItem.Service -> {
                            ServiceCard(
                                service = event,
                                onDelete = { onDeleteSubEventClick(event) }
                            )
                        }

                        is VehicleHistoryItem.Base -> {
                            Text(
                                "У цій події ще немає записів. Додайте щось!",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = onAddSubEventClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add SubEvent")
            Spacer(Modifier.width(8.dp))
            Text("Додати деталь")
        }
    }
}


@Composable
fun TripCard(trip: VehicleHistoryItem.Trip, onDelete: () -> Unit) {
    Spacer(modifier = Modifier.width(16.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Route,
                contentDescription = "Trip",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${trip.startPoint} ➡️ ${trip.endPoint}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Дистанція: ${trip.distanceKM} км",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Одометр: ${trip.odometer} км • Витрати: ${trip.totalCost} ₴",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(text = trip.date.take(10), style = MaterialTheme.typography.labelSmall)
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun FuelingCard(fueling: VehicleHistoryItem.Fueling, onDelete: () -> Unit) {
    Spacer(modifier = Modifier.width(16.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalGasStation,
                contentDescription = "Fueling",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Заправка ${if (fueling.isFullTank) "(Повний бак)" else ""}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${fueling.volumeLiters} л. по ${fueling.pricePerLiter} ₴",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Одометр: ${fueling.odometer} км • Всього: ${fueling.totalCost} ₴",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(text = fueling.date.take(10), style = MaterialTheme.typography.labelSmall)
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ServiceCard(service: VehicleHistoryItem.Service, onDelete: () -> Unit) {
    Spacer(modifier = Modifier.width(16.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Service",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.workTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "СТО: ${service.serviceStation}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Одометр: ${service.odometer} км • Вартість: ${service.totalCost} ₴",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(text = service.date.take(10), style = MaterialTheme.typography.labelSmall)
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}