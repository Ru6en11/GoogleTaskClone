package com.example.googletaskclonepro.model.task

import androidx.lifecycle.LiveData
import com.example.foundation.model.Repository
import com.example.googletaskclonepro.model.task.Task


interface TaskRepository : Repository {

    fun getTasks(): LiveData<List<Task>>
    fun getFavouriteTasks(): LiveData<List<Task>>
    fun getCompletedTasks(): LiveData<List<Task>>

    suspend fun updateTask(task: Task)

    suspend fun removeTask(task: Task)

    suspend fun add(task: Task)

    fun moveTask(from: Int, to: Int)

}