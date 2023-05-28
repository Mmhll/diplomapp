package com.ggg.gggapp.fragments.service

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.R
import com.ggg.gggapp.adapters.OnItemClickListener
import com.ggg.gggapp.adapters.UserClickableAdapter
import com.ggg.gggapp.databinding.FragmentUsersBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.viewmodel.common.CommonServiceUserViewModel
import com.ggg.gggapp.viewmodel.service.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UsersFragment : Fragment() {
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<UsersViewModel>()
    private val commonViewModel by activityViewModels<CommonServiceUserViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
            .getString("token", "").toString()
        val jwtParser = JWTParser(token)
        viewModel.findAll(token)
        binding.userRefresher.setOnRefreshListener {
            binding.userRefresher.isRefreshing = false
            viewModel.findAll(token)
        }

        val adapter = UserClickableAdapter(requireContext())
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                commonViewModel.user.value = viewModel.users.value!![position]
                findNavController().navigate(R.id.action_usersFragment_to_oneUserFragment)
            }
        })
        viewModel.userStatus.observe(viewLifecycleOwner) {
            if (it == ApiStatus.COMPLETE) {
                val users = viewModel.users.value!!
                if (jwtParser.getClaimString("permission") == "ROLE_MODERATOR") {
                    users.removeIf { user -> user.roles[0].permissions.name == "ROLE_ADMIN" }
                }
                adapter.setUsers(users)
            }
        }
        binding.userRecyclerView.adapter = adapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}