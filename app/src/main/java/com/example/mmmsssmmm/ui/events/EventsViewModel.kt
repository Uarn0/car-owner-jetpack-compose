package com.example.mmmsssmmm.ui.events

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventsViewModel(
    private val repo: EventRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val vehicleId: Long = checkNotNull(savedStateHandle["vehicleId"])

    var odometer by mutableStateOf("")
    var totalCost by mutableStateOf("")

    var startPoint by mutableStateOf("")
    var endPoint by mutableStateOf("")
    var distance by mutableStateOf("")

    var volume by mutableStateOf("")
    var pricePerLiter by mutableStateOf("")

    var workTitle by mutableStateOf("")
    var stationName by mutableStateOf("")



    val baseEvents = repo.observeBaseEvents(vehicleId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun createBaseEvent(
        name: String, date: String, odometer: Int, totalCost: Double,
        onSuccess: (Long) -> Unit,
    ) = viewModelScope.launch {
        val newEventId = repo.insertBasicEvent(vehicleId, name, date, odometer, totalCost)
        onSuccess(newEventId)
    }

    fun addTripToEvent(
        eventId: Long, startPoint: String, endPoint: String, distanceKM: Int, isBusiness: Boolean,
    ) = viewModelScope.launch {
        repo.insertTripDetails(eventId, startPoint, endPoint, distanceKM, isBusiness)
    }

    fun addFuelingToEvent(
        eventId: Long,
        fuelTypeId: Int,
        volumeLiters: Double,
        pricePerLiter: Double,
        isFullTank: Boolean,
    ) = viewModelScope.launch {
        repo.insertFuelingDetails(eventId, fuelTypeId, volumeLiters, pricePerLiter, isFullTank)
    }

    fun addServiceToEvent(
        eventId: Long, workTitle: String, serviceStation: String,
    ) = viewModelScope.launch {
        repo.insertServiceDetails(eventId, workTitle, serviceStation)
    }


    fun allDelete(id: Long) = viewModelScope.launch { repo.deleteInId(id) }

    fun tripDelete(id: Long) = viewModelScope.launch { repo.tripDeleteInId(id) }
    fun serviceDelete(id: Long) = viewModelScope.launch { repo.serviceDeleteInId(id) }
    fun fuelDelete(id: Long) = viewModelScope.launch { repo.fuelDeleteInId(id) }
}