package com.example.businesscard.fragments.editfragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Patterns
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
import androidx.navigation.fragment.navArgs
import com.example.businesscard.R
import com.example.businesscard.data.Contacts
import com.example.businesscard.data.ContactsViewModel
import com.example.businesscard.databinding.FragmentEditBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern


class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private val args by navArgs<EditFragmentArgs>()
    private lateinit var mContactsViewModel: ContactsViewModel
    private val edit = "edit"
    private val save = "save"
    var photo: Bitmap? = null
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private var regEx = Pattern.compile("^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})\$")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false)
        photo = decodePhoto(args.currentPerson.photo)
        mContactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)

        showEdit()

        binding.switchButton?.setOnClickListener {
            when (binding.switchButton!!.text) {
                save -> {
                    showEdit()
                    updatePerson()
                }
                edit -> showSave()
            }
        }

        binding.deleteButton?.setOnClickListener {
            mContactsViewModel.deleteCurrentWord(args.currentPerson)
            findNavController().navigate(R.id.action_editFragment_to_contactFragment)
        }

        binding.circleImageView.setImageBitmap(photo)
        binding.fullNameTextInput.setText(args.currentPerson.fullName)
        binding.emailTextInput.setText(args.currentPerson.email)
        binding.phoneTextInput.setText(args.currentPerson.phone)

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

        binding.callButton.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)

            if (binding.phoneTextInput.text.toString().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Phone field must be not empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (!regEx.matcher(args.currentPerson.phone.toString()).matches()) {
                Toast.makeText(
                    requireContext(),
                    "Invalid phone number!",
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
        binding.qrCodeButton.setOnClickListener {

            val qrCode = QRCodeWriter()
            val bitmix = qrCode.encode(
                "${args.currentPerson.fullName}:${args.currentPerson.phone}:${args.currentPerson.email}",
                BarcodeFormat.QR_CODE,
                150,
                150
            )

            val height: Int = 150
            val width: Int = 150
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitmix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            binding.qrCodeImageView?.setImageBitmap(bmp)

        }
        binding.contactsButton.setOnClickListener {
            findNavController().navigate(R.id.action_editFragment_to_contactFragment)
        }
        return binding.root
    }


    private fun showEdit() {
        binding.switchButton?.text = edit
        binding.emailTextInput.isFocusable = false
        binding.phoneTextInput.isFocusable = false
        binding.fullNameTextInput.isFocusable = false
        binding.callButton.visibility = View.VISIBLE
        binding.emailButton.visibility = View.VISIBLE

    }

    private fun showSave() {
        binding.switchButton?.text = save
        binding.emailTextInput.isFocusableInTouchMode = true
        binding.phoneTextInput.isFocusableInTouchMode = true
        binding.fullNameTextInput.isFocusableInTouchMode = true
        binding.callButton.visibility = View.GONE
        binding.emailButton.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatePerson() {
        val fullName = binding.fullNameTextInput.text.toString()
        val phone = binding.phoneTextInput.text.toString()
        val email = binding.emailTextInput.text.toString()
        val update =
            Contacts(args.currentPerson.id, fullName, phone, email, encodePhoto(photo))
        mContactsViewModel.update(update)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("photo", photo)
    }

    private fun decodePhoto(encodedString: String?): Bitmap? {
        val decodedString: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(
            decodedString, 0,
            decodedString.size
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111) {
            photo = data?.getParcelableExtra("data")
            binding.circleImageView.setImageBitmap(photo)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodePhoto(photo: Bitmap?): String? {
        val bos = ByteArrayOutputStream()
        photo?.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val byteArray: ByteArray = bos.toByteArray()
        return java.util.Base64.getEncoder().encodeToString(byteArray)
    }
}