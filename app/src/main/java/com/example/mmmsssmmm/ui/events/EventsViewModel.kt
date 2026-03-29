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

    val events = repo.observeEvents(vehicleId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTrip(
        name: String, date: String, odometer: Int, totalCost: Double,
        startPoint: String, endPoint: String, distanceKM: Int, isBusiness: Boolean
    ) = viewModelScope.launch {
        repo.insertTripEvent(
            vehicleId = vehicleId, // Беремо ID поточної машини з ViewModel
            name = name, time = date,
            odometer = odometer, totalCost = totalCost,
            startPoint = startPoint, endPoint = endPoint,
            distanceKM = distanceKM, isBusiness = isBusiness
        )
    }


    fun addFueling(
        name: String, date: String, odometer: Int, totalCost: Double,
        fuelTypeId: Int, volumeLiters: Double, pricePerLiter: Double, isFullTank: Boolean
    ) = viewModelScope.launch {
        repo.insertFuelingEvent(
            vehicleId = vehicleId,
            name = name, date = date,
            odometer = odometer, totalCost = totalCost,
            fuelTypeId = fuelTypeId, volumeLiters = volumeLiters,
            pricePerLiter = pricePerLiter, isFullTank = isFullTank
        )
    }

    // Додати сервіс/ремонт
    fun addService(
        name: String, date: String, odometer: Int, totalCost: Double,
        workTitle: String, serviceStation: String
    ) = viewModelScope.launch {
        repo.insertServiceEvent(
            vehicleId = vehicleId,
            name = name, date = date,
            odometer = odometer, totalCost = totalCost,
            workTitle = workTitle, serviceStation = serviceStation
        )
    }

    fun allDelete(id: Long) = viewModelScope.launch { repo.deleteInId(id) }

    // (Ці методи можна залишити, якщо вони є в репозиторії, але зазвичай достатньо одного allDelete)
    fun tripDelete(id: Long) = viewModelScope.launch { repo.tripDeleteInId(id) }
    fun serviceDelete(id: Long) = viewModelScope.launch { repo.serviceDeleteInId(id) }
    fun fuelDelete(id: Long) = viewModelScope.launch { repo.fuelDeleteInId(id) }
}