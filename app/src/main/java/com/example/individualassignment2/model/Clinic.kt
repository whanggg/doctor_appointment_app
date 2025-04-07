package com.example.individualassignment2.model

data class Clinic(
    val name: String,
    val address: String,
    val phone: String,
    val email: String,
    val website: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val operatingHours: Map<String, String>,
    val facilities: List<String>
)
