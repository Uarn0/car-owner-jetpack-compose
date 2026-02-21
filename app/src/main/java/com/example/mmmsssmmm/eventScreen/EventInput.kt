package com.example.mmmsssmmm.eventScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventInput(onSave: (name: String, type: Int, date: String) -> Unit) {
    val options = listOf("Repair", "Trip", "Service")
    var selected by remember { mutableStateOf(options.first()) }
    Column {

        Text("Please select the event")
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(60.dp)
                    .selectable(
                        selected = (option == selected),
                        onClick = { selected = option },
                        role = Role.RadioButton
                    )
            ) {
                RadioButton(selected = (option == selected), onClick = null)
                Text(
                    option,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 30.dp)
                )
            }
        }
        Button(
            onClick = {
                onSave(
                    selected, whatType(selected), whatTime()
                )
            }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Text("Add")
        }


    }
}

fun whatType(event: String): Int {
    return when (event) {
        "Repair" -> 0
        "Trip" -> 1
        else -> 2
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun whatTime(): String {
    return OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}