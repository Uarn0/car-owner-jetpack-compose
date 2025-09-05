package com.example.mmmsssmmm.mainScreen

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mmmsssmmm.data.VehiclesEntity

@Composable
fun MainList(
    vehicles: List<VehiclesEntity>,
    onDetailsClick: (VehiclesEntity) -> Unit,
    onAddClick: () -> Unit,
    onDelete: (VehiclesEntity) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
        ) {
            items(
                items = vehicles,
                key = { it.id }
            ) { vehicle ->
                val stringType = typeVehicleToString(vehicle.type)

                Image(
                    painter = painterResource(id = vehicle.image),
                    contentDescription = stringType
                )
                Text("${vehicle.name}\n${stringType}", modifier = Modifier.padding(8.dp))

                Button(onClick = { onDelete(vehicle) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                    Spacer(Modifier.width(8.dp))
                    Text("Remove")
                }
                Button(onClick = { onDetailsClick(vehicle) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Details")
                    Spacer(Modifier.width(8.dp))
                    Text("Details")
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

private fun typeVehicleToString(type: Int): String =
    when (type) {
        0 -> "Car"
        1 -> "Minibus"
        else -> "Motorcycle"
    }