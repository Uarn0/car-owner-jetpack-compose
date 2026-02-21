package com.example.mmmsssmmm.domain.eventModel

data class RepairEvent(
    override val id: Long,
    override val vehicleId: Long,
    override val name: String = "Repair",
    override val type: Int = 0,
    override val timeWhenAdded: String
): IEvent