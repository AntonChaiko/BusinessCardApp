package com.example.businesscard.fragments.savefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.businesscard.R
import com.example.businesscard.databinding.FragmentEditBinding
import com.example.businesscard.databinding.FragmentSaveBinding


class SaveFragment : Fragment() {
    private lateinit var binding: FragmentSaveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save, container,false)
        binding.toolbarSaveButton.setOnClickListener {
            val direction = SaveFragmentDirections.actionSaveFragmentToEditFragment(
                binding.fullNameTextInput.text.toString(),
                binding.phoneTextInput.text.toString(),
                binding.emailTextInput.text.toString()

            )
            it.findNavController().navigate(direction)
        }
        return binding.root
    }

}