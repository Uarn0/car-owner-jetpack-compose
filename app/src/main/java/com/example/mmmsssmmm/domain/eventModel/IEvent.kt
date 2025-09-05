package com.example.mmmsssmmm.domain.eventModel

sealed interface IEvent {
    val id: Long
    val vehicleId: Long
    val name: String
    val type: Int
    val timeWhenAdded: String
}
