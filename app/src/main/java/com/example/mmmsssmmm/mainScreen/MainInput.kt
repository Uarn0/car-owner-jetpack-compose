package com.example.mmmsssmmm.mainScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mmmsssmmm.data.dictionary.BrandDictEntity
import com.example.mmmsssmmm.data.dictionary.ModelDictEntity
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainInput(
    brands: List<BrandDictEntity>,
    models: List<ModelDictEntity>,
    onBrandSelected: (Long) -> Unit,
    onSave: (modelId: Long, year: Int, tank: Double, plate: String, imageUri: String?) -> Unit
) {
    var selectedBrand by remember { mutableStateOf<BrandDictEntity?>(null) }
    var selectedModel by remember { mutableStateOf<ModelDictEntity?>(null) }

    var brandExpanded by remember { mutableStateOf(false) }
    var modelExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }

    var year by remember { mutableStateOf("") }
    var tankCapacity by remember { mutableStateOf("") }
    var plateNumber by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                photoPickerLauncher.launch(
                    androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(40.dp))
                    Text("Додати фото машини")
                }
            }
        }


        ExposedDropdownMenuBox(
            expanded = brandExpanded,
            onExpandedChange = { brandExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedBrand?.brandName ?: "Оберіть марку",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )

            if (brands.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = brandExpanded,
                    onDismissRequest = { brandExpanded = false }
                ) {
                    brands.forEach { brand ->
                        DropdownMenuItem(
                            text = { Text(brand.brandName) },
                            onClick = {
                                selectedBrand = brand
                                selectedModel = null
                                year = ""
                                brandExpanded = false
                                onBrandSelected(brand.id)
                            }
                        )
                    }
                }
            }
        }


        if (selectedBrand != null) {
            ExposedDropdownMenuBox(
                expanded = modelExpanded,
                onExpandedChange = { modelExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedModel?.name ?: "Оберіть модель",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                if (models.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = modelExpanded,
                        onDismissRequest = { modelExpanded = false }
                    ) {
                        models.forEach { model ->
                            DropdownMenuItem(
                                text = { Text(model.name) },
                                onClick = {
                                    selectedModel = model
                                    year = ""
                                    modelExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }


        if (selectedModel != null) {
            ExposedDropdownMenuBox(
                expanded = yearExpanded,
                onExpandedChange = { yearExpanded = it }
            ) {
                OutlinedTextField(
                    value = if (year.isNotEmpty()) year else "Оберіть рік випуску",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Рік випуску") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val endYear = if (selectedModel!!.yearEnd >= selectedModel!!.yearStart && selectedModel!!.yearEnd <= currentYear) {
                    selectedModel!!.yearEnd
                } else {
                    currentYear
                }

                val availableYears = (selectedModel!!.yearStart..endYear).toList().reversed()

                if (availableYears.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = yearExpanded,
                        onDismissRequest = { yearExpanded = false }
                    ) {
                        availableYears.forEach { y ->
                            DropdownMenuItem(
                                text = { Text(y.toString()) },
                                onClick = {
                                    year = y.toString()
                                    yearExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = tankCapacity,
            onValueChange = { tankCapacity = it },
            label = { Text("Об'єм баку (літри)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = plateNumber,
            onValueChange = { plateNumber = it },
            label = { Text("Держ. номер") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (selectedModel != null && year.isNotBlank() && tankCapacity.isNotBlank()) {
                    onSave(
                        selectedModel!!.id,
                        year.toIntOrNull() ?: 0,
                        tankCapacity.toDoubleOrNull() ?: 0.0,
                        plateNumber.trim(),
                        imageUri?.toString()
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = selectedModel != null && year.isNotBlank()
        ) {
            Icon(Icons.Default.Check, contentDescription = "Add")
            Spacer(Modifier.width(8.dp))
            Text("Зберегти авто")
        }
    }
}