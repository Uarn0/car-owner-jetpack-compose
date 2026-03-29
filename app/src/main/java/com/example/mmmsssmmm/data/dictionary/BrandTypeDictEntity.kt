package com.example.mmmsssmmm.data.dictionary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("body_types")
data class BrandTypeDictEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val typeName: String
)
