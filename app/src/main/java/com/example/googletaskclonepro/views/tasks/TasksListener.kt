package com.example.googletaskclonepro.views.tasks

import com.example.googletaskclonepro.model.task.Task

interface TasksListener {

    fun onClickTask(task: Task)

    fun showTaskScreen(task:Task)

    fun onMoveTask(from: Int, to: Int)

}