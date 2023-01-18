package com.example.googletaskclonepro.views.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foundation.views.BaseViewModel
import com.example.googletaskclonepro.model.task.InDatabaseTaskRepository
import com.example.googletaskclonepro.model.task.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasksViewModel(
    private val taskRepository: InDatabaseTaskRepository
) : BaseViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = taskRepository.getTasks()
    val favouriteTasks = taskRepository.getFavouriteTasks()
    val completedTask = taskRepository.getCompletedTasks()

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }

    fun onMoveTask(from: Int, to: Int) {

    }

    fun removeTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.removeTask(task)
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.add(task)
        }
//        taskRepository.add(task)
    }


}