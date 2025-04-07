package com.example.individualassignment2.model

data class Doctor(
    val id: String,
    val name: String,
    val specialization: String,
    val experience: Int, // years of experience
    val certifications: List<String>,
    val competencies: List<String>,
    val languagesSpoken: List<String>,
    val imageUrl: String? = null
)
