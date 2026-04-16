package com.example.mmmsssmmm.data.tuples

data class MostTraveledVehicleTuple(
    val plateNumber: String,
    val carName: String,
    val dateWhenAdd: String,
    val totalTraveledKM: Int,
)
data class PopularRouteTuple(
    val startPoint: String,
    val endPoint: String,
    val tripCount: Int,
    val totalRouteDistance: Int
)