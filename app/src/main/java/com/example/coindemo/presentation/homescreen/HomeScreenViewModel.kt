package com.example.coindemo.presentation.homescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coindemo.domain.model.Resource
import com.example.coindemo.domain.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {
    var state by mutableStateOf(HomeScreenState())

    init {
        getCurrencyRateList()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.FromCurrencySelect -> state = state.copy(selection = SelectionState.FROM)
            HomeScreenEvent.ToCurrencySelect -> state = state.copy(selection = SelectionState.TO)
            is HomeScreenEvent.NumberButtonClicked -> updateCurrencyValue(event.value)
            is HomeScreenEvent.BottomSheetItemClicked -> {
                state = if (state.selection == SelectionState.FROM) {
                    state.copy(
                        fromCurrencyCode = event.value,
                        fromCurrencyValue = "0.00", // Reset to "0.00" after currency change
                        toCurrencyValue = "0.00"   // Reset to "0.00" after currency change
                    )
                } else {
                    state.copy(
                        toCurrencyCode = event.value,
                        fromCurrencyValue = "0.00", // Reset to "0.00" after currency change
                        toCurrencyValue = "0.00"   // Reset to "0.00" after currency change
                    )
                }
            }
        }
    }

    private fun getCurrencyRateList() {
        viewModelScope.launch {
            repository.getCurrencyRatesList()
                .flowOn(Dispatchers.IO)
                .collectLatest { results ->
                    state = when (results) {
                        is Resource.Error -> state.copy(
                            currencyRates = results.data?.associateBy { it.code } ?: emptyMap(),
                            error = results.message
                        )
                        is Resource.Success -> state.copy(
                            currencyRates = results.data?.associateBy { it.code } ?: emptyMap(),
                            error = null // Clear error on success
                        )
                        is Resource.Loading -> state // Added support for Loading state, though not fully utilized here
                    }
                }
        }
    }

    private fun updateCurrencyValue(value: String) {
        val currentValue = when (state.selection) {
            SelectionState.FROM -> state.fromCurrencyValue
            SelectionState.TO -> state.toCurrencyValue
        }
        val fromRate = state.currencyRates[state.fromCurrencyCode]?.rate ?: 1.0
        val toRate = state.currencyRates[state.toCurrencyCode]?.rate ?: 1.0
        val numberFormat = NumberFormat.getNumberInstance().apply { maximumFractionDigits = 2 }

        val updatedValue = when (value) {
            "C" -> "0.00" // Reset to "0.00" explicitly
            "." -> if (currentValue.contains(".")) currentValue else "$currentValue."
            else -> {
                val newValue = if (currentValue == "0.00") value else currentValue + value
                if (newValue.count { it == '.' } > 1 || newValue.length > 10) currentValue else newValue
            }
        }

        when (state.selection) {
            SelectionState.FROM -> {
                val fromValue = updatedValue.toDoubleOrNull() ?: 0.0
                val toValue = fromValue / fromRate * toRate
                state = state.copy(
                    fromCurrencyValue = updatedValue,
                    toCurrencyValue = if (updatedValue == "0.00") "0.00" else numberFormat.format(toValue) // Ensure "0.00" on reset
                )
            }
            SelectionState.TO -> {
                val toValue = updatedValue.toDoubleOrNull() ?: 0.0
                val fromValue = toValue / toRate * fromRate
                state = state.copy(
                    toCurrencyValue = updatedValue,
                    fromCurrencyValue = if (updatedValue == "0.00") "0.00" else numberFormat.format(fromValue) // Ensure "0.00" on reset
                )
            }
        }
    }
}