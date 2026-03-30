package com.example.mmmsssmmm.data.dictionary

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "models",
    foreignKeys = [
        ForeignKey(
            entity = BrandDictEntity::class,
            parentColumns = ["id"],
            childColumns = ["brandId"],
            onDelete = ForeignKey.CASCADE,
    ), ForeignKey(
            entity = BrandTypeDictEntity::class,
            parentColumns = ["id"],
            childColumns = ["bodyTypeId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("brandId"), Index("bodyTypeId")]
)

data class ModelDictEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val brandId: Long,
    val bodyTypeId: Int,
    val name: String,
    val yearStart: Int,
    val yearEnd: Int,
    val imageResName: String
)
