package com.example.mmmsssmmm.data.dictionary

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "body_types")
data class BrandTypeDictEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val typeName: String
)