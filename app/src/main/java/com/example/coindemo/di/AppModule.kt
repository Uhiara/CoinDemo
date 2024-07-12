package com.example.coindemo.di

import android.app.Application
import androidx.room.Room
import com.example.coindemo.data.local.CurrencyRateDatabase
import com.example.coindemo.data.remote.CurrencyApi
import com.example.coindemo.data.repository.CurrencyRepositoryImpl
import com.example.coindemo.domain.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(CurrencyApi.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
        return retrofit.create(CurrencyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDataBase(application: Application): CurrencyRateDatabase {
        return Room
            .databaseBuilder(
                application,
                CurrencyRateDatabase::class.java,
                "currency_db"
            ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(
        api: CurrencyApi,
        db: CurrencyRateDatabase
    ): CurrencyRepository {
        return CurrencyRepositoryImpl (
            api = api,
            dao = db.currencyRateDao
        )
    }
}