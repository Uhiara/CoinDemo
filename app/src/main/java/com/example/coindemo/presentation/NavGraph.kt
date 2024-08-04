package com.example.coindemo.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coindemo.presentation.homescreen.HomeScreen
import com.example.coindemo.presentation.homescreen.HomeScreenViewModel
import com.example.coindemo.presentation.onboarding.MainViewModel
import com.example.coindemo.presentation.onboarding.OnBoardingScreen
import com.example.coindemo.presentation.onboarding.SplashScreen

@Composable
fun NavGraph(mainViewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val isOnboardingComplete by mainViewModel.isOnboardingComplete.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isOnboardingComplete) "Home Screen" else "Splash Screen"
    ) {
        composable("Splash Screen") {
            SplashScreen(navController, mainViewModel)
        }
        composable("OnBoarding Screen") {
            OnBoardingScreen(navController, mainViewModel)
        }
        composable("Home Screen") {
            HomeScreen(state = viewModel.state, onEvent = viewModel::onEvent)
        }
    }
}