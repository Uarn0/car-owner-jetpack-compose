package com.example.mmmsssmmm.eventScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mmmsssmmm.domain.eventModel.IEvent
import com.example.mmmsssmmm.domain.eventModel.RepairEvent
import com.example.mmmsssmmm.domain.eventModel.TripEvent
import com.example.mmmsssmmm.domain.eventModel.ServiceEvent
import com.example.mmmsssmmm.domain.item.VehicleHistoryItem

@Composable
fun EventList(
    events: List<VehicleHistoryItem>,
    onAddClick: () -> Unit,
    onDeleteClick: (Long) -> Unit,
) {
    if (events.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Подій поки немає. Додайте першу!")
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 72.dp)
            ) {
                items(
                    items = events,
                    key = { it.eventId }
                ) { event ->

                    when (event) {
                        is VehicleHistoryItem.Trip -> {
                            TripCard(
                                trip = event,
                                onDelete = { onDeleteClick(event.eventId) }
                            )
                        }

                        is VehicleHistoryItem.Fueling -> {
                            FuelingCard(
                                fueling = event,
                                onDelete = { onDeleteClick(event.eventId) }
                            )
                        }

                        is VehicleHistoryItem.Service -> {
                            ServiceCard(
                                service = event,
                                onDelete = { onDeleteClick(event.eventId) }
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }

            Button(
                onClick = onAddClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(60.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(Modifier.width(8.dp))
                Text("Add")
            }
        }
    }
}
