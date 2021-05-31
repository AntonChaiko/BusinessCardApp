package com.example.businesscard.fragments.contact_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.businesscard.R
import com.example.businesscard.data.Contacts
import com.example.businesscard.data.ContactsViewModel
import com.example.businesscard.databinding.FragmentContactBinding


class ContactFragment : Fragment() {
    private lateinit var mContactsViewModel: ContactsViewModel
    private lateinit var binding: FragmentContactBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mContactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)

        binding.addNewContactButton.setOnClickListener {
            findNavController().navigate(R.id.action_contactFragment_to_saveFragment)
        }

        val adapter = ContactsAdapter(
            mContactsViewModel = mContactsViewModel,
            navController = findNavController()
        )

        mContactsViewModel.readAllData.observe(viewLifecycleOwner, Observer { words ->
            adapter.setData(words as MutableList<Contacts>)
        })

        binding.contactsRecyclerView.adapter = adapter
        binding.contactsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.contactsRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        return binding.root
    }


}