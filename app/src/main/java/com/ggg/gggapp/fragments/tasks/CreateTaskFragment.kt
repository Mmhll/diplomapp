package com.ggg.gggapp.fragments.tasks

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.R
import com.ggg.gggapp.adapters.OnItemClickListener
import com.ggg.gggapp.adapters.UserRadioAdapter
import com.ggg.gggapp.databinding.FragmentCreateTaskBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.utils.getCurrentDate
import com.ggg.gggapp.utils.getCurrentDateTime
import com.ggg.gggapp.viewmodel.tasks.CreateTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {

    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CreateTaskViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "")!!
        val date = getCurrentDate()
        val parser = JWTParser(token)
        binding.taskDeadline.setOnClickListener {
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
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(
                    R.color.blue, requireContext().theme
                )
            )
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                resources.getColor(
                    android.R.color.holo_red_light,
                    requireContext().theme
                )
            )
        }

        val adapter = UserRadioAdapter(requireContext())
        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(position: Int) {
                binding.executorRecycler.post {
                    adapter.notifyDataSetChanged()
                }
            }

        })
        binding.executorRecycler.adapter = adapter


        viewModel.getUsers(token)
        viewModel.userStatus.observe(viewLifecycleOwner) {
            if (it == ApiStatus.COMPLETE) {
                adapter.setUsers(viewModel.users.value!!)
            }
        }


        binding.createTaskButton.setOnClickListener {
            val name = binding.taskNameInput.text.toString()
            val description = binding.taskDescriptionInput.text.toString()
            val deadline = binding.taskDeadline.text.toString()
            val executor = adapter.getUser()
            val dateTimeArray = getCurrentDateTime()
            //2023-05-23T06:57:30.925Z
            val creationDate =
                "${dateTimeArray[0]}-${dateTimeArray[1]}-${dateTimeArray[2]}T${dateTimeArray[3]}:${dateTimeArray[4]}:${dateTimeArray[5]}.${dateTimeArray[6]}Z"
            if (name.isNotEmpty() && description.isNotEmpty() && executor != null) {
                viewModel.createTask(
                    token,
                    creationDate,
                    name,
                    parser.getId(),
                    description,
                    deadline,
                    executor.id,
                    arrayListOf<Long>()
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