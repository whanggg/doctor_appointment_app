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
import com.example.individualassignment2.ui.screens.AppointmentConfirmationScreen
import com.example.individualassignment2.model.Appointment
import java.util.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ClinicInfo : Screen("clinic_info")
    object DoctorList : Screen("doctor_list")
    object Appointment : Screen("appointment/{doctorId}")
    object Confirmation : Screen("confirmation/{appointmentId}/{patientName}/{date}/{time}/{reason}/{isNewPatient}/{contactNumber}/{email}/{preferredContactMethod}/{symptoms}/{additionalNotes}")
    
    fun withArgs(vararg args: String): String {
        return buildString {
            var tempRoute = route
            args.forEachIndexed { index, arg ->
                tempRoute = when {
                    tempRoute.contains("{doctorId}") -> tempRoute.replace("{doctorId}", arg)
                    tempRoute.contains("{appointmentId}") -> tempRoute.replace("{appointmentId}", arg)
                    tempRoute.contains("{patientName}") -> tempRoute.replace("{patientName}", arg)
                    tempRoute.contains("{date}") -> tempRoute.replace("{date}", arg)
                    tempRoute.contains("{time}") -> tempRoute.replace("{time}", arg)
                    tempRoute.contains("{reason}") -> tempRoute.replace("{reason}", arg)
                    tempRoute.contains("{isNewPatient}") -> tempRoute.replace("{isNewPatient}", arg)
                    tempRoute.contains("{contactNumber}") -> tempRoute.replace("{contactNumber}", arg)
                    tempRoute.contains("{email}") -> tempRoute.replace("{email}", arg)
                    tempRoute.contains("{preferredContactMethod}") -> tempRoute.replace("{preferredContactMethod}", arg)
                    tempRoute.contains("{symptoms}") -> tempRoute.replace("{symptoms}", arg)
                    tempRoute.contains("{additionalNotes}") -> tempRoute.replace("{additionalNotes}", arg)
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
                    // Navigate to confirmation screen with all appointment details
                    navController.navigate(
                        Screen.Confirmation.withArgs(
                            appointment.id,
                            appointment.patientName,
                            appointment.date,
                            appointment.time,
                            appointment.reason,
                            appointment.isNewPatient.toString(),
                            appointment.contactNumber,
                            appointment.email,
                            appointment.preferredContactMethod,
                            appointment.symptoms.joinToString(","),
                            appointment.additionalNotes
                        )
                    )
                }
            )
        }
        
        composable(
            route = Screen.Confirmation.route,
            arguments = listOf(
                navArgument("appointmentId") { type = NavType.StringType },
                navArgument("patientName") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType },
                navArgument("reason") { type = NavType.StringType },
                navArgument("isNewPatient") { type = NavType.StringType },
                navArgument("contactNumber") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("preferredContactMethod") { type = NavType.StringType },
                navArgument("symptoms") { type = NavType.StringType },
                navArgument("additionalNotes") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val appointment = Appointment(
                id = backStackEntry.arguments?.getString("appointmentId") ?: "",
                doctorId = "",
                patientName = backStackEntry.arguments?.getString("patientName") ?: "",
                date = backStackEntry.arguments?.getString("date") ?: "",
                time = backStackEntry.arguments?.getString("time") ?: "",
                reason = backStackEntry.arguments?.getString("reason") ?: "",
                isNewPatient = backStackEntry.arguments?.getString("isNewPatient")?.toBoolean() ?: true,
                contactNumber = backStackEntry.arguments?.getString("contactNumber") ?: "",
                email = backStackEntry.arguments?.getString("email") ?: "",
                preferredContactMethod = backStackEntry.arguments?.getString("preferredContactMethod") ?: "",
                symptoms = backStackEntry.arguments?.getString("symptoms")?.split(",") ?: listOf(),
                additionalNotes = backStackEntry.arguments?.getString("additionalNotes") ?: ""
            )
            
            AppointmentConfirmationScreen(
                appointment = appointment,
                onDone = {
                    navController.popBackStack(Screen.DoctorList.route, false)
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
