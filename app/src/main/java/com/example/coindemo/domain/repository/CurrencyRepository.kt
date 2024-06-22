package com.example.coindemo.domain.repository

import com.example.coindemo.domain.model.CurrencyRate
import com.example.coindemo.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getCurrencyRatesList() : Flow<Resource<List<CurrencyRate>>>
}