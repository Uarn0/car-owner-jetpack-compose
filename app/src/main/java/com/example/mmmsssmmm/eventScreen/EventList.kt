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

@Composable
fun EventList(
    events: List<IEvent>,
    onAddClick: () -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
        ) {
            items(
                items = events,
                key = { it.id }
            ) { event ->

                when (event) {
                    is RepairEvent,
                    is TripEvent,
                    is ServiceEvent -> {
                        Text("Event: ${event.name}\n${event.timeWhenAdded}")
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { onDeleteClick(event.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                            Spacer(Modifier.width(8.dp))
                            Text("Delete")
                        }
                    }
                }
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
