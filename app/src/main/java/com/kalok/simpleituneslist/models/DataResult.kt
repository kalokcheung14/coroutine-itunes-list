package com.kalok.simpleituneslist.models

data class DataResult<T>(
    val resultCount: Int,
    val results: ArrayList<T>
)