@file:Suppress("UNCHECKED_CAST")

package com.example.mmmsssmmm.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mmmsssmmm.data.repos.EventRepository
import com.example.mmmsssmmm.data.repos.VehicleRepository
import com.example.mmmsssmmm.statsScreen.StatsViewModel
import com.example.mmmsssmmm.ui.events.EventsViewModel
import com.example.mmmsssmmm.ui.events.SubEventViewModel
import com.example.mmmsssmmm.ui.vehicles.VehiclesViewModel

class VehiclesVMFactory(private val repo: VehicleRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VehiclesViewModel(repo) as T
    }
}

class EventsVMFactory(
    private val repo: EventRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()

        return when {
            modelClass.isAssignableFrom(EventsViewModel::class.java) -> {
                EventsViewModel(repo, savedStateHandle) as T
            }

            modelClass.isAssignableFrom(SubEventViewModel::class.java) -> {
                SubEventViewModel(repo, savedStateHandle) as T
            }

            modelClass.isAssignableFrom(StatsViewModel::class.java) -> {
                StatsViewModel(repo) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}