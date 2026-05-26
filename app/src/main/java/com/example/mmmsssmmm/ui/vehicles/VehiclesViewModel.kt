package com.example.mmmsssmmm.ui.vehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.VehicleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class CarFilter { ALL, JDM, MODERN_OR_LARGE, WITH_SERVICE }
class VehiclesViewModel(private val repo: VehicleRepository) : ViewModel() {

    private val _currentFilter = MutableStateFlow(CarFilter.ALL)
    val currentFilter = _currentFilter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val vehicles = _currentFilter.flatMapLatest { filter ->
        when (filter) {
            CarFilter.ALL -> repo.observeAllVehiclesFull()
            CarFilter.JDM -> repo.observeJdmCars()
            CarFilter.MODERN_OR_LARGE -> repo.observeModernOrLargeCapacityCars()
            CarFilter.WITH_SERVICE -> repo.observeCarsWithServiceHistory()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(filter: CarFilter) {
        _currentFilter.value = filter
    }


    val brands = repo.observeBrands()

    private val _selectedBrandId = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val modelsForSelectedBrand = _selectedBrandId.flatMapLatest { brandId ->
        if (brandId != null) {
            repo.observeModels(brandId)
        } else {
            flowOf(emptyList())
        }
    }

    fun loadModelsForBrand(brandId: Long) {
        _selectedBrandId.value = brandId
    }

    fun add(
        modelId: Long,
        manufactureYear: Int,
        tankCapacity: Double,
        plateNumber: String,
    ) {
        viewModelScope.launch {
            repo.insert(modelId, manufactureYear, tankCapacity, plateNumber)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            repo.deleteById(id)
        }
    }
}