package com.ggg.gggapp.fragments.tasks

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
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
import com.ggg.gggapp.adapters.OnItemClickListener
import com.ggg.gggapp.adapters.TaskAdapter
import com.ggg.gggapp.databinding.FragmentTaskBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.viewmodel.common.CommonTaskViewModel
import com.ggg.gggapp.viewmodel.tasks.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TasksFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TasksViewModel>()
    private val commonViewModel by activityViewModels<CommonTaskViewModel>()

    private val spinnerItems = arrayOf("Постановщик", "Исполнитель", "Участник")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("token", "")!!
        val jwtParser = JWTParser(token)
        val recyclerAdapter = TaskAdapter(requireContext())
        recyclerAdapter.setOnItemClickListener(object: OnItemClickListener{
            override fun onItemClick(position: Int) {
                commonViewModel.task.value = viewModel.tasks.value!![position].id
                findNavController().navigate(R.id.action_navigation_task_to_oneTaskFragment)
            }
        })
        binding.recycleView.adapter = recyclerAdapter
        binding.taskRefresher.setOnRefreshListener {
            binding.taskRefresher.isRefreshing = false
            viewModel.getTasks(binding.taskSpinner.selectedItemPosition, token, jwtParser.getId())
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, spinnerItems
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.taskSpinner.gravity = Gravity.CENTER_HORIZONTAL
        binding.taskSpinner.adapter = adapter
        binding.taskSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.getTasks(position, token, jwtParser.getId())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.getTasks(0, token, jwtParser.getId())
            }
        }
        viewModel.taskStatus.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.COMPLETE -> {
                    recyclerAdapter.setTasks(viewModel.tasks.value!!)
                }
                else -> {
                    recyclerAdapter.setTasks(arrayListOf())
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}