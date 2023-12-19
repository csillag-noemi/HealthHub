package com.example.healthhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthhub.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var databaseReference: DatabaseReference
    private var isEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Initialize the Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance("https://health-hub-44ed5-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")

        // Retrieve user data from the Realtime Database
        userId?.let { userId ->
            val userReference = databaseReference.child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User data exists, retrieve and populate the UI
                        val userName = dataSnapshot.child("name").getValue(String::class.java)
                        val userPhone = dataSnapshot.child("phoneNumber").getValue(String::class.java)

                        // Populate the UI
                        binding.userName.text = userName
                        binding.userPhone.text = userPhone
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors if any
                    Toast.makeText(this@ProfileActivity, "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

}


