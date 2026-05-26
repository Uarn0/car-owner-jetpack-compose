package com.example.mmmsssmmm.data.tuples

import com.example.mmmsssmmm.data.entity.VehiclesEntity

enum class StatsTab { FUEL, CTO, DISTANCE }
enum class FuelSubTab { ALL, BY_TYPE, MOST_EXPENSIVE }
enum class CtoSubTab { SEARCH, BY_CAR, TOP_STATIONS, COMPLEX }
enum class DistanceSubTab { ALL_TRIPS, ROUTES }

data class StatsUiState(
    val activeTab: StatsTab = StatsTab.FUEL,
    val activeFuelSubTab: FuelSubTab = FuelSubTab.ALL,
    val activeCtoSubTab: CtoSubTab = CtoSubTab.SEARCH,
    val activeDistanceSubTab: DistanceSubTab = DistanceSubTab.ALL_TRIPS,

    val ctoSearchQuery: String = "",

    val globalTotalSpent: Double? = null,
    val globalCostPerKm: Double? = null,

    val filterStartDate: Long? = null,
    val filterEndDate: Long? = null,
    val expensiveStations: List<CtoStationCountTotalTuple> = emptyList(),
    val fuelStatsAll: List<TotalCostForFuelTuple> = emptyList(),
    val fuelStatsByType: List<FuelTypeCostTuple> = emptyList(),
    val fuelMostExpensive: List<TotalCostForFuelTuple> = emptyList(),

    val ctoStats: List<CTOTuple> = emptyList(),
    val ctoCostsByCar: List<CtoCostByCarTuple> = emptyList(),
    val ctoPopularStations: List<CtoStationCountTuple> = emptyList(),

    val distanceStats: List<MostTraveledVehicleTuple> = emptyList(),
    val popularRoutes: List<PopularRouteTuple> = emptyList(),

    val carsList: List<CarDropdownTuple> = emptyList(),
    val selectedCarId: Long? = null,

    val carsWithFuelings: List<VehiclesEntity> = emptyList()
)