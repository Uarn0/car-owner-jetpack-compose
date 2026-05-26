package com.example.mmmsssmmm.data.fulldetails

import androidx.room.Embedded
import com.example.mmmsssmmm.data.entity.VehiclesEntity

data class FullVehicleDetails(
    @Embedded val vehicle: VehiclesEntity,
    val brandName: String,
    val modelName: String,
    val typeName: String,
)
