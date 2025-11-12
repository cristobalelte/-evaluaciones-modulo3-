package com.example.ama.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CartRow::class],
    version = 2,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
