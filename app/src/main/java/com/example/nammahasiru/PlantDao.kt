package com.example.nammahasiru

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlantDao {

    @Insert
    suspend fun insert(p: Plant)

    @Update
    suspend fun update(p: Plant)

    @Query("SELECT * FROM plants")
    fun getAll(): LiveData<List<Plant>>

    @Query("SELECT * FROM plants WHERE id = :id")
    suspend fun getById(id: Int): Plant

    @Delete
    suspend fun delete(plant: Plant)
}
