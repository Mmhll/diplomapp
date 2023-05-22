package com.ggg.gggapp.fragments.tasks

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.R
import com.ggg.gggapp.databinding.FragmentEditTaskBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.viewmodel.common.CommonTaskViewModel
import com.ggg.gggapp.viewmodel.tasks.EditTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTaskFragment : Fragment() {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EditTaskViewModel>()
    private val commonViewModel by activityViewModels<CommonTaskViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "")!!
        binding.deadlineInput.setText(commonViewModel.fullTask.value!!.deadline)
        binding.headerInput.setText(commonViewModel.fullTask.value!!.name)
        binding.descriptionInput.setText(commonViewModel.fullTask.value!!.description)
        binding.deadlineInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val date = arrayOf(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            DatePickerDialog(
                requireContext(),
                R.style.DialogTheme,
                { _, year, monthOfYear, dayOfMonth ->
                    val pickedDate = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.deadlineInput.setText(pickedDate)
                },
                date[0],
                date[1],
                date[2]

            ).show()
        }
        binding.editTaskEditButton.setOnClickListener {
            val deadline = binding.deadlineInput.text.toString()
            val name = binding.headerInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            if (deadline.isNotEmpty() && name.isNotEmpty() && description.isNotEmpty()) {
                viewModel.updateTask(
                    token,
                    commonViewModel.fullTask.value!!.id,
                    name,
                    description,
                    deadline
                )
                viewModel.apiStatus.observe(viewLifecycleOwner){
                    if (it == ApiStatus.COMPLETE){
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}