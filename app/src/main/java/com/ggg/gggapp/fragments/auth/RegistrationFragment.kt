package com.ggg.gggapp.fragments.auth

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.databinding.FragmentRegBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.viewmodel.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "")!!

        binding.RegButton.setOnClickListener {
            val email = binding.emailText.text.toString()
            val password = binding.passwordText.text.toString()
            val firstname = binding.firstnameText.text.toString()
            val lastname = binding.lastnameText.text.toString()
            val middlename = binding.middlenameText.text.toString()
            val phoneNumber = binding.phoneNumberText.text.toString()
            if (
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                firstname.isNotEmpty() &&
                lastname.isNotEmpty() &&
                middlename.isNotEmpty() &&
                phoneNumber.isNotEmpty()
            ) {
                viewModel.register(
                    token,
                    email,
                    password,
                    firstname,
                    lastname,
                    middlename,
                    phoneNumber
                )
                viewModel.status.observe(viewLifecycleOwner) {
                    when (it) {
                        ApiStatus.COMPLETE -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Пользователь зарегистрирован")
                                .setMessage(
                                    "Для входа в поле username используйте ваш первоначальный email до знака '@' (${
                                        email.split(
                                            '@'
                                        )[0]
                                    })\nСоветуем изменить пароль при первом входе"
                                )
                                .setPositiveButton("Ok") { _, _ ->
                                    findNavController().popBackStack()
                                }
                                .create()
                                .show()
                        }
                        ApiStatus.FAILED -> {
                            Toast.makeText(requireContext(), "Пользователь с этим первоначальным email уже зарегистрирован", Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Какое-то из полей пустое", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}