package com.example.healthhub

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthhub.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Provide the Firebase Realtime Database URL with the correct region
        val databaseReference = FirebaseDatabase.getInstance("https://health-hub-44ed5-default-rtdb.europe-west1.firebasedatabase.app/").reference
        firebaseAuth = FirebaseAuth.getInstance()

        binding.mainSignupBtn.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val pass = binding.signupPass.editText?.text.toString()
            val confirmPass = binding.signupConfirmPass.editText?.text.toString()
            val name = binding.signupName.text.toString()
            val phoneNumber = binding.signupPhone.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // User created successfully, now store additional information
                            val user = User(name, phoneNumber)
                            val userId = FirebaseAuth.getInstance().currentUser?.uid

                            userId?.let {
                                val userReference = databaseReference.child("users").child(it)
                                userReference.setValue(user)
                            }

                            Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
