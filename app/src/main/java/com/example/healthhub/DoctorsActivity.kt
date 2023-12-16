package com.example.healthhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthhub.databinding.ActivityAppointmentsBinding
import com.example.healthhub.databinding.ActivityDoctorsBinding
import com.example.healthhub.databinding.ActivityLoginBinding

class DoctorsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}