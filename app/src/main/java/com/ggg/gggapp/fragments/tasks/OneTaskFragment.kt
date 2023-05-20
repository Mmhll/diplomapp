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
import com.ggg.gggapp.databinding.FragmentOneTaskBinding
import com.ggg.gggapp.utils.JWTParser
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
        val task = commonViewModel.task.value!!

        if (task.members.find { user ->
                task.creator.id == user.id && user.id == jwtParser.getId() ||
                        task.executor.id == user.id && task.executor.id == jwtParser.getId()
        } == null) {
            binding.status.isEnabled = false
        } else {
            if (task.creator.id != task.executor.id) {
                spinnerItems.removeLast()
            }
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, spinnerItems
        )

        binding.status.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        binding.status.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}