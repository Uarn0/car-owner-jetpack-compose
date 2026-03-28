package com.example.mmmsssmmm.data.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mmmsssmmm.data.entity.ServiceEntity

@Dao
interface ServiceDAO {
    @Insert
    suspend fun insert(service: ServiceEntity)

    @Query("DELETE FROM events WHERE globalEventId = :id")
    suspend fun delete(id: Long)
}
