package com.example.healthhub

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.healthhub.databinding.ActivityDoctorsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationCallback
import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import java.time.LocalDate
import java.time.LocalTime


class DoctorsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap // Added variable to store GoogleMap object
    private lateinit var locationCallback: LocationCallback
    private lateinit var selectedDate: LocalDate
    private lateinit var selectedTime: LocalTime
    data class Appointment(val doctor: String, val date: LocalDate, val time: LocalTime)
    private val appointments: MutableList<Appointment> = mutableListOf()


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedDate = LocalDate.now()
        selectedTime = LocalTime.now()

        binding.appointmentBtn.setOnClickListener {
            showDateTimePickerDialog()
        }

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request location permission
        requestLocationPermission()

        // Get the map fragment
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // Get the map asynchronously
        mapFragment.getMapAsync { map ->
            // Store the GoogleMap object for later use
            googleMap = map

            // Customize and work with the GoogleMap object here
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            // Enable zoom controls
            googleMap.uiSettings.isZoomControlsEnabled = true

            // Set up the location callback
            setupLocationCallback()

            // Access the device's last known location
            getLastKnownLocation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDateTimePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                showTimePickerDialog()
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )

        // Set the minimum date to today
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedTime = LocalTime.of(hourOfDay, minute)
                // Handle the selected date and time as needed (e.g., save appointment)
                handleAppointment(selectedDate, selectedTime)
            },
            selectedTime.hour,
            selectedTime.minute,
            false
        )
        timePickerDialog.show()
    }

    private fun handleAppointment(date: LocalDate, time: LocalTime?) {
        // Handle the selected date and time
        val appointment = Appointment("Dr. Smith", selectedDate, selectedTime)
        // Save the appointment to the list
        appointments.add(appointment)

        // You can access the appointments list later
        // For example, you can iterate through the list to display appointments
        for (apt in appointments) {
            Log.d("Appointment", "Doctor: ${apt.doctor}, Date: ${apt.date}, Time: ${apt.time}")
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is granted. Access the location.
            getLastKnownLocation()
        } else {
            // Request location permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult.lastLocation != null) {
                    val currentLocation = LatLng(
                        locationResult.lastLocation!!.latitude,
                        locationResult.lastLocation!!.longitude
                    )
                    // Move the camera to the user's current location
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
                }
            }
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. Use it as needed.
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Use the latitude and longitude to show nearby pins on the map.

                    // Move the camera to the user's last known location
                    val currentLocation = LatLng(latitude, longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f))

                    // Add a marker at the user's current location
                    val markerOptions = MarkerOptions()
                        .position(currentLocation)
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    googleMap.addMarker(markerOptions)
                } else {
                    // Last known location is not available. Handle accordingly.
                    Toast.makeText(this, "Last known location not available", Toast.LENGTH_SHORT).show()
                }

                // Start location updates
                startLocationUpdates()
            }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationRequest = LocationRequest.create().apply {
                interval = 5000 // 5 seconds
                fastestInterval = 3000 // 3 seconds
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    // Override onRequestPermissionsResult to handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted. Access the location.
                    getLastKnownLocation()
                } else {
                    // Permission denied. Handle accordingly.
                    // You might want to inform the user about the need for location permission.
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            // Handle other permission requests if needed.
        }
    }

    // Stop location updates when the activity is no longer in the foreground
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
