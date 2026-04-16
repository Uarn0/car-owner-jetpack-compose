package com.example.mmmsssmmm.statsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.EventRepository
import com.example.mmmsssmmm.data.tuples.CtoSubTab
import com.example.mmmsssmmm.data.tuples.DistanceSubTab
import com.example.mmmsssmmm.data.tuples.FuelSubTab
import com.example.mmmsssmmm.data.tuples.StatsTab
import com.example.mmmsssmmm.data.tuples.StatsUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(private val repo: EventRepository) : ViewModel() {

    private val _activeTab = MutableStateFlow(StatsTab.FUEL)
    private val _activeFuelSubTab = MutableStateFlow(FuelSubTab.ALL)
    private val _activeCtoSubTab = MutableStateFlow(CtoSubTab.SEARCH)
    private val _activeDistanceSubTab = MutableStateFlow(DistanceSubTab.ALL_TRIPS)
    private val _ctoSearchQuery = MutableStateFlow("")


    private val uiInputsFlow = combine(
        _activeTab,
        _activeFuelSubTab,
        _activeCtoSubTab,
        _activeDistanceSubTab,
        _ctoSearchQuery
    ) { tab, fuelTab, ctoTab, distTab, query ->
        listOf(tab, fuelTab, ctoTab, distTab, query)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ctoSearchFlow = _ctoSearchQuery.flatMapLatest { query ->
        repo.observeAllCTOStats(query)
    }

    private val fuelStatsFlow = combine(
        repo.getFuelHistoryBetweenDates(),
        repo.getTotalCostByFuelType(),
        repo.getMostExpensiveFueling()
    ) { all, byType, max ->
        Triple(all, byType, max)
    }

    private val ctoAnalyticsFlow = combine(
        repo.getCtoCostsByCar(),
        repo.getPopularStations()
    ) { costs, stations ->
        Pair(costs, stations)
    }

    private val distanceAnalyticsFlow = combine(
        repo.observeDistanceStats(),
        repo.getPopularRoutes()
    ) { all, routes ->
        Pair(all, routes)
    }

    val uiState: StateFlow<StatsUiState> = combine(
        uiInputsFlow,
        fuelStatsFlow,
        ctoSearchFlow,
        ctoAnalyticsFlow,
        distanceAnalyticsFlow
    ) { inputs, fuel, ctoSearch, ctoAnalytics, distance ->
        StatsUiState(
            activeTab = inputs[0] as StatsTab,
            activeFuelSubTab = inputs[1] as FuelSubTab,
            activeCtoSubTab = inputs[2] as CtoSubTab,
            activeDistanceSubTab = inputs[3] as DistanceSubTab,
            ctoSearchQuery = inputs[4] as String,

            fuelStatsAll = fuel.first,
            fuelStatsByType = fuel.second,
            fuelMostExpensive = fuel.third,

            ctoStats = ctoSearch,
            ctoCostsByCar = ctoAnalytics.first,
            ctoPopularStations = ctoAnalytics.second,

            distanceStats = distance.first,
            popularRoutes = distance.second
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsUiState())


    fun setTab(tab: StatsTab) { _activeTab.value = tab }
    fun setFuelSubTab(subTab: FuelSubTab) { _activeFuelSubTab.value = subTab }
    fun setCtoSubTab(subTab: CtoSubTab) { _activeCtoSubTab.value = subTab }
    fun setDistanceSubTab(subTab: DistanceSubTab) { _activeDistanceSubTab.value = subTab }
    fun updateCtoSearch(query: String) { _ctoSearchQuery.value = query }
}