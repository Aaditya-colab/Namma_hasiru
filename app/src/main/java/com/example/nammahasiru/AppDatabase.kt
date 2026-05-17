package com.example.nammahasiru

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Plant::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): PlantDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "db"
                ).fallbackToDestructiveMigration(true)
                    .build()
            }
            return INSTANCE!!
        }
    }
}
