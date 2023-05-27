package com.ggg.gggapp.fragments.chat

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
import com.ggg.gggapp.adapters.ChatUserAdapter
import com.ggg.gggapp.databinding.FragmentChatUsersBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.viewmodel.chat.ChatUsersViewModel
import com.ggg.gggapp.viewmodel.common.CommonChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatUsersFragment : Fragment() {


    private var _binding: FragmentChatUsersBinding? = null
    private val binding get() = _binding!!
    private val commonViewModel by activityViewModels<CommonChatViewModel>()

    private val viewModel by viewModels<ChatUsersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatUsersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ChatUserAdapter(requireContext())
        binding.usersRecycler.adapter = adapter
        var sharedPrefs = requireActivity().getSharedPreferences("chat_id", Context.MODE_PRIVATE)
        val chatId = sharedPrefs.getLong("id", 0)
        sharedPrefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("token", "")!!
        viewModel.getChat(chatId, token)
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.COMPLETE -> {
                    val chat = viewModel.chat.value!!
                    commonViewModel.chat.value = chat
                    val users = chat.users
                    adapter.setUsers(users.toMutableList())
                }
                else -> {}
            }
        }
        binding.usersAddUserButton.setOnClickListener {
            findNavController().navigate(R.id.action_chatUsersFragment_to_addUserFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}