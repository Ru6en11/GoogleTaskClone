package com.example.googletaskclonepro.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.googletaskclonepro.databinding.DateTimePickerFragmentBinding
import java.util.*

class TimePickerDialog : DialogFragment() {

    interface Callbacks : java.io.Serializable {
        fun onTimeSelected(date: Date)
    }

    private lateinit var binding: DateTimePickerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DateTimePickerFragmentBinding.inflate(inflater, container, false)

        binding.dateSubmitButton.setOnClickListener {
            binding.run {
                val resultDate =GregorianCalendar(
                    datePicker.year,
                    datePicker.month,
                    datePicker.dayOfMonth,
                    timePicker.hour,
                    timePicker.minute
                ).time
                targetFragment?.let {
                    (it as Callbacks).onTimeSelected(resultDate)
                }
            }
            dismiss()

        }

        return binding.root
    }

}