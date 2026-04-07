package com.example.mmmsssmmm.eventScreen

import androidx.compose.foundation.layout.*
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
import com.example.mmmsssmmm.data.entity.EventEntity
import com.example.mmmsssmmm.domain.item.VehicleHistoryItem

@Composable
fun EventList(
    events: List<EventEntity>,
    onEventClick: (Long) -> Unit,
    onAddEventClick: () -> Unit,
    onDeleteEventClick: (Long) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (events.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Історія порожня. Створіть першу подію!")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(events, key = { it.globalEventId }) { event ->
                    Card(
                        onClick = { onEventClick(event.globalEventId) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Подія", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Одометр: ${event.odometer} км", style = MaterialTheme.typography.bodyMedium)
                                Text(event.date.take(10), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                            IconButton(onClick = { onDeleteEventClick(event.globalEventId) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Event", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onAddEventClick,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Spacer(Modifier.width(8.dp))
            Text("Створити подію")
        }
    }
}
