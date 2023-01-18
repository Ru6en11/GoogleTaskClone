package com.example.googletaskclonepro.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.googletaskclonepro.model.task.Task
import java.util.UUID

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE isCompleted = 0")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isFavourite = 1 and isCompleted = 0")
    fun getFavouriteTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 1")
    fun getCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Insert
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)
}