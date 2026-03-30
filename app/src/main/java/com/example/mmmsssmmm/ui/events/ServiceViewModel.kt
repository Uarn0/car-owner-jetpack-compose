package com.example.mmmsssmmm.ui.events

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiceViewModel(private val repo: EventRepository, savedStateHandle: SavedStateHandle
): ViewModel() {
    private val globalEventId: Long = checkNotNull(savedStateHandle["globalEventId"])

    fun add(vehicleId: Long, name: String, date: String, odometer: Int, totalCost: Double, workTitle: String, serviceStation: String) = viewModelScope.launch { repo.insertServiceEvent(
        vehicleId = vehicleId,
        name = name,
        date = date,
        odometer = odometer,
        totalCost = totalCost,
        workTitle = workTitle,
        serviceStation = serviceStation
    ) }

    fun delete(id: Long) = viewModelScope.launch { repo.deleteInId(id) }
}

