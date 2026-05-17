package com.example.nammahasiru

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val species: String,
    val notes: String,
    val imageUri: String,
    val latitude: Double,
    val longitude: Double,
    val status: String
)
