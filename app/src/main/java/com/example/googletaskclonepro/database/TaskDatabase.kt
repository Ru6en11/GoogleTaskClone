package com.example.googletaskclonepro.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.googletaskclonepro.model.task.Task

@Database(entities = [ Task::class ], version = 1, exportSchema = false)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}