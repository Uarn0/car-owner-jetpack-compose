package com.example.mmmsssmmm.ui.events

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripViewModel(
    private val repo: TripRepository, savedStateHandle: SavedStateHandle
): ViewModel() {
    private val globalEventId: Long = checkNotNull(savedStateHandle["globalEventId"])

    val trips = repo.observeTrip(globalEventId)
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    fun add(startPoint: String, endPoint: String, distanceKM: Int, isBusiness: Boolean) = viewModelScope.launch { repo.insert(
        startPoint = startPoint,
        endPoint = endPoint,
        distanceKM = distanceKM,
        isBusiness = isBusiness
    ) }

    fun delete(id: Long) = viewModelScope.launch { repo.deleteInId(id) }
}

