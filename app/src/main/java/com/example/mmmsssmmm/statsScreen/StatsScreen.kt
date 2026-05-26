package com.example.mmmsssmmm.statsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mmmsssmmm.data.tuples.CTOTuple
import com.example.mmmsssmmm.data.tuples.CtoSubTab
import com.example.mmmsssmmm.data.tuples.DistanceSubTab
import com.example.mmmsssmmm.data.tuples.FuelSubTab
import com.example.mmmsssmmm.data.tuples.MostTraveledVehicleTuple
import com.example.mmmsssmmm.data.tuples.StatsTab
import com.example.mmmsssmmm.data.tuples.TotalCostForFuelTuple
import com.example.mmmsssmmm.statsScreen.statCards.CTOStatsCard
import com.example.mmmsssmmm.statsScreen.statCards.DistanceStatsCard
import com.example.mmmsssmmm.statsScreen.statCards.FuelStatCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(vm: StatsViewModel) {
    val state = vm.uiState.collectAsStateWithLifecycle().value

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { vm.setTab(StatsTab.FUEL) },
                colors = ButtonDefaults.buttonColors(containerColor = if (state.activeTab == StatsTab.FUEL) MaterialTheme.colorScheme.primary else Color.LightGray)
            ) { Text("Заправка") }

            Button(
                onClick = { vm.setTab(StatsTab.CTO) },
                colors = ButtonDefaults.buttonColors(containerColor = if (state.activeTab == StatsTab.CTO) MaterialTheme.colorScheme.primary else Color.LightGray)
            ) { Text("СТО") }

            Button(
                onClick = { vm.setTab(StatsTab.DISTANCE) },
                colors = ButtonDefaults.buttonColors(containerColor = if (state.activeTab == StatsTab.DISTANCE) MaterialTheme.colorScheme.primary else Color.LightGray)
            ) { Text("Поїздка") }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                var showExistsQuery by remember { mutableStateOf(false) }

                val carsWithFuelings by vm.carsWithFuelingsFlow.collectAsState(initial = emptyList())
//                val carsWithCTO by vm.expensiveStationsFlow.collectAsState(initial = emptyList())

                val displayCars = when (state.activeTab) {
                    StatsTab.FUEL -> {
                        if (showExistsQuery) {
                            val validIds = carsWithFuelings.map { it.globalVehicleId }
                            state.carsList.filter { it.carId in validIds }
                        } else state.carsList
                    }

                    else -> state.carsList
                }

                if (displayCars.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.05f))
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(displayCars) { car ->
                            val isSelected = state.selectedCarId == car.carId

                            FilterChip(
                                selected = isSelected,
                                onClick = { vm.setSelectedCar(car.carId) },
                                label = {
                                    Text(
                                        "${car.carName} (${car.plateNumber})",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Немає авто для відображення",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (state.activeTab == StatsTab.FUEL) {
                        Button(
                            onClick = { showExistsQuery = !showExistsQuery },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (showExistsQuery) "Показати всі автомобілі" else "Фільтр: Тільки авто із заправками")

                        }
                    }
//                    } else if (state.activeTab == StatsTab.CTO) {
//                        Button(
//                            onClick = { showExpensiveStations = !showExpensiveStations },
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Text(if (showExpensiveStations) "Приховати аналітику СТО" else "ТОП дорогих СТО (>10к ₴)")
//                        }
//
//                        if (showExpensiveStations && carsWithCTO.isNotEmpty()) {
//                            Column(modifier = Modifier.padding(top = 8.dp)) {
//                                carsWithCTO.forEach { station ->
//                                    Row(
//                                        modifier = Modifier.fillMaxWidth(),
//                                        horizontalArrangement = Arrangement.SpaceBetween
//                                    ) {
//                                        Text("СТО: ${station.serviceStation}")
//                                        Text(
//                                            "${station.total} ₴",
//                                            color = Color.Red,
//                                            fontWeight = Bold
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Всього витрачено",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (state.globalTotalSpent != null && state.globalTotalSpent > 0)
                                String.format("%.0f ₴", state.globalTotalSpent)
                            else "0 ₴",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp)
                            .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Вартість 1 км",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (state.globalCostPerKm != null && state.globalCostPerKm > 0)
                                String.format("%.2f ₴/км", state.globalCostPerKm)
                            else "0.00 ₴",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (state.activeTab) {

                StatsTab.FUEL -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { vm.setFuelSubTab(FuelSubTab.ALL) },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = if (state.activeFuelSubTab == FuelSubTab.ALL) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                        ) { Text("Всі") }

                        OutlinedButton(
                            onClick = { vm.setFuelSubTab(FuelSubTab.BY_TYPE) },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = if (state.activeFuelSubTab == FuelSubTab.BY_TYPE) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                        ) { Text("По типу") }

                        OutlinedButton(
                            onClick = { vm.setFuelSubTab(FuelSubTab.MOST_EXPENSIVE) },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = if (state.activeFuelSubTab == FuelSubTab.MOST_EXPENSIVE) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                        ) { Text("Найдорожча") }
                    }

                    when (state.activeFuelSubTab) {
                        FuelSubTab.ALL -> {
                            Column(modifier = Modifier.fillMaxSize()) {
                                DateRangeFilter(
                                    startDate = state.filterStartDate,
                                    endDate = state.filterEndDate,
                                    onStartSelect = { vm.setStartDate(it) },
                                    onEndSelect = { vm.setEndDate(it) }
                                )
                                FuelStatsList(state.fuelStatsAll)
                            }
                        }

                        FuelSubTab.BY_TYPE -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp).systemBarsPadding()
                            ) {
                                items(state.fuelStatsByType) { stat ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Пальне: ${stat.fuelType}", fontWeight = Bold)
                                            Text(
                                                "Витрачено: ${stat.totalCost} ₴",
                                                color = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        FuelSubTab.MOST_EXPENSIVE -> FuelStatsList(state.fuelMostExpensive)
                    }
                }

                StatsTab.CTO -> {
                    Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(androidx.compose.foundation.rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { vm.setCtoSubTab(CtoSubTab.SEARCH) },
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = if (state.activeCtoSubTab == CtoSubTab.SEARCH) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            ) { Text("Пошук") }

                            OutlinedButton(
                                onClick = { vm.setCtoSubTab(CtoSubTab.BY_CAR) },
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = if (state.activeCtoSubTab == CtoSubTab.BY_CAR) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            ) { Text("Витрати авто") }

                            OutlinedButton(
                                onClick = { vm.setCtoSubTab(CtoSubTab.TOP_STATIONS) },
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = if (state.activeCtoSubTab == CtoSubTab.TOP_STATIONS) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            ) { Text("Топ СТО") }

                            OutlinedButton(
                                onClick = { vm.setCtoSubTab(CtoSubTab.COMPLEX) },
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = if (state.activeCtoSubTab == CtoSubTab.COMPLEX) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            ) { Text("Звіт") }
                        }

                        when (state.activeCtoSubTab) {
                            CtoSubTab.SEARCH -> {
                                OutlinedTextField(
                                    value = state.ctoSearchQuery,
                                    onValueChange = { vm.updateCtoSearch(it) },
                                    label = { Text("Пошук по СТО (напр. ГРМ)") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                )
                                CtoStatsList(state.ctoStats)
                            }

                            CtoSubTab.BY_CAR -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    items(state.ctoCostsByCar) { stat ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                    "${stat.carName} (${stat.plateNumber})",
                                                    fontWeight = Bold
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    "Всього на ремонти: ${stat.totalCost} ₴",
                                                    color = Color.Red
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            CtoSubTab.TOP_STATIONS -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    items(state.ctoPopularStations) { stat ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .padding(16.dp)
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text("СТО: ${stat.stationName}", fontWeight = Bold)
                                                Text(
                                                    "Візитів: ${stat.visitCount}",
                                                    color = MaterialTheme.colorScheme.primary,
                                                    fontWeight = Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            CtoSubTab.COMPLEX -> {
                                val statsList by vm.complexServiceStatFlow.collectAsState(initial = emptyList())

                                var inputDate by remember { mutableStateOf("2026-01-01") }
                                var inputCost by remember { mutableStateOf("5000") }

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                                alpha = 0.5f
                                            )
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                "Налаштування комплексного звіту",
                                                fontWeight = Bold,
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))

                                            OutlinedTextField(
                                                value = inputDate,
                                                onValueChange = { inputDate = it },
                                                label = { Text("З якої дати? (РРРР-ММ-ДД)") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))

                                            OutlinedTextField(
                                                value = inputCost,
                                                onValueChange = { inputCost = it },
                                                label = { Text("Мінімальна сума СТО (HAVING)") },
                                                modifier = Modifier.fillMaxWidth(),
                                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))

                                            Button(
                                                onClick = {
                                                    val costInt = inputCost.toDoubleOrNull() ?: 0.0
                                                    vm.updateComplexStatFilters(inputDate, costInt)
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text("Застосувати фільтр")
                                            }
                                        }
                                    }

                                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                                        if (statsList.isEmpty()) {
                                            item {
                                                Text(
                                                    "За цими параметрами витрат не знайдено",
                                                    modifier = Modifier.padding(16.dp),
                                                    color = MaterialTheme.colorScheme.outline
                                                )
                                            }
                                        }

                                        items(statsList) { stat ->
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                elevation = CardDefaults.cardElevation(
                                                    defaultElevation = 2.dp
                                                ),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(
                                                        alpha = 0.3f
                                                    )
                                                ),
                                                border = androidx.compose.foundation.BorderStroke(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.tertiary
                                                )
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .padding(16.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            "СТО: ${stat.serviceStation}",
                                                            fontWeight = Bold
                                                        )
                                                        Text(
                                                            "Відфільтровано: > ${inputCost}₴ з ${inputDate}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                    Text(
                                                        "${stat.totalCost} ₴",
                                                        color = MaterialTheme.colorScheme.error,
                                                        fontWeight = Bold,
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                StatsTab.DISTANCE -> {
                    Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = { vm.setDistanceSubTab(DistanceSubTab.ALL_TRIPS) },
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = if (state.activeDistanceSubTab == DistanceSubTab.ALL_TRIPS) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            ) { Text("Всі") }

                            OutlinedButton(
                                onClick = { vm.setDistanceSubTab(DistanceSubTab.ROUTES) },
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = if (state.activeDistanceSubTab == DistanceSubTab.ROUTES) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            ) { Text("Топ маршрути") }

                        }

                        when (state.activeDistanceSubTab) {
                            DistanceSubTab.ALL_TRIPS -> {
                                DistanceStatsList(state.distanceStats)
                            }

                            DistanceSubTab.ROUTES -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    items(state.popularRoutes) { route ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .padding(16.dp)
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "📍 ${route.startPoint} ➔ ${route.endPoint}",
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        "Поїздок: ${route.tripCount}",
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        "Загалом: ${route.totalRouteDistance} км",
                                                        color = MaterialTheme.colorScheme.outline
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CtoStatsList(ctoStats: List<CTOTuple>) {
    LazyColumn(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        items(ctoStats) { cto ->
            CTOStatsCard(
                cto.date,
                cto.carName,
                cto.plateNumber,
                cto.cost,
                cto.whatWork,
                cto.station,
            )
        }
    }
}

@Composable
fun FuelStatsList(fuelStats: List<TotalCostForFuelTuple>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp).systemBarsPadding()
    ) {
        items(fuelStats) { stat ->
            FuelStatCard(
                stat.carName,
                stat.plateNumber,
                stat.fuelType,
                stat.date,
                stat.totalCost
            )
        }
    }
}

@Composable
fun DistanceStatsList(distanceStats: List<MostTraveledVehicleTuple>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp).systemBarsPadding()
    ) {
        items(distanceStats) { distance ->
            DistanceStatsCard(
                distance.carName,
                distance.plateNumber,
                distance.dateWhenAdd,
                distance.totalTraveledKM
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeFilter(
    startDate: Long?,
    endDate: Long?,
    onStartSelect: (Long?) -> Unit,
    onEndSelect: (Long?) -> Unit,
) {
    var showStart by remember { mutableStateOf(false) }
    var showEnd by remember { mutableStateOf(false) }

    val formatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val startText = startDate?.let { formatter.format(Date(it)) } ?: "З дати"
    val endText = endDate?.let { formatter.format(Date(it)) } ?: "По дату"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(onClick = { showStart = true }, modifier = Modifier.weight(1f)) {
            Text("📅 $startText")
        }
        OutlinedButton(onClick = { showEnd = true }, modifier = Modifier.weight(1f)) {
            Text("📅 $endText")
        }
    }

    if (showStart) {
        val state = rememberDatePickerState(initialSelectedDateMillis = startDate)
        DatePickerDialog(
            onDismissRequest = { showStart = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onStartSelect(state.selectedDateMillis)
                        showStart = false
                    }
                ) { Text("ОК") }
            },
            dismissButton = {
                TextButton(onClick = { showStart = false }) { Text("Скасувати") }
            }
        ) { DatePicker(state = state) }
    }

    if (showEnd) {
        val state = rememberDatePickerState(initialSelectedDateMillis = endDate)
        DatePickerDialog(
            onDismissRequest = { showEnd = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEndSelect(state.selectedDateMillis)
                        showEnd = false
                    }
                ) { Text("ОК") }
            },
            dismissButton = {
                TextButton(onClick = { showEnd = false }) { Text("Скасувати") }
            }
        ) { DatePicker(state = state) }
    }
}