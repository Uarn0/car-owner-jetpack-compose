package com.example.mmmsssmmm.statsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.entity.VehiclesEntity
import com.example.mmmsssmmm.data.repos.EventRepository
import com.example.mmmsssmmm.data.tuples.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StatsViewModel(private val repo: EventRepository) : ViewModel() {

    private val _activeTab = MutableStateFlow(StatsTab.FUEL)
    private val _activeFuelSubTab = MutableStateFlow(FuelSubTab.ALL)
    private val _activeCtoSubTab = MutableStateFlow(CtoSubTab.SEARCH)
    private val _activeDistanceSubTab = MutableStateFlow(DistanceSubTab.ALL_TRIPS)
    private val _ctoSearchQuery = MutableStateFlow("")

    private val _filterStartDate = MutableStateFlow<Long?>(null)
    private val _filterEndDate = MutableStateFlow<Long?>(null)

    private data class UiInputs(val tab: StatsTab, val fuel: FuelSubTab, val cto: CtoSubTab, val dist: DistanceSubTab, val query: String)
    private val uiInputsFlow = combine(_activeTab, _activeFuelSubTab, _activeCtoSubTab, _activeDistanceSubTab, _ctoSearchQuery) { t, f, c, d, q ->
        UiInputs(t, f, c, d, q)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ctoSearchFlow = _ctoSearchQuery.flatMapLatest { query ->
        repo.observeAllCTOStats(query)
    }

    private data class FuelData(val all: List<TotalCostForFuelTuple>, val byType: List<FuelTypeCostTuple>, val max: List<TotalCostForFuelTuple>)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val fuelStatsFlow = combine(
        _filterStartDate,
        _filterEndDate
    ) { start, end -> Pair(start, end) }.flatMapLatest { dates ->

        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val startStr = dates.first?.let { sdf.format(java.util.Date(it)) } ?: "2000-01-01"
        val endStr = dates.second?.let { sdf.format(java.util.Date(it)) } ?: "2099-12-31"

        combine(
            repo.getFuelHistoryBetweenDates(startStr, endStr),
            repo.getTotalCostByFuelType(),
            repo.getMostExpensiveFueling()
        ) { all, byType, max ->
            FuelData(all, byType, max)
        }
    }
    val carsWithFuelingsFlow = repo.getCarsWithFueling()

    val expensiveStationsFlow = repo.getExpensiveStations()

    private val _complexStatDate = MutableStateFlow("2026-01-01")
    private val _complexStatCost = MutableStateFlow(5000.0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val complexServiceStatFlow = combine(_complexStatDate, _complexStatCost) { date, cost ->
        Pair(date, cost)
    }.flatMapLatest { (dateString, costInt) ->
        repo.getComplexServiceStat(dateString, costInt)
    }

    fun updateComplexStatFilters(newDate: String, newCost: Double) {
        _complexStatDate.value = newDate
        _complexStatCost.value = newCost
    }

    private data class CtoData(val costs: List<CtoCostByCarTuple>, val stations: List<CtoStationCountTuple>)
    private val ctoAnalyticsFlow = combine(repo.getCtoCostsByCar(), repo.getPopularStations()) { costs, stations ->
        CtoData(costs, stations)
    }

    private data class DistanceData(val all: List<MostTraveledVehicleTuple>, val routes: List<PopularRouteTuple>)
    private val distanceAnalyticsFlow = combine(repo.observeDistanceStats(), repo.getPopularRoutes()) { all, routes ->
        DistanceData(all, routes)
    }


    private val _selectedCarId = MutableStateFlow<Long?>(null)

    private data class DashboardData(val totalSpent: Double?, val cost: Double?, val cars: List<CarDropdownTuple>, val selectedId: Long?)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dashboardMetricsFlow = combine(
        repo.getCarsForDashboard().onEach { cars ->
            if (_selectedCarId.value == null && cars.isNotEmpty()) {
                _selectedCarId.value = cars.first().carId
            }
        },
        _selectedCarId.flatMapLatest { carId ->
            if (carId == null) {
                flowOf(Pair(null, null))
            } else {
                combine(
                    repo.getTotalSpentByCar(carId),
                    repo.getCostPerKilometer(carId)
                ) { totalSpent, cost ->
                    Pair(totalSpent, cost.costPerKm)
                }
            }
        },
        _selectedCarId
    ) { cars, metrics, selectedId ->
        DashboardData(metrics.first, metrics.second, cars, selectedId)
    }

    val uiState: StateFlow<StatsUiState> = combine(
        uiInputsFlow,
        fuelStatsFlow,
        ctoSearchFlow,
        ctoAnalyticsFlow,
        combine(distanceAnalyticsFlow, dashboardMetricsFlow, expensiveStationsFlow) { dist, dash, expStations ->
            Triple(dist, dash, expStations)
        }
    ) { inputs, fuel, ctoSearch, ctoAnalytics, extraData ->

        val distance = extraData.first
        val dashboard = extraData.second
        val expStations = extraData.third
        StatsUiState(
            activeTab = inputs.tab,
            activeFuelSubTab = inputs.fuel,
            activeCtoSubTab = inputs.cto,
            activeDistanceSubTab = inputs.dist,
            ctoSearchQuery = inputs.query,

            fuelStatsAll = fuel.all,
            fuelStatsByType = fuel.byType,
            fuelMostExpensive = fuel.max,

            ctoStats = ctoSearch,
            ctoCostsByCar = ctoAnalytics.costs,
            ctoPopularStations = ctoAnalytics.stations,

            expensiveStations = expStations,

            distanceStats = distance.all,
            popularRoutes = distance.routes,

            globalTotalSpent = dashboard.totalSpent,
            globalCostPerKm = dashboard.cost,

            carsList = dashboard.cars,
            selectedCarId = dashboard.selectedId
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsUiState())

    fun setTab(tab: StatsTab) { _activeTab.value = tab }
    fun setFuelSubTab(subTab: FuelSubTab) { _activeFuelSubTab.value = subTab }
    fun setCtoSubTab(subTab: CtoSubTab) { _activeCtoSubTab.value = subTab }
    fun setDistanceSubTab(subTab: DistanceSubTab) { _activeDistanceSubTab.value = subTab }
    fun updateCtoSearch(query: String) { _ctoSearchQuery.value = query }
    fun setStartDate(millis: Long?) { _filterStartDate.value = millis }
    fun setEndDate(millis: Long?) { _filterEndDate.value = millis }
    fun setSelectedCar(carId: Long) { _selectedCarId.value = carId }


}