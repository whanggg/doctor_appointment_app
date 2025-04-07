package com.example.individualassignment2.model

import java.time.LocalDate
import java.time.LocalTime

data class Appointment(
    val id: String,
    val doctorId: String,
    val patientName: String,
    val date: LocalDate,
    val time: LocalTime,
    val reason: String,
    val isNewPatient: Boolean,
    val preferredContactMethod: String,
    val contactNumber: String,
    val symptoms: List<String>,
    val additionalNotes: String = ""
)
