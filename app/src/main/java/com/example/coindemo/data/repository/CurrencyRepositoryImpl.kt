package com.example.coindemo.data.repository

import com.example.coindemo.data.local.CurrencyRateDao
import com.example.coindemo.data.local.entity.toCurrencyRate
import com.example.coindemo.data.local.entity.toCurrencyRateEntity
import com.example.coindemo.data.remote.CurrencyApi
import com.example.coindemo.data.remote.dto.CurrencyDto
import com.example.coindemo.data.remote.dto.toCurrencyRates
import com.example.coindemo.domain.model.CurrencyRate
import com.example.coindemo.domain.model.Resource
import com.example.coindemo.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class CurrencyRepositoryImpl(private val api: CurrencyApi,
                             private val dao: CurrencyRateDao
) : CurrencyRepository {
    override suspend fun getCurrencyRatesList(): Flow<Resource<List<CurrencyRate>>> = flow {
        emit(Resource.Loading()) // Added Loading state for better UI feedback
        try {
            when (val newRates = getRemoteCurrencyRates()) {
                is Resource.Success -> {
                    val currencyRates = newRates.data?.toCurrencyRates() ?: emptyList()
                    updateLocalCurrencyRates(currencyRates)
                    emit(Resource.Success(currencyRates))
                }
                is Resource.Error -> emit(Resource.Error(newRates.message, getLocalRateCurrency()))
                else -> emit(Resource.Success(getLocalRateCurrency())) // Handle unexpected cases
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.message}", getLocalRateCurrency())) // Consolidated exception handling
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getLocalRateCurrency(): List<CurrencyRate> = dao.getAllCurrencyRates().map { it.toCurrencyRate() }

    private suspend fun getRemoteCurrencyRates(): Resource<CurrencyDto> = try {
        val response = api.getLatestRates()
        if (response.isSuccessful) Resource.Success(response.body())
        else Resource.Error("API error: ${response.code()} - ${response.message()}") // Simplified error handling
    } catch (e: Exception) {
        Resource.Error("Request failed: ${e.message}")
    }

    private suspend fun updateLocalCurrencyRates(currencyRates: List<CurrencyRate>) =
        dao.upsertAll(currencyRates.map { it.toCurrencyRateEntity() })
}