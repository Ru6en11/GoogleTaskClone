package com.example.googletaskclonepro.views.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.foundation.navigator.Navigator
import com.example.foundation.views.BaseViewModel
import com.example.googletaskclonepro.model.task.Task
import com.example.googletaskclonepro.model.task.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DetailViewModel(
    private val navigator: Navigator,
    private val taskRepository: TaskRepository
) : BaseViewModel() {

    private val taskId = MutableLiveData<UUID>()
    val task: LiveData<Task?> = Transformations.switchMap(taskId) {taskId ->
        taskRepository.getById(taskId)
    }

    fun load(_taskId: UUID) {
        taskId.value = _taskId
    }

    fun save(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.removeTask(task.value!!)
        }
    }

    fun goBackPressed() {
        navigator.goBack()
    }
}