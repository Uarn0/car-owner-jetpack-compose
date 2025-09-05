package com.example.mmmsssmmm.eventModel

data class ServiceEvent(
    override val name: String = "Service",
    override val type: Int = 2,
    override val timeWhenAdded: String
): IEvent