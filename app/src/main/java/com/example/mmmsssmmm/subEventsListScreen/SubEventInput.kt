package com.example.mmmsssmmm.subEventsListScreen

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mmmsssmmm.ui.events.SubEventViewModel

@Composable
fun SubEventInput(
    vm: SubEventViewModel,
    onDismiss: () -> Unit
) {
    val options = listOf("Trip", "Fueling", "Service")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Додати деталі",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TabRow(selectedTabIndex = selectedTabIndex) {
            options.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        when (options[selectedTabIndex]) {
            "Trip" -> {
                OutlinedTextField(value = vm.startPoint, onValueChange = { vm.startPoint = it }, label = { Text("Звідки") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = vm.endPoint, onValueChange = { vm.endPoint = it }, label = { Text("Куди") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = vm.distance,
                    onValueChange = { vm.updateTripDistance(it) },
                    label = { Text("Дистанція (км)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }

            "Fueling" -> {
                OutlinedTextField(
                    value = vm.volume,
                    onValueChange = { vm.updateFuelingData(newVol = it) },
                    label = { Text("Літри") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = vm.pricePerLiter,
                    onValueChange = { vm.updateFuelingData(newPrice = it) },
                    label = { Text("Ціна за літр") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }

            "Service" -> {
                OutlinedTextField(value = vm.workTitle, onValueChange = { vm.workTitle = it }, label = { Text("Опис робіт") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = vm.stationName, onValueChange = { vm.stationName = it }, label = { Text("Назва СТО") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = vm.totalCost,
                    onValueChange = { vm.totalCost = it.replace(',', '.') },
                    label = { Text("Вартість (₴)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        if (options[selectedTabIndex] != "Service") {
            Text(
                text = "Розрахована вартість: ${vm.totalCost} ₴",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
        }

        Row(Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f).height(56.dp)
            ) {
                Text("Скасувати")
            }

            Spacer(Modifier.width(16.dp))

            Button(
                onClick = {
                    when (options[selectedTabIndex]) {
                        "Trip" -> vm.addTrip(isBus = false)
                        "Fueling" -> vm.addFueling(fuelId = 1, isFull = true)
                        "Service" -> vm.addService()
                    }
                    onDismiss()
                },
                modifier = Modifier.weight(1f).height(56.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Додати")
            }
        }
    }
}