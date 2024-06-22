package com.example.coindemo.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coindemo.presentation.homescreen.HomeScreen
import com.example.coindemo.presentation.homescreen.HomeScreenState
import com.example.coindemo.presentation.homescreen.HomeScreenViewModel
import com.example.coindemo.presentation.onboarding.OnBoardingScreen
import com.example.coindemo.presentation.onboarding.SplashScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Splash Screen") {
        composable("Splash Screen") {
            SplashScreen(navController)
        }
        composable("OnBoarding Screen") {
            OnBoardingScreen(navController)
        }
        composable("Home Screen") {
            val viewModel: HomeScreenViewModel = hiltViewModel()
            HomeScreen(state = viewModel.state, onEvent = viewModel::onEvent)
        }
    }
}