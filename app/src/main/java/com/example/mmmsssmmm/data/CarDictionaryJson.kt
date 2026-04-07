package com.example.mmmsssmmm.data

import com.example.mmmsssmmm.data.dictionary.BrandDictEntity
import com.example.mmmsssmmm.data.dictionary.BrandTypeDictEntity
import com.example.mmmsssmmm.data.dictionary.FuelDictEntity
import com.example.mmmsssmmm.data.dictionary.ModelDictEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
data class CarDictionaryJson(
    val brands: List<BrandDictEntity>,
    val models: List<ModelDictEntity>,
    val fuelTypeDict: List<FuelDictEntity>,
    val bodyTypes: List<BrandTypeDictEntity>
)