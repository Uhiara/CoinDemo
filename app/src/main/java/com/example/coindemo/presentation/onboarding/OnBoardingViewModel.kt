package com.example.coindemo.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coindemo.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isOnboardingComplete = MutableStateFlow(false)
    val isOnboardingComplete: StateFlow<Boolean> = _isOnboardingComplete

    init {
        viewModelScope.launch {
            _isOnboardingComplete.value = userRepository.isOnboardingComplete()
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            userRepository.setOnboardingComplete(true)
            _isOnboardingComplete.value = true
        }
    }
}