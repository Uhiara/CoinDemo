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
            HomeScreenEvent.FromCurrencySelect -> {
                state = state.copy(selection = SelectionState.FROM)
            }

            HomeScreenEvent.ToCurrencySelect -> {
                state = state.copy(selection = SelectionState.TO)
            }

            is HomeScreenEvent.NumberButtonClicked -> {
                updateCurrencyValue(value = event.value)
            }

            is HomeScreenEvent.BottomSheetItemClicked -> {
                if (state.selection == SelectionState.FROM) {
                    state = state.copy(fromCurrencyCode = event.value)
                } else if (state.selection == SelectionState.TO) {
                    state = state.copy(toCurrencyCode = event.value)
                }
                updateCurrencyValue("")
            }
        }
    }

    private fun getCurrencyRateList() {
        viewModelScope.launch {
            repository.getCurrencyRatesList()
                .flowOn(Dispatchers.IO)
                .collectLatest { results ->
                state = when (results) {
                    is Resource.Error -> {
                        state.copy(
                            currencyRates = results.data?.associateBy { it.code } ?: emptyMap(),
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        state.copy(
                            currencyRates = results.data?.associateBy { it.code } ?: emptyMap(),
                            error = results.message
                        )
                    }
                }
            }
        }
    }

    private fun updateCurrencyValue(value: String) {
        val currentCurrencyValue = when (state.selection) {
            SelectionState.FROM -> state.fromCurrencyValue
            SelectionState.TO -> state.toCurrencyValue
        }
        val fromCurrencyRate = state.currencyRates[state.fromCurrencyCode]?.rate ?: 0.0
        val toCurrencyRate = state.currencyRates[state.toCurrencyCode]?.rate ?: 0.0

        val updatedCurrencyValue = when (value) {
            "C" -> "0.00"
            else -> if (currentCurrencyValue == "0.00") value else currentCurrencyValue + value
        }

        val numberFormat = NumberFormat.getNumberInstance()


        when (state.selection) {
            SelectionState.FROM -> {
                val fromValue = updatedCurrencyValue.toDoubleOrNull() ?: 0.0
                val toValue = fromValue / fromCurrencyRate * toCurrencyRate
                state = state.copy(
                    fromCurrencyValue = updatedCurrencyValue,
                    toCurrencyValue = numberFormat.format(toValue)
                )
            }

            SelectionState.TO -> {
                val toValue = updatedCurrencyValue.toDoubleOrNull() ?: 0.0
                val fromValue = toValue / toCurrencyRate * fromCurrencyRate
                state = state.copy(
                    toCurrencyValue = updatedCurrencyValue,
                    fromCurrencyValue = numberFormat.format(fromValue)
                )
            }
        }
    }
}