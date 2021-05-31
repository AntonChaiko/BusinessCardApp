package com.example.businesscard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contacts::class], version = 1, exportSchema = false)
abstract class ContactsDatabase : RoomDatabase() {


    abstract fun contactsDao(): ContactsDao?

    companion object {
        @Volatile
        private var INSTANCE: ContactsDatabase? = null

        fun getDatabase(context: Context): ContactsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ContactsDatabase::class.java,
                    "contacts_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}