package com.example.coindemo.data.remote

import com.example.coindemo.data.remote.dto.CurrencyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET ("v1/latest")
    suspend fun getLatestRates(
        @Query("apikey") apiKey: String = API_KEY
    ) : Response<CurrencyDto>

    companion object {
        const val API_KEY = "fca_live_gI0OzNNtWI5OzDTQDodWLYNphMfzIIqvXfSHFrSS"
        const val BASE_URL = "https://api.freecurrencyapi.com/"
    }
}