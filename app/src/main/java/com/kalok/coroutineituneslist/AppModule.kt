package com.kalok.coroutineituneslist

import com.kalok.coroutineituneslist.repositories.ApiDataRepository
import com.kalok.coroutineituneslist.repositories.DatabaseHelper
import com.kalok.coroutineituneslist.repositories.DatabaseHelperImpl
import com.kalok.coroutineituneslist.repositories.RetrofitApiDataRepository
import com.kalok.coroutineituneslist.viewmodels.BookmarksViewModel
import com.kalok.coroutineituneslist.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ApiDataRepository> { RetrofitApiDataRepository() }
    single<DatabaseHelper> { DatabaseHelperImpl(get()) }

    viewModel { BookmarksViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
}