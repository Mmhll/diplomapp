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
import com.ggg.gggapp.R
import com.ggg.gggapp.adapters.UserAdapter
import com.ggg.gggapp.databinding.FragmentAddAndRemoveMembersBinding
import com.ggg.gggapp.model.User
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.viewmodel.common.CommonTaskViewModel
import com.ggg.gggapp.viewmodel.tasks.AddAndRemoveMembersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAndRemoveMembersFragment : Fragment() {
    private var _binding: FragmentAddAndRemoveMembersBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddAndRemoveMembersViewModel>()
    private val commonViewModel by activityViewModels<CommonTaskViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAndRemoveMembersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "")!!
        val adapter = UserAdapter(requireContext())
        binding.updateMembersRecyclerView.adapter = adapter
        viewModel.getUsers(token)
        viewModel.userStatus.observe(viewLifecycleOwner) {
            if (it == ApiStatus.COMPLETE) {
                if (commonViewModel.isDeleteAction) {
                    adapter.setUsers(commonViewModel.users.value!!)
                } else {
                    val users: ArrayList<User> = viewModel.users.value!!
                    users.removeAll(commonViewModel.users.value!!.toSet())
                    adapter.setUsers(users)
                }
            }
        }


        binding.updateMembersButton.setOnClickListener {
            if (commonViewModel.isDeleteAction) {
                viewModel.deleteMembers(token, commonViewModel.task.value!!, adapter.getUserIds() as ArrayList<Long>)
            } else {
                viewModel.addMembers(token, commonViewModel.task.value!!, adapter.getUserIds() as ArrayList<Long>)
            }
            viewModel.apiStatus.observe(viewLifecycleOwner){
                if (it == ApiStatus.COMPLETE){
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.action_addAndRemoveMembersFragment_to_oneTaskFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}