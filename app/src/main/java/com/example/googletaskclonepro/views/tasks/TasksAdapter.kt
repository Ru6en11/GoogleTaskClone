package com.example.googletaskclonepro.views.tasks

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.googletaskclonepro.R
import com.example.googletaskclonepro.databinding.TaskItemBinding
import com.example.googletaskclonepro.model.task.Task
import java.util.*

class TasksDiffCallback(
    private val oldList: List<Task>,
    private val newList: List<Task>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition]
        val newTask = newList[newItemPosition]
        return oldTask.id == newTask.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition]
        val newTask = newList[newItemPosition]
        return oldTask == newTask
    }

}

class TasksAdapter(private val listener: TasksListener, private val adapterPos: Int) : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    var tasks: MutableList<Task> = mutableListOf()
            set(newValue) {
                val diffCallback = TasksDiffCallback(field, newValue)
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                field = newValue
                diffResult.dispatchUpdatesTo(this)
            }

    inner class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = TaskItemBinding.bind(view)

        fun bind(task: Task) = binding.run {

            root.setOnClickListener {
                listener.showTaskScreen(task, adapterPos)
            }

            isCompletedCheckBox.isChecked = task.isCompleted
            taskTitleTextView.text = task.text
            taskAdditInfoTextView.apply {
                if (task.additionalInfo.isNotBlank()) {
                    text = task.additionalInfo
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }

            isCompletedCheckBox.setOnClickListener {
                task.isCompleted = isCompletedCheckBox.isChecked
                listener.onClickTask(task)
                notifyDataChanged()
            }

            val imageRes = if (task.isFavourite) R.drawable.ic_star else R.drawable.ic_star_border
            isFavouriteImageButton.setImageResource(imageRes)

            isFavouriteImageButton.setOnClickListener {
                task.isFavourite = !task.isFavourite
                listener.onClickTask(task)
                val imRes = if (task.isFavourite) R.drawable.ic_star else R.drawable.ic_star_border
                isFavouriteImageButton.setImageResource(imRes)
                notifyDataChanged()
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TasksViewHolder(view)

    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    private fun notifyDataChanged() {
        val _tasks = tasks
        tasks = _tasks
    }

    fun moveItem(from: Int, to: Int) {
        Collections.swap(tasks, from, to)
        listener.onMoveTask(from, to)
    }

    fun addItem(item: Task, pos: Int? = null) {

        val _tasks = mutableListOf<Task>()
        tasks.forEach { _tasks.add(it) }

        if (pos == null)
            _tasks.add(item)
        else _tasks.add(pos, item)

        tasks = _tasks
    }

    fun removeItem(item: Task): Int? {

        val _tasks = mutableListOf<Task>()
        tasks.forEach { _tasks.add(it) }

        for (i in 0 until _tasks.size) {
            if (item.id == _tasks[i].id) {
                Log.d("delete", "before remove ${_tasks.size}")
                _tasks.removeAt(i)
                Log.d("delete", "after remove ${_tasks.size}")
                tasks = _tasks

                return i
            }
        }
        return null
    }

}