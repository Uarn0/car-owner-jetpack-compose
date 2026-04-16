package com.example.mmmsssmmm.data.tuples

data class TotalCostForFuelTuple(
    val plateNumber: String,
    val carName: String,
    val date: String,
    val fuelType: String,
    val totalCost: Double
)
data class FuelTypeCostTuple(
    val fuelType: String,
    val totalCost: Double
)