package com.ggg.gggapp.fragments.service

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ggg.gggapp.R
import com.ggg.gggapp.adapters.OnItemClickListener
import com.ggg.gggapp.adapters.RoleAdapter
import com.ggg.gggapp.databinding.FragmentRolesBinding
import com.ggg.gggapp.utils.ApiStatus
import com.ggg.gggapp.utils.PermissionName
import com.ggg.gggapp.utils.permissionNameAscii
import com.ggg.gggapp.viewmodel.service.RolesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RolesFragment : Fragment() {
    private var _binding: FragmentRolesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RolesViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRolesBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
            .getString("token", "").toString()
        viewModel.findAll(token)
        val adapter = RoleAdapter(requireContext())
        viewModel.roleStatus.observe(viewLifecycleOwner) {
            if (it == ApiStatus.COMPLETE) {
                adapter.setRoles(viewModel.roles.value!!)
            }
        }
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val role = viewModel.roles.value!![position]
                val dialogLayout = layoutInflater.inflate(R.layout.dialog_update_role, null)

                val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
                    requireActivity(), R.layout.simple_spinner_item_black, permissionNameAscii
                ).also {
                    it.setDropDownViewResource(R.layout.simple_spinner_item_drop)
                }
                val roleNameInput: EditText = dialogLayout.findViewById(R.id.dialogRoleName)
                val permissionSpinner: Spinner =
                    dialogLayout.findViewById(R.id.dialogPermissionSpinner)

                permissionSpinner.adapter = spinnerAdapter
                dialogLayout.layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                AlertDialog.Builder(requireContext()).setView(dialogLayout)
                    .setPositiveButton("Изменить") { _, _ ->

                        val permission = when (permissionSpinner.selectedItem.toString()) {
                            permissionNameAscii[0] -> PermissionName.ROLE_USER.name
                            permissionNameAscii[1] -> PermissionName.ROLE_EDITUSER.name
                            permissionNameAscii[2] -> PermissionName.ROLE_MODERATOR.name
                            permissionNameAscii[3] -> PermissionName.ROLE_ADMIN.name
                            else -> {
                                ""
                            }
                        }
                        if (permission.isNotEmpty() && roleNameInput.text.toString().isNotEmpty()) {
                            viewModel.updateRole(
                                token, role.id, roleNameInput.text.toString(), permission
                            )
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Вы оставили поле пустым",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.setNegativeButton("Отмена", null).show()
            }
        })
        binding.roleRecyclerView.adapter = adapter
        binding.rolesRefresher.setOnRefreshListener {
            binding.rolesRefresher.isRefreshing = false
            viewModel.findAll(token)
        }
        binding.addRoleButton.setOnClickListener {
            val dialogLayout = layoutInflater.inflate(R.layout.dialog_update_role, null)

            val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireActivity(), R.layout.simple_spinner_item_black, permissionNameAscii
            ).also {
                it.setDropDownViewResource(R.layout.simple_spinner_item_drop)
            }
            val roleNameInput: EditText = dialogLayout.findViewById(R.id.dialogRoleName)
            val permissionSpinner: Spinner =
                dialogLayout.findViewById(R.id.dialogPermissionSpinner)

            permissionSpinner.adapter = spinnerAdapter
            dialogLayout.layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

            AlertDialog.Builder(requireContext()).setView(dialogLayout)
                .setPositiveButton("Изменить") { _, _ ->
                    val permission = when (permissionSpinner.selectedItem.toString()) {
                        permissionNameAscii[0] -> PermissionName.ROLE_USER.name
                        permissionNameAscii[1] -> PermissionName.ROLE_EDITUSER.name
                        permissionNameAscii[2] -> PermissionName.ROLE_MODERATOR.name
                        permissionNameAscii[3] -> PermissionName.ROLE_ADMIN.name
                        else -> {
                            ""
                        }
                    }
                    if (permission.isNotEmpty() && roleNameInput.text.toString().isNotEmpty()) {
                        viewModel.addRole(
                            token, roleNameInput.text.toString(), permission
                        )
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Вы оставили поле пустым",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.setNegativeButton("Отмена", null).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}