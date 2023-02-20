package com.kalok.coroutineituneslist.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

// Extension function to setup recycler view
fun RecyclerView.setup() {
    setHasFixedSize(true)
    minimumHeight = 90
    // Disable item change default animation
    (itemAnimator as SimpleItemAnimator).apply {
        supportsChangeAnimations = false
    }
}

// Retry policy
private const val RETRY_TIME_IN_MILLIS = 15000L
private const val RETRY_ATTEMPT_COUNT = 3

// Customise Result handling
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this.map<T, Result<T>> {
        Result.Success(it)
    }.retryWhen { cause, attempt ->
        // Retry logic
        if (cause is IOException && attempt < RETRY_ATTEMPT_COUNT) {
            delay(RETRY_TIME_IN_MILLIS)
            true
        } else {
            false
        }
    }.catch {
        emit(Result.Error(it))
    }
}