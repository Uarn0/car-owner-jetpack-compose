package com.example.mmmsssmmm.statsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun StatsScreen(vm: StatsViewModel) {
    val state = vm.uiState.collectAsStateWithLifecycle().value

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { vm.setTab(StatsTab.FUEL) }) { Text("Fuel") }
            Button(onClick = { vm.setTab(StatsTab.CTO) }) { Text("CTO") }
            Button(onClick = { vm.setTab(StatsTab.DISTANCE) }) { Text("Distance") }
        }

        when (state.activeTab) {

            StatsTab.FUEL -> {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { vm.setFuelSubTab(FuelSubTab.ALL) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (state.activeFuelSubTab == FuelSubTab.ALL) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                        )
                    ) { Text("Всі") }

                    OutlinedButton(
                        onClick = { vm.setFuelSubTab(FuelSubTab.BY_TYPE) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (state.activeFuelSubTab == FuelSubTab.BY_TYPE) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                        )
                    ) { Text("По типу") }

                    OutlinedButton(
                        onClick = { vm.setFuelSubTab(FuelSubTab.MOST_EXPENSIVE) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (state.activeFuelSubTab == FuelSubTab.MOST_EXPENSIVE) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                        )
                    ) { Text("Найдорожча") }
                }

                when (state.activeFuelSubTab) {
                    FuelSubTab.ALL -> FuelStatsList(state.fuelStatsAll)

                    FuelSubTab.BY_TYPE -> {
                        LazyColumn(modifier = Modifier.padding(16.dp)) {
                            items(state.fuelStatsByType) { stat ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Пальне: ${stat.fuelType}", fontWeight = Bold)
                                        Text("Витрачено: ${stat.totalCost} ₴", color = Color.Red)
                                    }
                                }
                            }
                        }
                    }

                    FuelSubTab.MOST_EXPENSIVE -> FuelStatsList(state.fuelMostExpensive)
                }
            }

            StatsTab.CTO -> {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { vm.setCtoSubTab(CtoSubTab.SEARCH) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (state.activeCtoSubTab == CtoSubTab.SEARCH) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text("Пошук") }

                        OutlinedButton(
                            onClick = { vm.setCtoSubTab(CtoSubTab.BY_CAR) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (state.activeCtoSubTab == CtoSubTab.BY_CAR) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text("Витрати авто") }

                        OutlinedButton(
                            onClick = { vm.setCtoSubTab(CtoSubTab.TOP_STATIONS) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (state.activeCtoSubTab == CtoSubTab.TOP_STATIONS) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text("Топ СТО") }
                    }

                    when (state.activeCtoSubTab) {
                        CtoSubTab.SEARCH -> {
                            OutlinedTextField(
                                value = state.ctoSearchQuery,
                                onValueChange = { vm.updateCtoSearch(it) },
                                label = { Text("Пошук по СТО (напр. ГРМ)") },
                                modifier = Modifier.fillMaxWidth().padding(
                                    horizontal = 16.dp
                                )
                            )
                            CtoStatsList(state.ctoStats)
                        }

                        CtoSubTab.BY_CAR -> {
                            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                                items(state.ctoCostsByCar) { stat ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text("${stat.carName} (${stat.plateNumber})", fontWeight = Bold)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("Всього на ремонти: ${stat.totalCost} ₴", color = Color.Red)
                                        }
                                    }
                                }
                            }
                        }

                        CtoSubTab.TOP_STATIONS -> {
                            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                                items(state.ctoPopularStations) { stat ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("СТО: ${stat.stationName}", fontWeight = Bold)
                                            Text("Візитів: ${stat.visitCount}", color = MaterialTheme.colorScheme.primary, fontWeight = Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            StatsTab.DISTANCE -> {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { vm.setDistanceSubTab(DistanceSubTab.ALL_TRIPS) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (state.activeDistanceSubTab == DistanceSubTab.ALL_TRIPS) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text("Всі") }

                        OutlinedButton(
                            onClick = { vm.setDistanceSubTab(DistanceSubTab.ROUTES) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (state.activeDistanceSubTab == DistanceSubTab.ROUTES) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text("Топ маршрути") }

                    }

                    when (state.activeDistanceSubTab) {
                        DistanceSubTab.ALL_TRIPS -> {
                            DistanceStatsList(state.distanceStats)
                        }

                        DistanceSubTab.ROUTES -> {
                            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                                items(state.popularRoutes) { route ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
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
                                                Text("Поїздок: ${route.tripCount}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                                Text("Загалом: ${route.totalRouteDistance} км", color = MaterialTheme.colorScheme.outline)
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
    LazyColumn {
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
    LazyColumn(modifier = Modifier.padding()) {
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
    LazyColumn(modifier = Modifier.padding()) {
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