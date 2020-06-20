package com.amg.recipesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.amg.recipesapp.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.start.setOnClickListener {
            val name = binding.name.text.toString()
            if (name.isNotEmpty())
                findNavController().navigate(StartFragmentDirections.toChatFragment(name))
            else
                binding.nameLabel.error = "Enter Your Name"
        }
        binding.name.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                binding.nameLabel.isErrorEnabled = false
        }
    }
}