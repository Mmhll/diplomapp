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
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.adapters.UserAdapter
import com.ggg.gggapp.databinding.FragmentAddUserBinding
import com.ggg.gggapp.model.User
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.viewmodel.chat.AddUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserFragment : Fragment() {

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AddUserViewModel>()

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
        val jwtParser = JWTParser(token)
        binding.createChatUserRecyclerView.adapter = adapter
        viewModel.getUsers(token)
        viewModel.userStatus.observe(viewLifecycleOwner){ status ->
            when (status){
                ApiStatus.COMPLETE -> {
                    val users = viewModel.users.value!!
                    val usersWithoutCurrent: MutableList<User> = ArrayList()
                    users.forEach {
                        if (it.id != jwtParser.getId()) {
                            usersWithoutCurrent.add(it)
                        }
                    }
                    adapter.setUsers(usersWithoutCurrent)
                }
                else -> {}
            }
        }
        binding.addUserButton.setOnClickListener {
            if (adapter.getUserIds().size != 0) {
                val users = adapter.getUserIds()
                viewModel.addUser(token, users as ArrayList<Long>, id)
                viewModel.chatStatus.observe(viewLifecycleOwner) {
                    when (it) {
                        ApiStatus.COMPLETE -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Успешно")
                                .setMessage("Выбранные пользователи были добавлены")
                                .setPositiveButton("Ok") { _, _ ->
                                    findNavController().navigateUp()
                                }
                                .setOnCancelListener{
                                    findNavController().navigateUp()
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