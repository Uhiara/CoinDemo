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
import java.text.DecimalFormat
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

            is HomeScreenEvent.BottomSheetItemClicked -> {
                updateCurrencyValue(value = event.value)
            }

            is HomeScreenEvent.NumberButtonClicked -> TODO()
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
        val fromCurrencyRate = state.currencyRates[state.fromCurrencyValue]?.rate ?: 0.0
        val toCurrencyRate = state.currencyRates[state.toCurrencyValue]?.rate ?: 0.0

        val updateCurrencyValue = when (value) {
            "C" -> "0.00"
            else -> if (currentCurrencyValue == "0.00") value else currentCurrencyValue + value
        }

        val numberFormat = DecimalFormat("#.00")

        when (state.selection) {
            SelectionState.FROM -> {
                val fromValue = updateCurrencyValue.toDoubleOrNull() ?: 0.0
                val toValue = fromValue / fromCurrencyRate * toCurrencyRate
                state = state.copy(
                    fromCurrencyValue = updateCurrencyValue,
                    toCurrencyValue = numberFormat.format(toValue)
                )
            }

            SelectionState.TO -> {
                val toValue = updateCurrencyValue.toDoubleOrNull() ?: 0.0
                val fromValue = toValue / toCurrencyRate * fromCurrencyRate
                state = state.copy(
                    fromCurrencyValue = updateCurrencyValue,
                    toCurrencyValue = numberFormat.format(fromValue)
                )
            }
        }
    }
}