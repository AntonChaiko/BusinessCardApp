package com.example.businesscard.fragments.contact_fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.businesscard.R
import com.example.businesscard.data.Contacts
import com.example.businesscard.data.ContactsViewModel
import com.example.businesscard.fragments.editfragment.EditFragmentDirections

class ContactsAdapter(
    var contactsList: MutableList<Contacts> = mutableListOf(),
    private val mContactsViewModel: ContactsViewModel,
    private val navController: NavController
) : RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fullName: TextView? = null
        var email: TextView? = null
        var phone: TextView? = null
        var photo: ImageView? = null

        init {
            fullName = itemView.findViewById(R.id.full_name_adapter_text_field)
            email = itemView.findViewById(R.id.email_adapter_text_field)
            phone = itemView.findViewById(R.id.phone_adapter_text_field)
            photo = itemView.findViewById(R.id.person_image_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.contact_adapter_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = contactsList[position]

        holder.email?.text = currentItem.email
        holder.fullName?.text = currentItem.fullName
        holder.phone?.text = currentItem.phone
        holder.photo?.setImageBitmap(decodePhoto(currentItem.photo))

        holder.itemView.setOnClickListener {
            val action = ContactFragmentDirections.actionContactFragmentToEditFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    fun setData(contacts: MutableList<Contacts>) {
        this.contactsList = contacts
        notifyDataSetChanged()
    }

    private fun decodePhoto(encodedString: String?): Bitmap? {
        val decodedString: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(
            decodedString, 0,
            decodedString.size
        )
    }


}
