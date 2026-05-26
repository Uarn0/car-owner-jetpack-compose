package com.example.mmmsssmmm.ui.events

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.EventRepository
import com.example.mmmsssmmm.domain.item.VehicleHistoryItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class SubEventViewModel(
    private val repo: EventRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val eventId: Long = checkNotNull(savedStateHandle["eventId"])

    private var _capacityInCar: Double? = 0.0

    var capacity: Double?
        get() {
            return _capacityInCar
        }
        set(value) {
            _capacityInCar = value
        }

    init {
        viewModelScope.launch {
            val cId = repo.getCarId(eventId)
            capacity = repo.getCarCapacity(cId)
        }
    }

    val fuelTypesList = repo.getFuelTypes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var selectedFuelTypeId by mutableStateOf<Int?>(null)

    fun updateFuelingData(
        newVol: String? = null,
        newPrice: String? = null,
        newFuelTypeId: Int? = null,
    ) {
        if (newVol != null) volume = newVol
        if (newPrice != null) pricePerLiter = newPrice
        if (newFuelTypeId != null) selectedFuelTypeId = newFuelTypeId
    }

    var startPoint by mutableStateOf("")
    var endPoint by mutableStateOf("")
    var distance by mutableStateOf("")

    var volume by mutableStateOf("")
    var pricePerLiter by mutableStateOf("")

    var workTitle by mutableStateOf("")
    var stationName by mutableStateOf("")
    var serviceC by mutableStateOf("")
    var totalCost by mutableStateOf("")

    val currentSubEvent: StateFlow<List<VehicleHistoryItem>> = repo.observeSingleSubEvent(eventId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateTripDistance(newDist: String) {
        distance = newDist.replace(',', '.')
        val d = distance.toDoubleOrNull() ?: 0.0
        val avgCons = 8.0
        val fuelPrice = 70
        if (d > 0) {
            val calculated = (d / 100) * avgCons * fuelPrice
            totalCost = String.format("%.2f", calculated).replace(',', '.')
        }
    }


    fun addTrip() = viewModelScope.launch {
        val addedDist = distance.toIntOrNull() ?: 0
        val addedCost = totalCost.toDoubleOrNull() ?: 0.0

        repo.insertTripDetails(eventId, startPoint, endPoint, addedDist)

        repo.addToEventStats(eventId, addedCost, addedDist)
    }

    fun addFueling(fuelId: Int?) = viewModelScope.launch {

        val addedCost = totalCost.toDoubleOrNull() ?: 0.0
        val addedVol = volume.toDoubleOrNull() ?: 0.0
        val priceDb = pricePerLiter.toDoubleOrNull() ?: 0.0

        val addedOdo = 0
        val isFull = if (capacity!! <= addedVol) true else false

        repo.insertFuelingDetails(eventId, fuelId, addedVol, priceDb, isFull)
        repo.addToEventStats(eventId, addedCost, addedOdo)
    }

    fun addService() = viewModelScope.launch {
        val finalCost = serviceC.toDoubleOrNull() ?: 0.0

        repo.insertServiceDetails(eventId, workTitle, stationName, finalCost)

        repo.addToEventStats(eventId, finalCost, 0)
    }

    fun tripDelete() = viewModelScope.launch { repo.tripDeleteInId(eventId) }
    fun serviceDelete() = viewModelScope.launch { repo.serviceDeleteInId(eventId) }
    fun fuelDelete() = viewModelScope.launch { repo.fuelDeleteInId(eventId) }
}