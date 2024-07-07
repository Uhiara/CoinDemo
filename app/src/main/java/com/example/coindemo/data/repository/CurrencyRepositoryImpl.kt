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
import kotlinx.coroutines.withContext
import java.io.IOException

class CurrencyRepositoryImpl(
    private val api: CurrencyApi,
    private val dao: CurrencyRateDao
) : CurrencyRepository {

    override suspend fun getCurrencyRatesList(): Flow<Resource<List<CurrencyRate>>> =
        withContext(Dispatchers.IO) {
            flow {
                try {
                    val newRates = getRemoteCurrencyRates()
                    if (newRates is Resource.Success) {
                        val currencyRates = newRates.data?.toCurrencyRates() ?: emptyList()
                        updateLocalCurrencyRates(currencyRates)
                    }
                    emit(Resource.Success(getLocalRateCurrency()))

                } catch (e: IOException) {
                    emit(
                        Resource.Error(
                            "Couldn't reach server, check your internet connection",
                            getLocalRateCurrency()
                        )
                    )
                } catch (e: IOException) {
                    emit(Resource.Error("Oops, something went wrong!", getLocalRateCurrency()))
                }

            }
        }

    private suspend fun getLocalRateCurrency(): List<CurrencyRate> = withContext(Dispatchers.IO) {
        dao.getAllCurrencyRates().map { it.toCurrencyRate() }
    }

    private suspend fun getRemoteCurrencyRates(): Resource<CurrencyDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getLatestRates()
                if (response.isSuccessful) {
                    Resource.Success(response.body())
                } else {
                    when (response.code()) {
                        401, 403, 404, 500 -> Resource.Error("Something went wrong. Please retry.")
                        else -> {
                            Resource.Error(response.message())
                        }
                    }
                }
            } catch (ex: Exception) {
                Resource.Error("Oops, something went wrong!")
            }
        }

    private suspend fun updateLocalCurrencyRates(currencyRates: List<CurrencyRate>) =
        withContext(Dispatchers.IO) {
            dao.upsertAll(currencyRates.map { it.toCurrencyRateEntity() })
        }
}