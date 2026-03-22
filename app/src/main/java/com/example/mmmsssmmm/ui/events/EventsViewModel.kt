package com.example.mmmsssmmm.ui.events

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventsViewModel(
    private val repo: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val vehicleId: Long = checkNotNull(savedStateHandle["vehicleId"])
    private val globalEventId: Long = checkNotNull(savedStateHandle["globalEventId"])
    val events = repo.observeEvents(vehicleId)
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    fun add(name: String, type: Int, time: String) = viewModelScope.launch { repo.insert(vehicleId, name, type, time) }
    fun add(startPoint: String, endPoint: String, distanceKM: Int, isBusiness: Boolean) = viewModelScope.launch { repo.insert(
        globalEventId,
        startPoint = startPoint,
        endPoint = endPoint,
        distanceKM = distanceKM,
        isBusiness = isBusiness
    ) }
    fun allDelete(id: Long) = viewModelScope.launch { repo.deleteInId(id) }
    fun tripDelete(id: Long) = viewModelScope.launch { repo.tripDeleteInId(id) }
    fun serviceDelete(id: Long) = viewModelScope.launch { repo.serviceDeleteInId(id) }
    fun fuelDelete(id: Long) = viewModelScope.launch { repo.fuelDeleteInId(id) }
}