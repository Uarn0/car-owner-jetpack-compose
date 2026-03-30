package com.example.mmmsssmmm.ui.events

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripViewModel(
    private val repo: EventRepository, savedStateHandle: SavedStateHandle
): ViewModel() {
    private val globalEventId: Long = checkNotNull(savedStateHandle["globalEventId"])

//    val trips = repo.observeEvents(globalEventId)
//        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    fun add(vehicleId: Long, name: String, time: String, odometer: Int, totalCost: Double, startPoint: String, endPoint: String, distanceKM: Int, isBusiness: Boolean) = viewModelScope.launch { repo.insertTripEvent(
        vehicleId = vehicleId,
        startPoint = startPoint,
        endPoint = endPoint,
        distanceKM = distanceKM,
        isBusiness = isBusiness,
        name = name,
        time = time,
        odometer = odometer,
        totalCost = totalCost
    ) }

    fun delete(id: Long) = viewModelScope.launch { repo.deleteInId(id) }
}

