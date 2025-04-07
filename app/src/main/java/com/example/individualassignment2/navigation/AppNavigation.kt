package com.example.individualassignment2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ClinicInfo : Screen("clinic_info")
    object DoctorList : Screen("doctor_list")
    object Appointment : Screen("appointment/{doctorId}")
    
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
                    // Handle appointment booking and navigate back
                    navController.popBackStack()
                }
            )
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
