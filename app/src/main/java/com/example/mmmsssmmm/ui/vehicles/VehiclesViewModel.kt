package com.example.mmmsssmmm.ui.vehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.VehicleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VehiclesViewModel(private val repo: VehicleRepository): ViewModel(){
    val vehicles = repo.observerVehicle()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())
    fun add(brandId: Int, model: String, manufactureYear: Int, tankCapacity: Double, plateNumber: String, typeOfVehicle: Int) =
        viewModelScope.launch { repo.insert(
            brandId,
            model,
            manufactureYear,
            tankCapacity,
            plateNumber,
            typeOfVehicle
        ) }
    fun delete(id: Long) =
        viewModelScope.launch { repo.deleteById(id) }
}