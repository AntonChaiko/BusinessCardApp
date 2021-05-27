package com.example.businesscard.fragments.savefragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.businesscard.R
import com.example.businesscard.databinding.FragmentEditBinding
import com.example.businesscard.databinding.FragmentSaveBinding


class SaveFragment : Fragment() {
    private lateinit var binding: FragmentSaveBinding
    var photo: Bitmap? = null
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save, container, false)
        binding.toolbarSaveButton.setOnClickListener {
            val direction = SaveFragmentDirections.actionSaveFragmentToEditFragment(
                binding.fullNameTextInput.text.toString(),
                binding.phoneTextInput.text.toString(),
                binding.emailTextInput.text.toString(),
                photo!!
            )
            it.findNavController().navigate(direction)
        }
        binding.circleImageView.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(requireContext() as Activity, permissions, 111)
            }
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 111)
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111) {
            photo = data?.getParcelableExtra("data")
            binding.circleImageView.setImageBitmap(photo)
        }
    }
}