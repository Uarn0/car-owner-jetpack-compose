package com.example.mmmsssmmm.eventModel

data class TripEvent(
    override val name: String = "Trip",
    override val type: Int = 1,
    override val timeWhenAdded: String
) : IEvent
