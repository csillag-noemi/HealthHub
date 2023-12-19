package com.example.healthhub
import androidx.lifecycle.ViewModel
import common.Appointment

class AppointmentsViewModel : ViewModel() {
    private val appointmentsRepository = AppointmentsRepository.getInstance()
    private var appointmentsList: List<Appointment> = emptyList()

    // Function to retrieve appointments
    fun getAppointments(): List<Appointment> {
        appointmentsList = appointmentsRepository.getAppointments()
        return appointmentsList
    }

    // Function to add an appointment
    fun addAppointment(appointment: Appointment) {
        appointmentsRepository.addAppointment(appointment)
    }
}
