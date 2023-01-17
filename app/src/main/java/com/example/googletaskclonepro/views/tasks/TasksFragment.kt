package com.example.googletaskclonepro.views.tasks

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.screenViewModel
import com.example.googletaskclonepro.R
import com.example.googletaskclonepro.databinding.CreateTaskBottomSheetDialogBinding
import com.example.googletaskclonepro.databinding.FragmentTasksBinding
import com.example.googletaskclonepro.model.task.Task
import com.google.android.material.bottomsheet.BottomSheetDialog

class TasksFragment : BaseFragment() {

    class Screen : BaseScreen

    private lateinit var binding: FragmentTasksBinding
    override val viewModel: TasksViewModel by screenViewModel()
    private lateinit var createTaskDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)

        createTaskDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        createTaskDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        renderCreateTaskDialog()

        binding.createItemFab.setOnClickListener {
            createTaskDialog.show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tasks.observe(viewLifecycleOwner) {
            Log.d("tag", "${it}")
        }
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

}