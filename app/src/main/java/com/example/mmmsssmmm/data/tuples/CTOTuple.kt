package com.example.mmmsssmmm.data.tuples

data class CTOTuple(
    val plateNumber: String,
    val carName: String,
    val date: String,
    val station: String,
    val whatWork: String,
    val cost: Double
)
data class CtoCostByCarTuple(
    val carName: String,
    val plateNumber: String,
    val totalCost: Double
)

data class CtoStationCountTuple(
    val stationName: String,
    val visitCount: Int
)