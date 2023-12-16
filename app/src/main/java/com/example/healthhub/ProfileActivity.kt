package com.example.healthhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthhub.databinding.ActivityAppointmentsBinding
import com.example.healthhub.databinding.ActivityDoctorsBinding
import com.example.healthhub.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}