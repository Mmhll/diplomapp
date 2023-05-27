package com.ggg.gggapp.fragments.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.adapters.OnItemClickListener
import com.ggg.gggapp.adapters.UserRadioAdapter
import com.ggg.gggapp.databinding.FragmentUpdateExecutorBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.viewmodel.common.CommonTaskViewModel
import com.ggg.gggapp.viewmodel.tasks.UpdateExecutorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateExecutorFragment : Fragment() {

    private var _binding: FragmentUpdateExecutorBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<UpdateExecutorViewModel>()
    private val commonViewModel by activityViewModels<CommonTaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateExecutorBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("token", "")!!
        viewModel.getUsers(token)
        val adapter = UserRadioAdapter(requireContext())
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                binding.dialogExecutorRecycler.post {
                    adapter.notifyDataSetChanged()
                }
            }
        })
        viewModel.userStatus.observe(viewLifecycleOwner) {
            if (it == ApiStatus.COMPLETE) {
                adapter.setUsers(viewModel.users.value!!)
            }
        }
        binding.dialogExecutorRecycler.adapter = adapter
        binding.updateExecutorButton.setOnClickListener {
            if (adapter.getUser() == null) {
                Toast.makeText(
                    requireContext(), "Вы не выбрали ни одного исполнителя", Toast.LENGTH_SHORT
                ).show()
            } else {
                if (adapter.getUser() != commonViewModel.fullTask.value!!.executor) {

                    viewModel.updateExecutor(
                        token, commonViewModel.task.value!!, adapter.getUser()!!.id
                    )
                    Toast.makeText(requireContext(), "Исполнитель был изменён", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    Toast.makeText(requireContext(), "Исполнитель не изменился", Toast.LENGTH_SHORT)
                        .show()
                }
                findNavController().navigateUp()
            }
        }
    }

}