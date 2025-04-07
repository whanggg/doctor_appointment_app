package com.example.individualassignment2.model

import java.util.UUID

data class Appointment(
    val id: String,
    val doctorId: String,
    val patientName: String,
    val date: String, // Format: yyyy-MM-dd
    val time: String, // Format: HH:mm
    val reason: String,
    val isNewPatient: Boolean,
    val preferredContactMethod: String,
    val contactNumber: String,
    val email: String,
    val symptoms: List<String>,
    val additionalNotes: String = ""
) {
    // Helper function to get formatted date string
    fun getFormattedDate(): String {
        return date
    }

    // Helper function to get formatted time string
    fun getFormattedTime(): String {
        return time
    }
}
