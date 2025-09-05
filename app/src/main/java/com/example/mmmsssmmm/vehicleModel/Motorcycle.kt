package com.example.mmmsssmmm.vehicleModel

import com.example.mmmsssmmm.R

data class Motorcycle(
    override val name: String,
    override val type: Int = 2,
    override val image: Int = R.drawable.ic_moto
): IVehicle