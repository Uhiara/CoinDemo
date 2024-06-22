package com.example.coindemo.presentation.homescreen

import com.example.coindemo.domain.model.CurrencyRate

data class HomeScreenState(
    val fromCurrencyCode: String = "USD",
    val toCurrencyCode: String = "EUR",
    val fromCurrencyValue: String = "0.00",
    val toCurrencyValue: String = "0.00",
    val selection: SelectionState = SelectionState.FROM,
    val currencyRates: Map<String, CurrencyRate> = emptyMap(),
    val error: String? = null
)

enum class SelectionState {
    FROM,
    TO
}