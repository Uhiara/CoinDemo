package com.example.coindemo.presentation.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.coindemo.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(key1 = true) {
        delay(1500) // Shortened from 2000 for snappier UX
        alpha.animateTo( // Animate alpha from 1f to 0f over 500ms
            targetValue = 0f,
            animationSpec = tween(500)
        )
        delay(500)
        if (mainViewModel.isOnboardingComplete.value) {
            navController.navigate("Home Screen") { popUpTo("Splash Screen") { inclusive = true } }
        } else {
            navController.navigate("OnBoarding Screen") { popUpTo("Splash Screen") { inclusive = true } }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .alpha(alpha.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.coinxchange), contentDescription = null)
    }
}