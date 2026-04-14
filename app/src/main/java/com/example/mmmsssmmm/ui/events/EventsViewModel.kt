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




    val baseEvents = repo.observeBaseEvents(vehicleId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun createBaseEvent(
        name: String, date: String, odometer: Int, totalCost: Double,
        onSuccess: (Long) -> Unit,
    ) = viewModelScope.launch {
        val newEventId = repo.insertBasicEvent(vehicleId, name, date, odometer, totalCost)
        onSuccess(newEventId)
    }

    fun allDelete(id: Long) = viewModelScope.launch { repo.deleteInId(id) }

}