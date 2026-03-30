package com.example.mmmsssmmm.data.fulldetails

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mmmsssmmm.data.dictionary.BrandDictEntity
import com.example.mmmsssmmm.data.dictionary.BrandTypeDictEntity
import com.example.mmmsssmmm.data.dictionary.ModelDictEntity
import com.example.mmmsssmmm.data.entity.VehiclesEntity

data class FullVehicleDetails(
    @Embedded val vehicle: VehiclesEntity,
    val brandName: String,
    val modelName: String,
    val typeName: String,
    val imageResName: String
)
