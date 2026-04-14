package com.example.mmmsssmmm.ui.events

import androidx.compose.runtime.getValue
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

class SubEventViewModel(
    private val repo: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: Long = checkNotNull(savedStateHandle["eventId"])

    var startPoint by mutableStateOf("")
    var endPoint by mutableStateOf("")
    var distance by mutableStateOf("")

    var volume by mutableStateOf("")
    var pricePerLiter by mutableStateOf("")

    var workTitle by mutableStateOf("")
    var stationName by mutableStateOf("")

    var totalCost by mutableStateOf("")

    val currentSubEvent: StateFlow<List<VehicleHistoryItem>> = repo.observeSingleSubEvent(eventId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateFuelingData(newVol: String = volume, newPrice: String = pricePerLiter) {
        volume = newVol.replace(',', '.')
        pricePerLiter = newPrice.replace(',', '.')
        val v = volume.toDoubleOrNull() ?: 0.0
        val p = pricePerLiter.toDoubleOrNull() ?: 0.0
        if (v > 0 && p > 0) {
            totalCost = String.format("%.2f", v * p).replace(',', '.')
        }
    }

    fun updateTripDistance(newDist: String) {
        distance = newDist.replace(',', '.')
        val d = distance.toDoubleOrNull() ?: 0.0
        val avgCons = 8.0
        val fuelPrice = 52.0
        if (d > 0) {
            val calculated = (d / 100) * avgCons * fuelPrice
            totalCost = String.format("%.2f", calculated).replace(',', '.')
        }
    }


    fun addTrip(isBus: Boolean) = viewModelScope.launch {
        val addedDist = distance.toIntOrNull() ?: 0
        val addedCost = totalCost.toDoubleOrNull() ?: 0.0

        repo.insertTripDetails(eventId, startPoint, endPoint, addedDist, isBus)

        repo.addToEventStats(eventId, addedCost, addedDist)
    }

    fun addFueling(fuelId: Int, isFull: Boolean) = viewModelScope.launch {
        val addedCost = totalCost.toDoubleOrNull() ?: 0.0
        val addedVol = volume.toDoubleOrNull() ?: 0.0
        val priceDb = pricePerLiter.toDoubleOrNull() ?: 0.0

        val addedOdo = 0

        repo.insertFuelingDetails(eventId, fuelId, addedVol, priceDb, isFull)
        repo.addToEventStats(eventId, addedCost, addedOdo)
    }

    fun addService() = viewModelScope.launch {
        repo.insertServiceDetails(eventId, workTitle, stationName)
    }

    fun tripDelete() = viewModelScope.launch { repo.tripDeleteInId(eventId) }
    fun serviceDelete() = viewModelScope.launch { repo.serviceDeleteInId(eventId) }
    fun fuelDelete() = viewModelScope.launch { repo.fuelDeleteInId(eventId) }
}