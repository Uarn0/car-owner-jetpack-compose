package com.example.mmmsssmmm.domain.item

sealed class VehicleHistoryItem {
    abstract val eventId: Long
    abstract val date: String
    abstract val odometer: Int
    abstract val totalCost: Double

    data class Base(
        override val eventId: Long,
        override val date: String,
        override val odometer: Int,
        override val totalCost: Double
    ) : VehicleHistoryItem()
    data class Trip(
        override val eventId: Long,
        override val date: String,
        override val odometer: Int,
        override val totalCost: Double,
        val startPoint: String,
        val endPoint: String,
        val distanceKM: Int,
        val isBusiness: Boolean
    ): VehicleHistoryItem()

    data class Fueling(
        override val eventId: Long,
        override val date: String,
        override val odometer: Int,
        override val totalCost: Double,
        val volumeLiters: Double,
        val pricePerLiter: Double,
        val isFullTank: Boolean
    ): VehicleHistoryItem()

    data class Service(
        override val eventId: Long,
        override val date: String,
        override val odometer: Int,
        override val totalCost: Double,
        val workTitle: String,
        val serviceStation: String,
    ): VehicleHistoryItem()
}