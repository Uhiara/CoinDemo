package com.example.coindemo.domain.model

sealed class Resource<T> {
    class Loading<T> : Resource<T>() // Added Loading state for better state management
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error<T>(val message: String?, val data: T? = null) : Resource<T>()
}