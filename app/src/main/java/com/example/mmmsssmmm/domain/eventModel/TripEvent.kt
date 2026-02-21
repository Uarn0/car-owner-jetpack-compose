package com.example.mmmsssmmm.domain.eventModel

data class TripEvent(
    override val id: Long,
    override val vehicleId: Long,
    override val name: String = "Trip",
    override val type: Int = 1,
    override val timeWhenAdded: String
) : IEvent
