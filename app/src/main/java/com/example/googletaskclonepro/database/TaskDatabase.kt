package com.example.googletaskclonepro.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.googletaskclonepro.model.task.Task

@Database(entities = [ Task::class ], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}