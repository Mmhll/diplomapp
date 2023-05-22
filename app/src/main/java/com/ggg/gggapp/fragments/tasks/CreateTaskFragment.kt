package com.ggg.gggapp.fragments.tasks

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ggg.gggapp.R
import com.ggg.gggapp.databinding.FragmentCreateTaskBinding
import com.ggg.gggapp.utils.getCurrentDate
import com.ggg.gggapp.viewmodel.common.CommonTaskViewModel
import com.ggg.gggapp.viewmodel.tasks.CreateTaskViewModel

class CreateTaskFragment : Fragment() {

    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CreateTaskViewModel>()
    private val commonViewModel by activityViewModels<CommonTaskViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = getCurrentDate()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DialogTheme,
            { _, year, monthOfYear, dayOfMonth ->
                val pickedDate = ("$year-${monthOfYear + 1}-${dayOfMonth}")
                binding.taskDeadline.setText(pickedDate)
            },
            date[0],
            date[1],
            date[2]
        )
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(
            R.color.blue, requireContext().theme))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(android.R.color.holo_red_light, requireContext().theme))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}