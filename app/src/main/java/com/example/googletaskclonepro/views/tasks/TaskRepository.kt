package com.example.googletaskclonepro.views.tasks

import androidx.lifecycle.LiveData
import com.example.foundation.model.Repository
import com.example.googletaskclonepro.model.task.Task


interface TaskRepository : Repository {

    fun getTasks(): LiveData<List<Task>>

    fun updateTask(task: Task)

    fun removeTask(task: Task)

    fun add(task: Task)

    fun moveTask(from: Int, to: Int)

}