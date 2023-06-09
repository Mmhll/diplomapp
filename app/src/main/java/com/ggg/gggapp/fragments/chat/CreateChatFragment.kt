package com.ggg.gggapp.fragments.chat

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.R
import com.ggg.gggapp.adapters.UserAdapter
import com.ggg.gggapp.databinding.FragmentCreateChatBinding
import com.ggg.gggapp.model.User
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.viewmodel.chat.CreateChatViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE
        val prefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "")!!
        val adapter = UserAdapter(requireContext())
        binding.createChatUserRecyclerView.adapter = adapter
        val jwtParser = JWTParser(token)

        viewModel.getUsers(token)
        viewModel.userStatus.observe(viewLifecycleOwner) { it ->
            when (it) {
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
        binding.createNewChatButton.setOnClickListener {
            val chatName = binding.editText.text.toString()
            if (adapter.getUserIds().size != 0 && chatName.isNotEmpty()) {
                val users = adapter.getUserIds()
                users.add(jwtParser.getId())
                viewModel.createChat(token, users as ArrayList<Long>, chatName)
                viewModel.chatStatus.observe(viewLifecycleOwner) {
                    when (it) {
                        ApiStatus.COMPLETE -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Успешно")
                                .setMessage("Чат \"$chatName\" был успешно создан")
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

                        }
                        else -> {}
                    }
                }
            }
        }
    }
}