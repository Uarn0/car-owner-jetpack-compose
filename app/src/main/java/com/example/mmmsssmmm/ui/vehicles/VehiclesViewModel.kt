package com.example.mmmsssmmm.ui.vehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.VehicleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VehiclesViewModel(private val repo: VehicleRepository): ViewModel(){
    val vehicles = repo.observerVehicle()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())
    fun add(name: String, type: Int, image: Int) =
        viewModelScope.launch { repo.insert(name, type, image) }
    fun delete(id: Long) =
        viewModelScope.launch { repo.deleteById(id) }
}