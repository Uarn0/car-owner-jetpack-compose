package com.example.mmmsssmmm.ui.events

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.TripRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiceViewModel(private val repo: ServiceRepository, savedStateHandle: SavedStateHandle
): ViewModel() {
    private val globalEventId: Long = checkNotNull(savedStateHandle["globalEventId"])

    fun add(workTitle: String, serviceStation: String) = viewModelScope.launch { repo.insert(
        workTitle = workTitle,
        serviceStation = serviceStation
    ) }

    fun delete(id: Long) = viewModelScope.launch { repo.deleteInId(id) }
}

