package com.example.individualassignment2.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.individualassignment2.model.Appointment
import com.example.individualassignment2.model.Doctor
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBookingScreen(
    doctor: Doctor,
    onAppointmentBooked: (Appointment) -> Unit
) {
    var patientName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var reason by remember { mutableStateOf("") }
    var isNewPatient by remember { mutableStateOf(true) }
    var contactNumber by remember { mutableStateOf("") }
    var preferredContact by remember { mutableStateOf("Phone") }
    var selectedSymptoms by remember { mutableStateOf(setOf<String>()) }
    var additionalNotes by remember { mutableStateOf("") }
    
    val symptoms = remember { listOf("Fever", "Headache", "Cough", "Fatigue", "Pain") }
    val contactMethods = remember { listOf("Phone", "Email", "SMS") }
    
    val context = LocalContext.current
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Book Appointment with ${doctor.name}",
            style = MaterialTheme.typography.headlineSmall
        )

        // Patient Name
        OutlinedTextField(
            value = patientName,
            onValueChange = { patientName = it },
            label = { Text("Patient Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = hasError && patientName.isBlank()
        )

        // Date Picker
        Button(
            onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        selectedDate = LocalDate.of(year, month + 1, day)
                    },
                    selectedDate.year,
                    selectedDate.monthValue - 1,
                    selectedDate.dayOfMonth
                ).show()
            }
        ) {
            Text("Select Date: ${selectedDate}")
        }

        // Time Picker
        Button(
            onClick = {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        selectedTime = LocalTime.of(hour, minute)
                    },
                    selectedTime.hour,
                    selectedTime.minute,
                    true
                ).show()
            }
        ) {
            Text("Select Time: ${selectedTime}")
        }

        // Reason for Visit
        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            label = { Text("Reason for Visit") },
            modifier = Modifier.fillMaxWidth(),
            isError = hasError && reason.isBlank()
        )

        // New Patient Radio Buttons
        Column(Modifier.selectableGroup()) {
            Text("Are you a new patient?", style = MaterialTheme.typography.bodyLarge)
            Row {
                RadioButton(
                    selected = isNewPatient,
                    onClick = { isNewPatient = true }
                )
                Text("Yes", Modifier.padding(start = 8.dp))
                Spacer(Modifier.width(16.dp))
                RadioButton(
                    selected = !isNewPatient,
                    onClick = { isNewPatient = false }
                )
                Text("No", Modifier.padding(start = 8.dp))
            }
        }

        // Contact Number
        OutlinedTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text("Contact Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            isError = hasError && contactNumber.isBlank()
        )

        // Preferred Contact Method
        Text("Preferred Contact Method:", style = MaterialTheme.typography.bodyLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            contactMethods.forEach { method ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = preferredContact == method,
                        onClick = { preferredContact = method }
                    )
                    Text(method)
                }
            }
        }

        // Symptoms Checkboxes
        Text("Symptoms:", style = MaterialTheme.typography.bodyLarge)
        symptoms.forEach { symptom ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedSymptoms.contains(symptom),
                    onCheckedChange = { checked ->
                        selectedSymptoms = if (checked) {
                            selectedSymptoms + symptom
                        } else {
                            selectedSymptoms - symptom
                        }
                    }
                )
                Text(symptom)
            }
        }

        // Additional Notes
        OutlinedTextField(
            value = additionalNotes,
            onValueChange = { additionalNotes = it },
            label = { Text("Additional Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        if (hasError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Submit Button
        Button(
            onClick = {
                if (validateForm(patientName, reason, contactNumber)) {
                    val appointment = Appointment(
                        id = UUID.randomUUID().toString(),
                        doctorId = doctor.id,
                        patientName = patientName,
                        date = selectedDate,
                        time = selectedTime,
                        reason = reason,
                        isNewPatient = isNewPatient,
                        preferredContactMethod = preferredContact,
                        contactNumber = contactNumber,
                        symptoms = selectedSymptoms.toList(),
                        additionalNotes = additionalNotes
                    )
                    onAppointmentBooked(appointment)
                } else {
                    hasError = true
                    errorMessage = "Please fill in all required fields"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Book Appointment")
        }
    }
}

private fun validateForm(
    patientName: String,
    reason: String,
    contactNumber: String
): Boolean {
    return patientName.isNotBlank() &&
            reason.isNotBlank() &&
            contactNumber.isNotBlank()
}
