package com.example.mmmsssmmm.data.tuples

enum class StatsTab { FUEL, CTO, DISTANCE }

data class StatsUiState(
    val activeTab: StatsTab = StatsTab.CTO,
    val ctoSearchQuery: String = "",

    val fuelStats: List<TotalCostForFuelTuple> = emptyList(),
    val ctoStats: List<CTOTuple> = emptyList(),
    val distanceStats: List<MostTraveledVehicleTuple> = emptyList()
)