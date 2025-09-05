package com.example.mmmsssmmm.domain.vehicleModel

import com.example.mmmsssmmm.R

data class Car(
    override val id: Long,
    override val name: String,
    override val  type: Int = 0,
    override val  image: Int = R.drawable.ic_car
): IVehicle