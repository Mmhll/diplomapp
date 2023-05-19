package com.ggg.gggapp.fragments.chat

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.adapters.UserAdapter
import com.ggg.gggapp.databinding.FragmentAddUserBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.viewmodel.chat.AddUserViewModel
import com.ggg.gggapp.viewmodel.common.CommonChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserFragment : Fragment() {

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AddUserViewModel>()
    private val commonViewModel by activityViewModels<CommonChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var prefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "")!!
        prefs = requireActivity().getSharedPreferences("chat_id", Context.MODE_PRIVATE)
        val id = prefs.getLong("id", 0)
        val adapter = UserAdapter(requireContext())
        binding.createChatUserRecyclerView.adapter = adapter
        viewModel.getUsers(token)
        viewModel.userStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                ApiStatus.COMPLETE -> {
                    commonViewModel.chat.observe(viewLifecycleOwner) {
                        val chatUsers = it.users
                        if (chatUsers.isNotEmpty()) {
                            val users = viewModel.users.value!!
                            users.removeAll(chatUsers.toSet())
                            adapter.setUsers(users)
                        }
                    }
                }
                else -> {}
            }
        }
        binding.addUserButton.setOnClickListener {
            val userIds = adapter.getUserIds()
            if (userIds.size != 0) {
                viewModel.addUser(token, userIds as ArrayList<Long>, id)
                viewModel.chatStatus.observe(viewLifecycleOwner) {
                    when (it) {
                        ApiStatus.COMPLETE -> {
                            userIds.forEach {
                                viewModel.users.value!!.removeIf { user -> user.id != it }
                            }
                            commonViewModel.chat.value!!.users.addAll(viewModel.users.value!!)
                            AlertDialog.Builder(requireContext())
                                .setTitle("Успешно")
                                .setMessage("Выбранные пользователи были добавлены")
                                .setPositiveButton("Ok") { _, _ ->
                                    findNavController().popBackStack()
                                }
                                .setOnCancelListener {
                                    findNavController().popBackStack()
                                }
                                .create()
                                .show()
                        }
                        ApiStatus.FAILED -> {
                            Log.e("Error", "Something went wrong")
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}