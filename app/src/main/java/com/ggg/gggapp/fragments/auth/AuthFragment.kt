package com.ggg.gggapp.fragments.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ggg.gggapp.activities.BottomNavigationActivity
import com.ggg.gggapp.databinding.FragmentAuthBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.viewmodel.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "")!!
        if (token.isNotEmpty()){
            requireActivity().startActivity(Intent(requireActivity(),
                BottomNavigationActivity::class.java))
            requireActivity().finish()
        }
        binding.authButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.authorize(email, password)
                viewModel.status.observe(viewLifecycleOwner){
                    when(it){
                        ApiStatus.COMPLETE -> {
                            prefs.edit().putString("token", viewModel.data.value!!.token).apply()
                            prefs.edit().putString("password", binding.password.text.toString()).apply()
                            requireActivity().startActivity(Intent(requireActivity(),
                                BottomNavigationActivity::class.java))
                            requireActivity().finish()
                        }
                        ApiStatus.FAILED -> {
                            Toast.makeText(requireActivity(),"Неверный логин или пароль",Toast.LENGTH_SHORT).show()
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