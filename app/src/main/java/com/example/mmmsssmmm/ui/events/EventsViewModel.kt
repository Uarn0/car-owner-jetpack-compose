package com.example.mmmsssmmm.ui.events

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventsViewModel(
    private val repo: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val vehicleId: Long = checkNotNull(savedStateHandle["vehicleId"])

    val events = repo.observeEvents(vehicleId)
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    fun add(name: String, type: Int, time: String) = viewModelScope.launch { repo.insert(vehicleId, name, type, time) }

    fun delete(id: Long) = viewModelScope.launch { repo.deleteById(id) }
}