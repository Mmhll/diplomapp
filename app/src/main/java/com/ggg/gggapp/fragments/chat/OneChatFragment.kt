package com.ggg.gggapp.fragments.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggg.gggapp.adapters.OneChatAdapter
import com.ggg.gggapp.databinding.FragmentOneChatBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.viewmodel.chat.OneChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OneChatFragment : Fragment() {

    private val viewModel by viewModels<OneChatViewModel>()
    private var _binding: FragmentOneChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOneChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var sharedPrefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("token", "")
        sharedPrefs = requireActivity().getSharedPreferences("chat_id", Context.MODE_PRIVATE)
        val id = sharedPrefs.getLong("id", 0)
        Log.e("id", "ID: $id; user_id: ${JWTParser(token!!).getId()}")
        val jwt = JWTParser(token)
        val adapter = OneChatAdapter(jwt.getId())
        val manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        manager.stackFromEnd = true
        manager.isSmoothScrollbarEnabled = false
        binding.recyclerOneChat.adapter = adapter
        binding.recyclerOneChat.layoutManager = manager
        viewModel.getChat(id, token)
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.COMPLETE -> {
                    val data = viewModel.chat.value!!.messages
                    if (adapter.getMessages() != data.toMutableList()) {
                        adapter.addMessages(data.toMutableList())
                        binding.recyclerOneChat.scrollToPosition(data.size - 1)
                    }
                }
                ApiStatus.FAILED -> {

                }
                else -> {}
            }
        }
        binding.sendMessageButton.setOnClickListener {
            val message = binding.newMessageInput.text.toString()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(token, JWTParser(token).getId(), message, id)
                viewModel.messageStatus.observe(viewLifecycleOwner){
                    when (it){
                        ApiStatus.FAILED -> {
                            Log.e("TAG", "something went wrong")
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }
}