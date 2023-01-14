package com.example.googletaskclonepro.views.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foundation.views.BaseViewModel
import com.example.googletaskclonepro.model.task.Task

class TasksViewModel : BaseViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    fun updateTask(task: Task) {

    }

    fun onMoveTask(from: Int, to: Int) {

    }

    fun removeTask(task: Task) {

    }

    fun createTask(task: Task) {

    }


}