package com.ggg.gggapp.fragments.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.R
import com.ggg.gggapp.databinding.FragmentOneTaskBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.utils.generateInitials
import com.ggg.gggapp.viewmodel.common.CommonTaskViewModel
import com.ggg.gggapp.viewmodel.tasks.OneTaskViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OneTaskFragment : Fragment() {

    private var _binding: FragmentOneTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<OneTaskViewModel>()
    private val commonViewModel by activityViewModels<CommonTaskViewModel>()

    private val spinnerItems = arrayListOf("Новая", "В работе", "Выполненная", "Завершённая")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOneTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("token", "")!!
        val jwtParser = JWTParser(token)
        val taskId = commonViewModel.task.value!!
        viewModel.getTask(token, taskId)
        viewModel.taskStatus.observe(viewLifecycleOwner) {
            if (it == ApiStatus.COMPLETE) {
                val task = viewModel.task.value
                when (jwtParser.getId()) {
                    task!!.creator.id -> {
                        binding.editTaskButton.visibility = View.VISIBLE
                        binding.deleteButton.visibility = View.VISIBLE
                        binding.membersLinear.visibility = View.VISIBLE
                    }

                    task.executor.id -> {
                        spinnerItems.removeLast()
                        binding.membersLinear.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.status.isEnabled = false
                    }
                }
                val creator = "Постановщик: ${generateInitials(task.creator.userData)}"
                val executor = "Исполнитель: ${generateInitials(task.executor.userData)}"
                var members = "Участники: "
                val deadline = "Дедлайн: ${task.deadline}"
                val created = "Создано: ${task.creation_date}"
                task.members.forEach { user ->
                    members += "${generateInitials(user.userData)}\t"
                }
                binding.owner.text = creator
                binding.taskExecutor.text = executor
                binding.taskMembers.text = members
                binding.deadline.text = deadline
                binding.dateOfCreation.text = created

                binding.status.setSelection(spinnerItems.indexOf(task.status))
            }
        }


        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireActivity(),
            R.layout.simple_spinner_item, spinnerItems
        ).also {
            it.setDropDownViewResource(R.layout.simple_spinner_item_drop)
        }
        binding.status.adapter = adapter
        binding.status.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.updateStatus(token, commonViewModel.task.value!!, spinnerItems[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Nothing to do
            }
        }
        binding.deleteButton.setOnClickListener {
            viewModel.deleteTask(token, taskId)
            findNavController().popBackStack()
            findNavController().navigateUp()
        }
        binding.addMembersButton.setOnClickListener {
            if (viewModel.taskStatus.value!! == ApiStatus.COMPLETE) {
                commonViewModel.users.value = viewModel.task.value!!.members
                commonViewModel.isDeleteAction = false
                findNavController().navigate(R.id.action_oneTaskFragment_to_addAndRemoveMembersFragment)
            }
        }
        binding.deleteMembersButton.setOnClickListener {
            if (viewModel.taskStatus.value!! == ApiStatus.COMPLETE) {
                commonViewModel.users.value = viewModel.task.value!!.members
                commonViewModel.isDeleteAction = true
                findNavController().navigate(R.id.action_oneTaskFragment_to_addAndRemoveMembersFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}