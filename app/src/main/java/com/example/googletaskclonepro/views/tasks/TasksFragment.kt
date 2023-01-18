package com.example.googletaskclonepro.views.tasks

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.screenViewModel
import com.example.googletaskclonepro.R
import com.example.googletaskclonepro.databinding.CreateTaskBottomSheetDialogBinding
import com.example.googletaskclonepro.databinding.FragmentTasksBinding
import com.example.googletaskclonepro.model.task.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator

val categories = arrayOf(
    "Избранные",
    "Мои задачи",
    "Выполненые"
)

class TasksFragment : BaseFragment(), TasksListener {

    class Screen : BaseScreen

    override val viewModel: TasksViewModel by screenViewModel()
    private lateinit var binding: FragmentTasksBinding
    private lateinit var viewPager2Adapter: ViewPager2Adapter
    private lateinit var createTaskDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)

        viewPager2Adapter = ViewPager2Adapter(this)
        binding.categoryViewPager2.adapter = viewPager2Adapter

        TabLayoutMediator(binding.categoryTabLayout, binding.categoryViewPager2) {tab, position ->

            if (position == 0) {
                tab.setIcon(R.drawable.ic_star)
                val tabIconColor = ContextCompat.getColor(requireContext(), R.color.blue)
                tab.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            } else {
                tab.text = categories[position]
            }
        }.attach()

        createTaskDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        createTaskDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        renderCreateTaskDialog()

        binding.createItemFab.setOnClickListener {
            createTaskDialog.show()
        }

        return binding.root
    }

    private fun renderCreateTaskDialog() {

        val dialogBinding = CreateTaskBottomSheetDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        createTaskDialog.setContentView(dialogBinding.root)

        dialogBinding.saveTaskButton.isEnabled = false

        dialogBinding.additionalInfoImageBtn.setOnClickListener {
            dialogBinding.additionalInfoEditText.visibility = View.VISIBLE
        }
        dialogBinding.addInFavouriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            dialogBinding.addInFavouriteCheckBox.setButtonDrawable(
                if (isChecked) R.drawable.ic_star else R.drawable.ic_star_border
            )
        }

        dialogBinding.taskTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                dialogBinding.saveTaskButton.isEnabled = s?.isNotBlank() == true
            }
        })

        dialogBinding.saveTaskButton.setOnClickListener {
            val task = Task(
                isCompleted = false,
                text = dialogBinding.taskTitleEditText.text.toString(),
                additionalInfo = dialogBinding.additionalInfoEditText.text.toString(),
                isFavourite = dialogBinding.addInFavouriteCheckBox.isChecked
            )
            viewModel.createTask(task)
            createTaskDialog.dismiss()
        }
        dialogBinding.taskTitleEditText.requestFocus()

        createTaskDialog.setOnDismissListener {
            dialogBinding.apply {
                taskTitleEditText.text.clear()
                additionalInfoEditText.text.clear()
                additionalInfoEditText.visibility = View.GONE
                addInFavouriteCheckBox.isChecked = false
            }
        }

    }

    override fun onClickTask(task: Task) {
        viewModel.updateTask(task)
    }

    override fun showTaskScreen(task: Task) {
        viewModel.onShowDetailsCalled(task)
    }

    override fun onMoveTask(from: Int, to: Int) {
        //move items
    }

    override fun observeData(lifecycleOwner: LifecycleOwner, adapter: TasksAdapter, position: Int) {
        when (position) {
            0 -> viewModel.favouriteTasks.observe(lifecycleOwner) { adapter.tasks = it.toMutableList() }
            1 -> viewModel.tasks.observe(lifecycleOwner) { adapter.tasks = it.toMutableList() }
            2 -> viewModel.completedTask.observe(lifecycleOwner) { adapter.tasks = it.toMutableList() }
        }
    }

}