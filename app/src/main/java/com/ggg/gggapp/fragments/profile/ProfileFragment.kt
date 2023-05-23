package com.ggg.gggapp.fragments.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.auth0.android.jwt.JWT
import com.ggg.gggapp.R
import com.ggg.gggapp.activities.MainActivity
import com.ggg.gggapp.databinding.FragmentProfileBinding
import com.ggg.gggapp.remote_model.UpdatePasswordRequest
import com.ggg.gggapp.remote_model.UpdateUserRequest
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.viewmodel.auth.AuthViewModel
import com.ggg.gggapp.viewmodel.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("token", "").toString()
        var password = sharedPrefs.getString("password", "").toString()
        val jwtParser = JWTParser(token)

        val id = jwtParser.getId()
        val username = jwtParser.getSubject()

        updateData(jwtParser)


        binding.changeButton.setOnClickListener {
            viewModel.setUserData(
                token, UpdateUserRequest(
                    id = id,
                    email = binding.emailProfile.text.toString(),
                    phone_number = binding.phoneProfile.text.toString(),
                    firstname = binding.firstnameProfile.text.toString(),
                    lastname = binding.lastnameProfile.text.toString(),
                    middlename = binding.middlenameProfile.text.toString(),
                )
            )
            viewModel.updateStatus.observe(viewLifecycleOwner) {
                when (it) {
                    ApiStatus.COMPLETE -> {
                        Toast.makeText(
                            requireContext(), "Данные успешно изменены", Toast.LENGTH_SHORT
                        ).show()
                        Log.e("TAG", jwtParser.getSubject())
                        authViewModel.authorize(jwtParser.getSubject(), password)
                        authViewModel.status.observe(viewLifecycleOwner){ status ->
                            if (status == ApiStatus.COMPLETE){
                                sharedPrefs.edit().putString("token", authViewModel.data.value!!.token).apply()
                                updateData(JWTParser(authViewModel.data.value!!.token))
                                Log.e("TAG", "PIZDA")
                            }

                        }
                    }
                    ApiStatus.FAILED -> {
                        Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {}
                }
            }
        }
        binding.changePasswordButton.setOnClickListener {
            val dialogLayout = layoutInflater.inflate(R.layout.dialog_set_password, null)
            AlertDialog.Builder(requireContext()).setView(dialogLayout)
                .setPositiveButton("Сменить пароль") { _, _ ->
                    val passwordText: EditText = dialogLayout.findViewById(R.id.newPassword)
                    val rePasswordText: EditText = dialogLayout.findViewById(R.id.newPasswordRepeat)
                    if (passwordText.text.toString() == rePasswordText.text.toString()) {
                        viewModel.setUserPassword(
                            token, UpdatePasswordRequest(id, username, passwordText.text.toString())
                        )
                        viewModel.updateStatus.observe(viewLifecycleOwner) {
                            when (it) {
                                ApiStatus.COMPLETE -> {
                                    Toast.makeText(
                                        requireContext(), "Пароль изменён", Toast.LENGTH_SHORT
                                    ).show()
                                    password = passwordText.text.toString()
                                    sharedPrefs.edit().putString("password", password).apply()
                                }
                                ApiStatus.FAILED -> {
                                    Toast.makeText(
                                        requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {}
                            }
                        }
                    }
                }.setNegativeButton("Отмена", null).show()
        }
        binding.logoutButton.setOnClickListener {
            sharedPrefs.edit().putString("token", "").apply()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
        binding.createUserButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_registrationFragment)
        }

    }

    private fun updateData(jwtParser: JWTParser){
        binding.emailProfile.setText(jwtParser.getClaimString("email"))
        binding.phoneProfile.setText(jwtParser.getClaimString("phone_number"))
        binding.firstnameProfile.setText(jwtParser.getClaimString("firstname"))
        binding.lastnameProfile.setText(jwtParser.getClaimString("lastname"))
        binding.middlenameProfile.setText(jwtParser.getClaimString("middlename"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}