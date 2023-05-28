package com.ggg.gggapp.fragments.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ggg.gggapp.R
import com.ggg.gggapp.databinding.FragmentServicesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesFragment : Fragment() {

    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editRolesButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_services_to_rolesFragment)
        }
        binding.editUsersButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_services_to_usersFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}