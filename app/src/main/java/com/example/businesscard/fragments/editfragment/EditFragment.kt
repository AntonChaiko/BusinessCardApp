package com.example.businesscard.fragments.editfragment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.businesscard.R
import com.example.businesscard.databinding.FragmentEditBinding


class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding
    var photo: Bitmap? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false)
        if (savedInstanceState != null) {
            photo = savedInstanceState.getParcelable("photo")
        }
        when (arguments?.isEmpty) {
            true -> {
            }
            false -> arguments?.let {
                val args = EditFragmentArgs.fromBundle(it)
                binding.fullNameTextInput.setText(args.fullName)
                binding.phoneTextInput.setText(args.phone)
                binding.emailTextInput.setText(args.email)
                photo = args.photo
            }
        }
        if (photo != null) {
            binding.circleImageView.setImageBitmap(photo)
        }
        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.action_editFragment_to_saveFragment)
        }
        binding.callButton.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)

            if (binding.phoneTextInput.text.toString().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Phone field must be not empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                callIntent.data = Uri.parse("tel:" + binding.phoneTextInput.text.toString())
                startActivity(callIntent)
            }
        }
        binding.emailButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            val email = binding.emailTextInput.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Email field must be not empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter correct email!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.putExtra(
                    Intent.EXTRA_EMAIL,
                    Array(1) { email })

                startActivity(emailIntent)
            }
        }

        return binding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("photo", photo)
    }
}