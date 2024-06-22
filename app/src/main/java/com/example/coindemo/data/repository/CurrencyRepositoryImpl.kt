package com.example.coindemo.data.repository

import com.example.coindemo.data.local.CurrencyRateDao
import com.example.coindemo.data.local.entity.toCurrencyRate
import com.example.coindemo.data.local.entity.toCurrencyRateEntity
import com.example.coindemo.data.remote.CurrencyApi
import com.example.coindemo.data.remote.dto.toCurrencyRates
import com.example.coindemo.domain.model.CurrencyRate
import com.example.coindemo.domain.model.Resource
import com.example.coindemo.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class CurrencyRepositoryImpl(
    private val api: CurrencyApi,
    private val dao: CurrencyRateDao
) : CurrencyRepository {

    override fun getCurrencyRatesList(): Flow<Resource<List<CurrencyRate>>> = flow {
        val localCurrencyRate = getLocalRateCurrency()
        emit(Resource.Success(localCurrencyRate))

        try {
            val newRates = getRemoteCurrencyRates()
            updateLocalCurrencyRates(newRates)
            emit(Resource.Success(newRates))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check your internet connection", localCurrencyRate))
        } catch (e: IOException) {
            emit(Resource.Error("Oops, something went wrong!", localCurrencyRate))
        }

    }

    private suspend fun getLocalRateCurrency(): List<CurrencyRate> {
        return dao.getAllCurrencyRates().map { it.toCurrencyRate() }
    }

    private suspend fun getRemoteCurrencyRates(): List<CurrencyRate> {
        val response = api.getLatestRates()
        return response.toCurrencyRates()
    }

    private suspend fun updateLocalCurrencyRates(currencyRates: List<CurrencyRate>) {
        dao.upsertAll(currencyRates.map { it.toCurrencyRateEntity() })
    }
}