package com.example.mmmsssmmm.mainScreen

import androidx.compose.foundation.layout.Column
import com.example.mmmsssmmm.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun MainInput(onSave: (name: String, type: Int, image: Int) -> Unit) {
    var vehicleName by remember { mutableStateOf("") }
    val types = listOf("Car", "Motorcycle", "Minibus")
    var selectedType by remember { mutableStateOf(types[0]) }

    Column(Modifier.fillMaxSize()) {
        TextField(
            value = vehicleName,
            onValueChange = { vehicleName = it },
            label = { Text("Input the name of your vehicle") },
            placeholder = { Text("Enter name here") }
        )

        types.forEach { t ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(48.dp)
                    .selectable(
                        selected = selectedType == t,
                        onClick = { selectedType = t },
                        role = Role.RadioButton
                    )
            ) {
                RadioButton(selected = selectedType == t, onClick = null)
                Text(t, modifier = Modifier.padding(start = 12.dp))
            }
        }
        Button(onClick = {
            val typeInt = when (selectedType) {
                "Car" -> 0
                "Minibus" -> 1
                else -> 2
            }

            val image = when (typeInt) {
                0 -> R.drawable.ic_car
                1 -> R.drawable.ic_minibus
                else -> R.drawable.ic_moto
            }

            if (vehicleName.isNotBlank()) {
                onSave(vehicleName.trim(), typeInt, image)
            }
        }) {
            Icon(Icons.Default.Check, contentDescription = "Add")
            Spacer(Modifier.width(8.dp))
            Text("Add")
        }

    }
}
