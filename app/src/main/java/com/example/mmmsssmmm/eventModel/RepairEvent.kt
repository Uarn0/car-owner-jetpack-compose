package com.example.mmmsssmmm.eventModel

data class RepairEvent(
    override val name: String = "Repair",
    override val type: Int = 0,
    override val timeWhenAdded: String
): IEvent