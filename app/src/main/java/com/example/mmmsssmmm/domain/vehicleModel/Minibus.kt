package com.example.mmmsssmmm.domain.vehicleModel

import com.example.mmmsssmmm.R

data class Minibus(
    override val id: Long,
    override val name: String,
    override val type: Int = 1,
    override val image: Int = R.drawable.ic_minibus
): IVehicle