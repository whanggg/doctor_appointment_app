package com.example.individualassignment2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.individualassignment2.model.Doctor
import com.example.individualassignment2.ui.components.DoctorProfileCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(
    onDoctorClick: (Doctor) -> Unit,
    onBookAppointment: (Doctor) -> Unit,
    onClinicInfoClick: () -> Unit
) {
    // Sample data - In a real app, this would come from a ViewModel
    val doctors = remember {
        listOf(
            Doctor(
                id = "1",
                name = "Dr. Sarah Chen",
                specialization = "Cardiologist",
                experience = 15,
                certifications = listOf(
                    "Board Certified in Cardiovascular Disease",
                    "Fellow of the American College of Cardiology"
                ),
                competencies = listOf(
                    "Heart Disease",
                    "Cardiac Rehabilitation",
                    "Preventive Cardiology"
                ),
                languagesSpoken = listOf("English", "Mandarin", "Malay")
            ),
            Doctor(
                id = "2",
                name = "Dr. James Wilson",
                specialization = "Pediatrician",
                experience = 12,
                certifications = listOf(
                    "Board Certified in Pediatrics",
                    "Member of American Academy of Pediatrics"
                ),
                competencies = listOf(
                    "Child Development",
                    "Preventive Care",
                    "Pediatric Emergency"
                ),
                languagesSpoken = listOf("English", "Spanish")
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Our Doctors") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = onClinicInfoClick) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Clinic Information"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(doctors) { doctor ->
                DoctorProfileCard(
                    doctor = doctor,
                    onClick = { onDoctorClick(doctor) },
                    onBookAppointment = { onBookAppointment(doctor) }
                )
            }
        }
    }
}
