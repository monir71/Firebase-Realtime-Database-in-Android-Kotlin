package com.example.footballdilalpur

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                val token = task.result
                Log.d("FirebaseToken", "Token: $token")
            }
            else
            {
                Log.w("FirebaseToken", "Fetching FCM registration token failed", task.exception)
            }
        }

        val userRef = FirebaseDatabase.getInstance().getReference("CopyRight")
        userRef.setValue("I am here to help you.")

        val contactRef = FirebaseDatabase.getInstance().getReference("Contact")
        val contactModel = ContactModel("Md Moniruzzaman", "01710389323")
        val contactId : String = contactRef.push().key ?: ""
        contactRef.child(contactId).setValue(contactModel)

        contactRef.child("Contact").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children){
                    val name = child.child("name").getValue(ContactModel::class.java)
                    val phone = child.child("phoneNumber").getValue(ContactModel::class.java)
                    Log.d("MyData", "Name: $name and Phone: $phone")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DB Error : ", error.toString())
            }
        })



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}