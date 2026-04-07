package com.example.mmmsssmmm.data.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mmmsssmmm.data.entity.ServiceEntity

@Dao
interface ServiceDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(service: ServiceEntity)

    @Query("DELETE FROM service WHERE eventId = :id")
    suspend fun delete(id: Long)
}
