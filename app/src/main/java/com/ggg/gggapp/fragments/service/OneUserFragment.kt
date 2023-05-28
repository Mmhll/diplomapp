package com.ggg.gggapp.fragments.service

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.R
import com.ggg.gggapp.databinding.FragmentOneUserBinding
import com.ggg.gggapp.model.User
import com.ggg.gggapp.remote_model.UpdatePasswordRequest
import com.ggg.gggapp.remote_model.UpdateUserRequest
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.JWTParser
import com.ggg.gggapp.viewmodel.common.CommonServiceUserViewModel
import com.ggg.gggapp.viewmodel.service.OneUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OneUserFragment : Fragment() {

    private var _binding: FragmentOneUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<OneUserViewModel>()
    private val commonViewModel by activityViewModels<CommonServiceUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOneUserBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("token", "").toString()
        val jwtParser = JWTParser(token)
        val user = commonViewModel.user.value!!
        updateOneUserData(user)
        viewModel.findAll(token)
        binding.changeButton.setOnClickListener {
            viewModel.setUserData(
                token, UpdateUserRequest(
                    id = user.id,
                    email = binding.emailOneUser.text.toString(),
                    phone_number = binding.phoneOneUser.text.toString(),
                    firstname = binding.firstnameOneUser.text.toString(),
                    lastname = binding.lastnameOneUser.text.toString(),
                    middlename = binding.middlenameOneUser.text.toString(),
                )
            )
            viewModel.updateStatus.observe(viewLifecycleOwner) {
                when (it) {
                    ApiStatus.COMPLETE -> {
                        Toast.makeText(
                            requireContext(), "Данные успешно изменены", Toast.LENGTH_SHORT
                        ).show()
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
                    if (passwordText.text.toString() == rePasswordText.text.toString() && passwordText.text.toString().length >= 6) {
                        viewModel.setUserPassword(
                            token,
                            UpdatePasswordRequest(
                                user.id,
                                user.username,
                                passwordText.text.toString()
                            )
                        )
                        viewModel.updateStatus.observe(viewLifecycleOwner) {
                            when (it) {
                                ApiStatus.COMPLETE -> {
                                    Toast.makeText(
                                        requireContext(), "Пароль изменён", Toast.LENGTH_SHORT
                                    ).show()
                                }

                                ApiStatus.FAILED -> {
                                    Toast.makeText(
                                        requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT
                                    ).show()
                                }

                                else -> {}
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Пароли не совпадают или длина пароля меньше 6 символов",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.setNegativeButton("Отмена", null).show()
        }
        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser(token, user.id.toString())
            viewModel.deleteStatus.observe(viewLifecycleOwner) {
                if (it == ApiStatus.COMPLETE) {
                    findNavController().navigateUp()
                }
            }
        }

        viewModel.roleStatus.observe(viewLifecycleOwner) {
            if (it == ApiStatus.COMPLETE) {
                val arrayList = arrayListOf<String>()
                val roles = viewModel.roles.value!!
                roles.forEach { role ->
                    if (jwtParser.getClaimString("permission") == "ROLE_ADMIN") {
                        arrayList.add(role.role_name)
                    } else {
                        if (role.permissions.name != "ROLE_ADMIN") {
                            arrayList.add(role.role_name)
                        }
                    }
                }

                binding.roleOneUser.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        when (position) {
                            0 -> {
                                viewModel.updateUserRole(
                                    token,
                                    user.id,
                                    roles.find { role -> role.role_name == arrayList[position] }!!.id
                                )
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        //Nothing to do
                    }

                }
                val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
                    requireActivity(), R.layout.simple_spinner_item_black, arrayList
                ).also { adapeter ->
                    adapeter.setDropDownViewResource(R.layout.simple_spinner_item_drop)
                }

                binding.roleOneUser.adapter = spinnerAdapter

            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateOneUserData(user: User) {
        binding.emailOneUser.setText(user.email)
        binding.phoneOneUser.setText(user.userData.phone_number)
        binding.firstnameOneUser.setText(user.userData.firstname)
        binding.lastnameOneUser.setText(user.userData.lastname)
        binding.middlenameOneUser.setText(user.userData.middlename)
    }
}