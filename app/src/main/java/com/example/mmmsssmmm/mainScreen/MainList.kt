package com.example.mmmsssmmm.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mmmsssmmm.data.fulldetails.FullVehicleDetails

@Composable
fun MainList(
    vehicles: List<FullVehicleDetails>,
    onDetailsClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onDelete: (Long) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Vehicle")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
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

                            if (details.vehicle.userImageUri != null) {
                                AsyncImage(
                                    model = details.vehicle.userImageUri,
                                    contentDescription = "User car photo",
                                    modifier = Modifier.size(100.dp)
                                )
                            } else {
                                val imageResId = context.resources.getIdentifier(
                                    details.imageResName, "drawable", context.packageName
                                )

                                if (imageResId != 0) {
                                    Image(
                                        painter = painterResource(id = imageResId),
                                        contentDescription = "${details.brandName} ${details.modelName}",
                                        modifier = Modifier.size(100.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.DirectionsCar,
                                        contentDescription = "No Image",
                                        modifier = Modifier.size(100.dp)
                                    )
                                }
                            }

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
                                Text("Remove")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { onDetailsClick(details.vehicle.globalVehicleId) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Details")
                                Spacer(Modifier.width(4.dp))
                                Text("Details")
                            }
                        }
                    }
                }
            }
        }
    }
}