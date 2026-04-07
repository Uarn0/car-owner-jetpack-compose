package com.example.mmmsssmmm.data.dictionary

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "models",
    indices = [Index("brandId"), Index("bodyTypeId")]
)
data class ModelDictEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val brandId: Long,
    val bodyTypeId: Long,
    val name: String,
    val yearStart: Int,
    val yearEnd: Int,
    val imageResName: String
)