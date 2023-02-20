package com.kalok.coroutineituneslist.models

data class DataResult<T>(
    val resultCount: Int,
    val results: ArrayList<T>
)