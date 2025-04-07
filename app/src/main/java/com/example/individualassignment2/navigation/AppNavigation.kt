package com.example.individualassignment2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.individualassignment2.model.Clinic
import com.example.individualassignment2.model.Doctor.Companion.getDoctorById
import com.example.individualassignment2.ui.screens.AppointmentBookingScreen
import com.example.individualassignment2.ui.screens.ClinicInformationScreen
import com.example.individualassignment2.ui.screens.DoctorListScreen
import com.example.individualassignment2.ui.screens.HomeScreen
import androidx.compose.material3.Text

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ClinicInfo : Screen("clinic_info")
    object DoctorList : Screen("doctor_list")
    object Appointment : Screen("appointment/{doctorId}")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}

@Composable
fun AppNavigation(
    clinic: Clinic,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onGetStarted = {
                    navController.navigate(Screen.DoctorList.route)
                }
            )
        }

        composable(Screen.DoctorList.route) {
            DoctorListScreen(
                clinic = clinic,
                navController = navController,
                onClinicInfoClick = {
                    navController.navigate(Screen.ClinicInfo.route)
                }
                // Remove any additional parameter since DoctorListScreen doesn't need it
            )
        }

        composable(Screen.Appointment.route) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId")
            val doctor = getDoctorById(doctorId ?: "")

            if (doctor.id.isNotEmpty()) {
                AppointmentBookingScreen(
                    doctor = doctor,
                    onAppointmentBooked = { appointment ->
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Doctor not found")
            }
        }

        composable(Screen.ClinicInfo.route) {
            ClinicInformationScreen(
                clinic = clinic,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}