package com.example.individualassignment2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.individualassignment2.model.Appointment
import com.example.individualassignment2.model.Clinic
import com.example.individualassignment2.model.Doctor
import com.example.individualassignment2.ui.screens.*
import com.example.individualassignment2.ui.theme.IndividualAssignment2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val clinic = Clinic(
            name = "HealthCare Medical Center",
            address = "123 Medical Drive, Healthcare City, HC 12345",
            phone = "+1 (555) 123-4567",
            email = "contact@healthcaremedical.com",
            website = "https://www.healthcaremedical.com",
            latitude = 3.1390,  // Example coordinates for Malaysia
            longitude = 101.6869,
            description = "HealthCare Medical Center is a state-of-the-art medical facility providing comprehensive healthcare services. Our team of experienced medical professionals is committed to delivering exceptional patient care using the latest medical technologies and treatments.",
            operatingHours = mapOf(
                "Monday" to "8:00 AM - 6:00 PM",
                "Tuesday" to "8:00 AM - 6:00 PM",
                "Wednesday" to "8:00 AM - 6:00 PM",
                "Thursday" to "8:00 AM - 6:00 PM",
                "Friday" to "8:00 AM - 6:00 PM",
                "Saturday" to "9:00 AM - 1:00 PM",
                "Sunday" to "Closed"
            ),
            facilities = listOf(
                "Modern Diagnostic Equipment",
                "Laboratory Services",
                "Pharmacy",
                "Emergency Care",
                "Wheelchair Access",
                "Parking Available",
                "Patient Lounge"
            )
        )

        setContent {
            IndividualAssignment2Theme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.DoctorList) }
                var selectedDoctor: Doctor? by remember { mutableStateOf(null) }
                var currentAppointment: Appointment? by remember { mutableStateOf(null) }

                when (currentScreen) {
                    Screen.ClinicInfo -> {
                        ClinicInformationScreen(
                            clinic = clinic,
                            onNavigateBack = { currentScreen = Screen.DoctorList }
                        )
                    }
                    Screen.DoctorList -> {
                        DoctorListScreen(
                            onDoctorClick = { doctor ->
                                Toast.makeText(
                                    this,
                                    "Selected doctor: ${doctor.name}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onBookAppointment = { doctor ->
                                selectedDoctor = doctor
                                currentScreen = Screen.BookAppointment
                            },
                            onClinicInfoClick = {
                                currentScreen = Screen.ClinicInfo
                            }
                        )
                    }
                    Screen.BookAppointment -> {
                        selectedDoctor?.let { doctor ->
                            AppointmentBookingScreen(
                                doctor = doctor,
                                onAppointmentBooked = { appointment ->
                                    currentAppointment = appointment
                                    currentScreen = Screen.Confirmation
                                }
                            )
                        }
                    }
                    Screen.Confirmation -> {
                        currentAppointment?.let { appointment ->
                            AppointmentConfirmationScreen(
                                appointment = appointment,
                                onDone = {
                                    currentAppointment = null
                                    selectedDoctor = null
                                    currentScreen = Screen.DoctorList
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen {
    object DoctorList : Screen()
    object BookAppointment : Screen()
    object Confirmation : Screen()
    object ClinicInfo : Screen()
}