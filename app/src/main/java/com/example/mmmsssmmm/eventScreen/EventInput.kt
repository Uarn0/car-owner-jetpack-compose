package com.example.mmmsssmmm.eventScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mmmsssmmm.ui.events.EventsViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventInput(
    vm: EventsViewModel,
    onCancel: () -> Unit,
    onEventCreated: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Новий запис",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Створіть подію для цього дня. Деталі (заправки, ремонти, маршрути) ви зможете додати на наступному екрані.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = vm.odometer,
            onValueChange = { if (it.all { char -> char.isDigit() }) vm.odometer = it },
            label = { Text("Поточний одометр (км)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(32.dp))

        Row(Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f).height(56.dp)
            ) {
                Text("Скасувати")
            }

            Spacer(Modifier.width(16.dp))

            Button(
                onClick = {
                    val time = whatTime()
                    val odo = vm.odometer.toIntOrNull() ?: 0

                    vm.createBaseEvent(
                        name = "Подія",
                        date = time,
                        odometer = odo,
                        totalCost = 0.0,
                        onSuccess = { newEventId ->
                            onEventCreated(newEventId)
                        }
                    )
                },
                modifier = Modifier.weight(1f).height(56.dp),
                enabled = vm.odometer.isNotBlank()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Створити")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun whatTime(): String {
    return OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}