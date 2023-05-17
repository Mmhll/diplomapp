package com.ggg.gggapp.fragments.chat

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ggg.gggapp.adapters.UserRecycler
import com.ggg.gggapp.databinding.FragmentCreateChatBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.viewmodel.chat.CreateChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateChatFragment : Fragment() {

    private val viewModel by viewModels<CreateChatViewModel>()
    private var _binding: FragmentCreateChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "")!!
        val adapter = UserRecycler(requireContext())
        binding.createChatUserRecyclerView.adapter = adapter

        viewModel.getUsers(token)
        viewModel.userStatus.observe(viewLifecycleOwner){
            when (it){
                ApiStatus.COMPLETE -> {
                    adapter.setUsers(viewModel.users.value!!.toMutableList())
                }
                else -> {}
            }
        }
        binding.createNewChatButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage(adapter.getUserIds().toString())
                .create()
                .show()
        }
    }

}