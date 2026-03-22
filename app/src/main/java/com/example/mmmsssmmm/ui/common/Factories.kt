@file:Suppress("UNCHECKED_CAST")

package com.example.mmmsssmmm.ui.common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmmsssmmm.data.repos.EventRepository
import com.example.mmmsssmmm.data.repos.TripRepository
import com.example.mmmsssmmm.data.repos.VehicleRepository
import com.example.mmmsssmmm.ui.events.EventsViewModel
import com.example.mmmsssmmm.ui.events.TripViewModel
import com.example.mmmsssmmm.ui.vehicles.VehiclesViewModel

class VehiclesVMFactory(private val repo: VehicleRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VehiclesViewModel(repo) as T
    }
}

class EventsVMFactory(private val repo: EventRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return EventsViewModel(repo, SavedStateHandle()) as T
    }
}

class TripVMFactory(private val repo: TripRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass : Class<T>): T{
        return TripViewModel(repo, SavedStateHandle()) as T
    }
}