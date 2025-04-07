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
) {
    companion object {
        private val doctors = listOf(
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
                    "Pediatric Immunizations",
                    "Child Nutrition"
                ),
                languagesSpoken = listOf("English", "Mandarin")
            )
        )

        fun getDoctorById(id: String): Doctor {
            return doctors.firstOrNull { it.id == id } ?: Doctor(
                id = "",
                name = "Doctor Not Found",
                specialization = "",
                experience = 0,
                certifications = emptyList(),
                competencies = emptyList(),
                languagesSpoken = emptyList()
            )
        }
    }
}
