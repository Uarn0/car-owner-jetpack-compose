package com.example.mmmsssmmm.data.dictionary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("fuel_types")
data class FuelDictEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nameOfFuel: String
)
