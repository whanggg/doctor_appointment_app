package com.example.individualassignment2.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.individualassignment2.model.Appointment
import com.example.individualassignment2.model.Doctor
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBookingScreen(
    doctor: Doctor,
    onAppointmentBooked: (Appointment) -> Unit
) {
    var patientName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedTime by remember { mutableStateOf(Pair(9, 0)) } // Store as hour and minute
    var reasonForVisit by remember { mutableStateOf("") }
    var isNewPatient by remember { mutableStateOf(true) }
    var contactNumber by remember { mutableStateOf("") }
    var preferredContactMethod by remember { mutableStateOf("Phone") }
    var selectedSymptoms by remember { mutableStateOf(setOf<String>()) }
    var additionalNotes by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }
    
    val symptoms = remember { listOf("Fever", "Headache", "Cough", "Fatigue", "Pain") }
    val contactMethods = remember { listOf("Phone", "Email", "SMS") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Doctor Information
        Text(
            text = "Booking Appointment with ${doctor.name}",
            style = MaterialTheme.typography.headlineSmall
        )
        
        // Patient Name
        OutlinedTextField(
            value = patientName,
            onValueChange = { patientName = it },
            label = { Text("Patient Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = hasError && patientName.isBlank(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        // Date Picker
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            selectedDate.set(Calendar.YEAR, year)
                            selectedDate.set(Calendar.MONTH, month)
                            selectedDate.set(Calendar.DAY_OF_MONTH, day)
                        },
                        selectedDate.get(Calendar.YEAR),
                        selectedDate.get(Calendar.MONTH),
                        selectedDate.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = "${selectedDate.get(Calendar.YEAR)}-${selectedDate.get(Calendar.MONTH) + 1}-${selectedDate.get(Calendar.DAY_OF_MONTH)}",
                onValueChange = {},
                label = { Text("Select Date") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                enabled = false
            )
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Select Date"
            )
        }

        // Time Picker
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            selectedTime = Pair(hour, minute)
                        },
                        selectedTime.first,
                        selectedTime.second,
                        true
                    ).show()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = String.format("%02d:%02d", selectedTime.first, selectedTime.second),
                onValueChange = {},
                label = { Text("Select Time") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                enabled = false
            )
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Select Time"
            )
        }

        // Reason for Visit
        OutlinedTextField(
            value = reasonForVisit,
            onValueChange = { reasonForVisit = it },
            label = { Text("Reason for Visit") },
            modifier = Modifier.fillMaxWidth(),
            isError = hasError && reasonForVisit.isBlank(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        // New Patient Radio Buttons
        Text(
            text = "Patient Type:",
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RadioButton(
                selected = isNewPatient,
                onClick = { isNewPatient = true }
            )
            Text("New Patient")
            RadioButton(
                selected = !isNewPatient,
                onClick = { isNewPatient = false }
            )
            Text("Existing Patient")
        }

        // Contact Information
        Text(
            text = "Contact Information:",
            style = MaterialTheme.typography.bodyLarge
        )
        
        // Contact Number
        OutlinedTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text("Contact Number") },
            modifier = Modifier.fillMaxWidth(),
            isError = hasError && contactNumber.isBlank(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        // Preferred Contact Method
        Text(
            text = "Preferred Contact Method:",
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            contactMethods.forEach { method ->
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { preferredContactMethod = method },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = preferredContactMethod == method,
                        onClick = { preferredContactMethod = method }
                    )
                    Text(method)
                }
            }
        }

        // Symptoms
        Text(
            text = "Select Symptoms:",
            style = MaterialTheme.typography.bodyLarge
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(symptoms) { symptom ->
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            selectedSymptoms = if (selectedSymptoms.contains(symptom))
                                selectedSymptoms - symptom
                            else
                                selectedSymptoms + symptom
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedSymptoms.contains(symptom),
                        onCheckedChange = null
                    )
                    Text(symptom)
                }
            }
        }

        // Additional Notes
        OutlinedTextField(
            value = additionalNotes,
            onValueChange = { additionalNotes = it },
            label = { Text("Additional Notes") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        // Submit Button
        Button(
            onClick = {
                if (validateForm(patientName, reasonForVisit, contactNumber)) {
                    val appointment = Appointment(
                        id = UUID.randomUUID().toString(),
                        doctorId = doctor.id,
                        patientName = patientName,
                        date = "${selectedDate.get(Calendar.YEAR)}-${selectedDate.get(Calendar.MONTH) + 1}-${selectedDate.get(Calendar.DAY_OF_MONTH)}",
                        time = String.format("%02d:%02d", selectedTime.first, selectedTime.second),
                        reason = reasonForVisit,
                        isNewPatient = isNewPatient,
                        preferredContactMethod = preferredContactMethod,
                        contactNumber = contactNumber,
                        email = email,
                        symptoms = selectedSymptoms.toList(),
                        additionalNotes = additionalNotes
                    )
                    onAppointmentBooked(appointment)
                } else {
                    hasError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Book Appointment")
        }
    }
}

private fun validateForm(
    patientName: String,
    reasonForVisit: String,
    contactNumber: String
): Boolean {
    return patientName.isNotBlank() &&
            reasonForVisit.isNotBlank() &&
            contactNumber.isNotBlank()
}
