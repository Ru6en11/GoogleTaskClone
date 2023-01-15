package com.example.googletaskclonepro.views.tasks

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.googletaskclonepro.database.TaskDatabase
import com.example.googletaskclonepro.model.task.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "tasks-database"

class InDatabaseTaskRepository private constructor(context: Context): TaskRepository {

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val tasksDao = database.taskDao()

    private var tasks: MutableList<Task> =  mutableListOf()


    override fun getTasks(): LiveData<List<Task>> = tasksDao.getTasks()

    override suspend fun updateTask(task: Task) {
        tasks.forEachIndexed { ind, t ->
            if (t.id == task.id) {
                tasks[ind] = task
            }
        }
        tasksDao.updateTask(task)

    }

    override suspend fun removeTask(task: Task) {
        tasks.remove(task)
        tasksDao.deleteTask(task)
    }

    override suspend fun add(task: Task) {
        Log.d("tag", "add ")
        tasks.add(task)
        tasksDao.addTask(task)
    }

    override fun moveTask(from: Int, to: Int) {
        Collections.swap(tasks, from, to)
    }

    companion object {

        private var INSTANCE: InDatabaseTaskRepository? = null

        fun get(): InDatabaseTaskRepository {
            return INSTANCE ?: throw IllegalStateException("InDatabaseTaskRepository must be initialize")
        }

        fun initial(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = InDatabaseTaskRepository(context)
            }
        }

    }

}