package com.example.healthhub


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.healthhub.databinding.ActivityAppointmentsBinding
import androidx.annotation.RequiresApi
import common.Appointment

class AppointmentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppointmentsBinding


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if (intent.hasExtra("APPOINTMENTS_LIST")) {
            val appointments = intent.getSerializableExtra("APPOINTMENTS_LIST") as? ArrayList<Appointment>
            if (appointments != null) {
                displayAppointments(appointments)
            } else {
                // Handle the case where the casting was unsuccessful (appointments is null)
                Log.e("AppointmentsActivity", "Error casting Serializable to ArrayList<Appointment>")
            }
        }
    }


    private fun displayAppointments(appointmentsList: ArrayList<Appointment>?) {
        Log.d("AppointmentsActivity", "Received appointmentsList: $appointmentsList")
        if (appointmentsList.isNullOrEmpty()) {
            Log.d("appointmentsActivity", "appointments list is empty or null.")
            binding.apptsText.text = "no appointments available."
        } else {
            // Use StringBuilder to concatenate appointments
            val appointmentsText = StringBuilder()

            for (appointment in appointmentsList) {
                val doctor = appointment.doctor
                val date = appointment.date
                val time = appointment.time

                val formattedAppointment = "Doctor: $doctor, Date: $date, Time: $time"
                appointmentsText.append(formattedAppointment).append("\n")
            }

            binding.apptsList.text = appointmentsText.toString().trim() // Set the concatenated text
        }
    }
}
