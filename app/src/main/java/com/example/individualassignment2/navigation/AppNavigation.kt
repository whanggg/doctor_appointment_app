package com.example.individualassignment2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.individualassignment2.model.Clinic
import com.example.individualassignment2.ui.screens.ClinicInformationScreen
import com.example.individualassignment2.ui.screens.DoctorListScreen
import com.example.individualassignment2.ui.screens.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ClinicInfo : Screen("clinic_info")
    object DoctorList : Screen("doctor_list")
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
                onDoctorClick = { /* Handle doctor click */ },
                onBookAppointment = { /* Handle appointment booking */ },
                onClinicInfoClick = {
                    navController.navigate(Screen.ClinicInfo.route)
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
