package com.pobochii.someapp.domain.users

/**
 * General class to wrap an operation response
 */
sealed class Result<out T> {
    data class Success<out S>(val data: S) : Result<S>()
    data class Error(val message: String) : Result<Nothing>()
}
