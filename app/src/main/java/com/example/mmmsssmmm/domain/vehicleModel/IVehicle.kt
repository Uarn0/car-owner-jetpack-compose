package com.example.mmmsssmmm.domain.vehicleModel

sealed interface IVehicle {
    val id: Long
    val name: String
    val type: Int

    val image: Int
}