package com.example.mmmsssmmm.domain.eventModel

data class ServiceEvent(
    override val id: Long,
    override val vehicleId: Long,
    override val name: String = "Service",
    override val type: Int = 2,
    override val timeWhenAdded: String
): IEvent