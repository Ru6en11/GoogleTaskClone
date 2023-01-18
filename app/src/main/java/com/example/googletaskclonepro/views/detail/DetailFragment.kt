package com.example.googletaskclonepro.views.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foundation.ARG_SCREEN
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.screenViewModel
import com.example.googletaskclonepro.R
import com.example.googletaskclonepro.databinding.FragmentDetailBinding
import com.example.googletaskclonepro.model.task.Task
import java.util.*

class DetailFragment : BaseFragment() {

    class Screen(
        val id: UUID
    ) : BaseScreen

    override val viewModel: DetailViewModel by screenViewModel<DetailViewModel>()
    private lateinit var binding: FragmentDetailBinding
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screen = requireArguments().getSerializable(ARG_SCREEN) as Screen
        viewModel.load(screen.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.goBackImageButton.setOnClickListener {
            viewModel.save(task)
            viewModel.goBackPressed()
        }

        binding.addInCompletedButton.setOnClickListener {
            task.isCompleted = !task.isCompleted
            if (task.isCompleted) {
                viewModel.save(task)
                viewModel.goBackPressed()
            } else {
                renderScreen()
            }
        }

        binding.favouriteCheckBox.setOnCheckedChangeListener { btn, isChecked ->
            task.isFavourite = isChecked
            btn.setButtonDrawable(if (isChecked) R.drawable.ic_star else R.drawable.ic_star_border)
        }

        binding.deleteImageButton.setOnClickListener {
            viewModel.delete()
            viewModel.goBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.task.observe(viewLifecycleOwner) {
            it?.let {
                this.task = it
                renderScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val titleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { task.text = s.toString() }
        }
        binding.taskTitleEditText.addTextChangedListener(titleTextWatcher)

        val additTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { task.additionalInfo = s.toString() }
        }
        binding.additInfoEditText.addTextChangedListener(additTextWatcher)

    }

    private fun renderScreen() = binding.run {
        taskTitleEditText.setText(task.text)
        additInfoEditText.setText(task.text)
        favouriteCheckBox.apply {
            isChecked = task.isFavourite
            setButtonDrawable(if (isChecked) R.drawable.ic_star else R.drawable.ic_star_border)
        }
        addInCompletedButton.setText(if (task.isCompleted) R.string.mark_uncompleted else R.string.mark_completed)
    }

}