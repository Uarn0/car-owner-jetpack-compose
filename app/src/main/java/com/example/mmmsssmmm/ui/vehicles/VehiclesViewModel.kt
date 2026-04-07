package com.example.mmmsssmmm.ui.vehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmmsssmmm.data.repos.VehicleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VehiclesViewModel(private val repo: VehicleRepository) : ViewModel() {

    val vehicles = repo.observeAllVehiclesFull()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val brands = repo.observeBrands()

    private val _selectedBrandId = MutableStateFlow<Long?>(null)


    @OptIn(ExperimentalCoroutinesApi::class)
    val modelsForSelectedBrand = _selectedBrandId.flatMapLatest { brandId ->
        if (brandId != null) {
            repo.observeModels(brandId) // Беремо з DictionaryDao
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
        imageUri: String? = null
    ) {
        viewModelScope.launch {
            repo.insert(modelId, manufactureYear, tankCapacity, plateNumber, imageUri)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            repo.deleteById(id)
        }
    }
}