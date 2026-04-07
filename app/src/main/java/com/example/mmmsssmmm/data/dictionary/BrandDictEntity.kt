package com.example.mmmsssmmm.data.dictionary

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "brands")
data class BrandDictEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val brandName: String
)