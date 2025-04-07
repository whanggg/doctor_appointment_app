package com.example.individualassignment2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.individualassignment2.model.Clinic
import com.example.individualassignment2.model.Doctor
import com.example.individualassignment2.model.Doctor.Companion.getDoctorById
import com.example.individualassignment2.ui.screens.AppointmentBookingScreen
import com.example.individualassignment2.ui.screens.ClinicInformationScreen
import com.example.individualassignment2.ui.screens.DoctorListScreen
import com.example.individualassignment2.ui.screens.HomeScreen
import com.example.individualassignment2.ui.screens.AppointmentConfirmationScreen
import com.example.individualassignment2.model.Appointment
import java.util.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ClinicInfo : Screen("clinic_info")
    object DoctorList : Screen("doctor_list")
    object Appointment : Screen("appointment/{doctorId}")
    object Confirmation : Screen("confirmation")
    
    fun withArgs(vararg args: String): String {
        return buildString {
            var tempRoute = route
            args.forEachIndexed { index, arg ->
                tempRoute = when {
                    tempRoute.contains("{doctorId}") -> tempRoute.replace("{doctorId}", arg)
                    else -> "$tempRoute/$arg"
                }
            }
            append(tempRoute)
        }
    }
}

@Composable
fun AppNavigation(
    clinic: Clinic,
    navController: NavHostController = rememberNavController()
) {
    var currentAppointment by remember { mutableStateOf<Appointment?>(null) }
    
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
            )
        }
        
        composable(
            route = Screen.Appointment.route,
            arguments = listOf(
                navArgument("doctorId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
            val doctor = remember { Doctor.getDoctorById(doctorId) }
            
            AppointmentBookingScreen(
                doctor = doctor,
                onAppointmentBooked = { appointment ->
                    currentAppointment = appointment
                    navController.navigate(Screen.Confirmation.route)
                }
            )
        }
        
        composable(Screen.Confirmation.route) {
            val appointment = currentAppointment
            if (appointment != null) {
                AppointmentConfirmationScreen(
                    appointment = appointment,
                    onDone = {
                        currentAppointment = null
                        navController.popBackStack(Screen.DoctorList.route, false)
                    }
                )
            } else {
                // If no appointment is available, go back to doctor list
                navController.popBackStack(Screen.DoctorList.route, false)
            }
        }
        
        composable(Screen.ClinicInfo.route) {
            ClinicInformationScreen(
                clinic = clinic,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
