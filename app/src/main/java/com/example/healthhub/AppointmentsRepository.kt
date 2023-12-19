package com.example.healthhub

import android.util.Log
import common.Appointment

class AppointmentsRepository private constructor() {
    private val appointments = mutableListOf<Appointment>()

    fun addAppointment(appointment: Appointment) {
        appointments.add(appointment)
        Log.d("AppointmentsRepository", "Appointment added: $appointment")
    }

    fun getAppointments(): List<Appointment> {
        Log.d("AppointmentsRepository", "Current appointments list size: ${appointments.size}")
        return appointments.toList() // Return a copy to prevent external modifications
    }

    companion object {
        // Singleton pattern to ensure a single instance of the repository
        private var instance: AppointmentsRepository? = null

        fun getInstance(): AppointmentsRepository {
            if (instance == null) {
                instance = AppointmentsRepository()
            }
            return instance!!
        }
    }
}