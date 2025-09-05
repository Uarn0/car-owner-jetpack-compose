package com.example.mmmsssmmm.eventScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.mmmsssmmm.data.EventEntity

@Composable
fun EventList(events: List<EventEntity>, onAddClick: () -> Unit, onDeleteClick: (EventEntity) -> Unit) {
    Text("Event list::")
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn (modifier = Modifier.fillMaxSize().padding(bottom = 72.dp)){
            items(
                items = events,
                key = { it.name + "_" + it.time }
            ) { event ->
            Text("Event::${event.name}\n${event.time}")

                Button(onClick = { onDeleteClick(event) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Spacer(Modifier.width(8.dp))
                    Text("Delete")
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