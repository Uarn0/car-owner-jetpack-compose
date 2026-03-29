package com.example.mmmsssmmm.data.dictionary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("brands")
data class BrandDictEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val brandName: String
)
