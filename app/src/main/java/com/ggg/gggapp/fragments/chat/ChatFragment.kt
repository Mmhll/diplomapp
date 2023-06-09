package com.ggg.gggapp.fragments.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.R
import com.ggg.gggapp.adapters.ChatAdapter
import com.ggg.gggapp.adapters.OnItemClickListener
import com.ggg.gggapp.databinding.FragmentChatBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.viewmodel.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ChatViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE).getString("token", "").toString()
        val jwtParser = JWTParser(token)
        val id = jwtParser.getId()
        val adapter = ChatAdapter(requireContext())
        viewModel.getChats(token, id)
        viewModel.status.observe(viewLifecycleOwner){
            when (it){
                ApiStatus.COMPLETE -> {
                    binding.recyclerChat.adapter = adapter
                    adapter.setChats(viewModel.chats.value!!)
                    adapter.setOnItemClickListener(object: OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            requireActivity().getSharedPreferences("chat_id", Context.MODE_PRIVATE).edit().putLong(
                                "id", viewModel.chats.value!![position].id).apply()
                            findNavController().navigate(R.id.action_navigation_chat_to_oneChatFragment)
                        }
                    })
                }
                else -> {

                }
            }
        }
        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.isRefreshing = false
            viewModel.getChats("Bearer $token", id)
            viewModel.status.observe(viewLifecycleOwner){
                when (it){
                    ApiStatus.COMPLETE -> {
                        binding.recyclerChat.adapter = adapter
                        adapter.setChats(viewModel.chats.value!!)
                    }
                    ApiStatus.FAILED -> {
                    }
                    else -> {

                    }
                }
            }
        }
        binding.createChatButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_chat_to_createChatFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}