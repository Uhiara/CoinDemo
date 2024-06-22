package com.example.coindemo.presentation.homescreen

sealed class HomeScreenEvent {
    object FromCurrencySelect: HomeScreenEvent()
    object ToCurrencySelect: HomeScreenEvent()
    data class BottomSheetItemClicked(val value: String): HomeScreenEvent()
    data class NumberButtonClicked(val value: String): HomeScreenEvent()
}