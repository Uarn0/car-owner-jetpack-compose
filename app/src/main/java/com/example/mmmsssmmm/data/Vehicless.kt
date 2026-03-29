package com.example.mmmsssmmm.data

sealed class Vehicless {
    abstract val id: Long
    abstract val brand: String
    abstract val model: String
    abstract val type: String
    abstract val image: String
    abstract val year: Int

    data class Car(
        override val id: Long,
        override val brand: String,
        override val model: String,
        override val type: String,
        override val image: String,
        override val year: Int,
        val tankCapacity: Double
    ): Vehicless()

    data class Minibus(
        override val id: Long,
        override val brand: String,
        override val model: String,
        override val type: String,
        override val image: String,
        override val year: Int,
    ) : Vehicless()

    data class Motorcycle(
        override val id: Long,
        override val brand: String,
        override val model: String,
        override val type: String,
        override val image: String,
        override val year: Int
    ) : Vehicless()
}