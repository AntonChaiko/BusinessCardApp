package com.example.businesscard.fragments.savefragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.businesscard.R
import com.example.businesscard.data.Contacts
import com.example.businesscard.data.ContactsViewModel
import com.example.businesscard.databinding.FragmentSaveBinding
import com.google.zxing.integration.android.IntentIntegrator
import java.io.ByteArrayOutputStream


class SaveFragment : Fragment() {

    private lateinit var binding: FragmentSaveBinding
    private var photo: Bitmap? = null
    private lateinit var mContactsViewModel: ContactsViewModel
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save, container, false)

        mContactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)

        if (savedInstanceState != null) {
            photo = savedInstanceState.getParcelable("photo")
        }

        if (photo != null) {
            binding.circleImageView.setImageBitmap(photo)
        }
        binding.toolbarAddButton?.setOnClickListener {
            val contacts = Contacts(
                0, binding.fullNameTextInput.text.toString(),
                binding.phoneTextInput.text.toString(),
                binding.emailTextInput.text.toString(),
                encodePhoto(photo)

            )
            mContactsViewModel.addContacts(contacts)
            findNavController().navigate(R.id.action_saveFragment_to_contactFragment)
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
        binding.scanQrButton?.setOnClickListener {
            IntentIntegrator.forSupportFragment(this).initiateScan()


        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111) {
            photo = data?.getParcelableExtra("data")
            binding.circleImageView.setImageBitmap(photo)
        }
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
            } else {


                val parts = result.contents.split(":")
                parts.map { it.trim() }
                binding.fullNameTextInput.setText(parts[0])
                binding.phoneTextInput.setText(parts[1])
                binding.emailTextInput.setText(parts[2])
                Toast.makeText(requireContext(), "Scanned: " + result.contents, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("photo", photo)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodePhoto(photo: Bitmap?): String? {
        val bos = ByteArrayOutputStream()
        photo?.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val byteArray: ByteArray = bos.toByteArray()
        return java.util.Base64.getEncoder().encodeToString(byteArray)
    }
}