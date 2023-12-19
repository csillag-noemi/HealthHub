package com.example.healthhub


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.healthhub.databinding.ActivityAppointmentsBinding
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import common.Appointment

class AppointmentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppointmentsBinding
    private lateinit var appointmentsViewModel: AppointmentsViewModel
    private lateinit var appointmentsRepository: AppointmentsRepository

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appointmentsViewModel = ViewModelProvider(this).get(AppointmentsViewModel::class.java)

        // Log a message to confirm that the repository is initialized
        Log.d("AppointmentsActivity", "AppointmentsRepository initialized")

        // Retrieve the list of appointments
        val appointmentsList = appointmentsViewModel.getAppointments()

        // Log the size of the retrieved list
        Log.d("AppointmentsActivity", "Retrieved appointments list size: ${appointmentsList?.size}")

        displayAppointments(appointmentsList)

    }


    private fun displayAppointments(appointmentsList: List<Appointment>?) {
        Log.d("AppointmentsActivity", "Received appointmentsList: $appointmentsList")
        val appointmentsText = StringBuilder()

        if (appointmentsList.isNullOrEmpty()) {
            Log.d("appointmentsActivity", "appointments list is empty or null.")
            appointmentsText.append("No appointments available.")
        } else {
            for (appointment in appointmentsList) {
                val doctor = appointment.doctor
                val date = appointment.date
                val time = appointment.time
                val formattedAppointment = "Doctor: $doctor, Date: $date, Time: $time"
                appointmentsText.append(formattedAppointment).append("\n")
            }
        }

        binding.apptsList.text = appointmentsText.toString().trim()
    }
}
