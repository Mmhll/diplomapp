package com.ggg.gggapp.fragments.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggg.gggapp.R
import com.ggg.gggapp.adapters.OneChatAdapter
import com.ggg.gggapp.databinding.FragmentOneChatBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.utils.generateEnding
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
        viewModel.restore()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var sharedPrefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("token", "")!!
        sharedPrefs = requireActivity().getSharedPreferences("chat_id", Context.MODE_PRIVATE)
        val id = sharedPrefs.getLong("id", 0)
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
                    val chat = viewModel.chat.value!!
                    binding.oneChatName.text = chat.title
                    val userCount = chat.users.size
                    val oneChatCountText = "$userCount участник${generateEnding(userCount)}"
                    binding.oneChatCount.text = oneChatCountText
                    val data = chat.messages
                    if (adapter.getMessages() != data.toMutableList()) {
                        adapter.addMessages(data.toMutableList())
                        binding.recyclerOneChat.scrollToPosition(data.size - 1)
                    }
                }
                ApiStatus.FAILED -> {}
                else -> {}
            }
        }
        binding.sendMessageButton.setOnClickListener {
            val message = binding.newMessageInput.text.toString()
            if (message.isNotEmpty()) {
                binding.newMessageInput.text.clear()
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
        binding.addChatMember.setOnClickListener {
            findNavController().navigate(R.id.action_oneChatFragment_to_addUserFragment)
        }
        binding.linearChatUsersButton.setOnClickListener {
            findNavController().navigate(R.id.action_oneChatFragment_to_chatUsersFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clear()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}