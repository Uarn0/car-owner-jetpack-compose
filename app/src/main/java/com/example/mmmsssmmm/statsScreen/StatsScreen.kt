package com.example.mmmsssmmm.statsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mmmsssmmm.data.tuples.CTOTuple
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
        Row {
            Button(onClick = { vm.setTab(StatsTab.FUEL) }) { Text("Fuel") }
            Button(onClick = { vm.setTab(StatsTab.CTO) }) { Text("CTO") }
        }

        when (state.activeTab) {

            StatsTab.FUEL -> {
                FuelStatsList(state.fuelStats)
            }

            StatsTab.CTO -> {
                OutlinedTextField(
                    value = state.ctoSearchQuery,
                    onValueChange = { vm.updateCtoSearch(it) },
                    label = { Text("Пошук по СТО (напр. ГРМ)") }
                )

                CtoStatsList(state.ctoStats)
            }

            StatsTab.DISTANCE -> {
                DistanceStatsList(state.distanceStats)
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