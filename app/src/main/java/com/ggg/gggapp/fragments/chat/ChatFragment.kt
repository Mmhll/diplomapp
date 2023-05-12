package com.ggg.gggapp.fragments.chat

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.auth0.android.jwt.JWT
import com.ggg.gggapp.databinding.FragmentChatBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.viewmodel.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ChatViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater)
        binding.sendButton.setOnClickListener {
            if (binding.messageChat.text.isNotEmpty()) {
                /*Messenger().sendMessage(
                    message = MessageClass(
                        messageText = binding!!.messageChat.text.toString(),
                        time = currentDate,
                        userId = Firebase.auth.uid
                    )
                )*/
                binding.messageChat.text.clear()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE).getString("token", "").toString()
        val jwtParser = JWTParser(token)
        val id = jwtParser.getId()
        viewModel.getChats("Bearer $token", id)
        viewModel.status.observe(viewLifecycleOwner){
            when (it){
                ApiStatus.COMPLETE -> {
                    AlertDialog.Builder(requireContext())
                        .setMessage(viewModel.chats.value.toString())
                        .show()
                }
                ApiStatus.FAILED -> {
                    Log.e("Error", "hmm")
                }

                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}