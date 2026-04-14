package com.example.mmmsssmmm.statsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.EventRepository
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
    private val _ctoSearchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ctoStatsFlow = _ctoSearchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            repo.observeAllCTOStats(query)
        } else {
            repo.observeAllCTOStats(query)
        }
    }

    val uiState: StateFlow<StatsUiState> = combine(
        _activeTab,
        _ctoSearchQuery,
        repo.observeFuelCostHistory(),
        ctoStatsFlow,
        repo.observeDistanceStats()
    ) { tab, query, fuel, cto, distance ->
        StatsUiState(
            activeTab = tab,
            ctoSearchQuery = query,
            fuelStats = fuel,
            ctoStats = cto,
            distanceStats = distance
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsUiState())


    fun setTab(tab: StatsTab) {
        _activeTab.value = tab
    }

    fun updateCtoSearch(query: String) {
        _ctoSearchQuery.value = query
    }
}