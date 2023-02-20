package com.kalok.coroutineituneslist

import com.kalok.coroutineituneslist.repositories.ApiDataRepository
import com.kalok.coroutineituneslist.repositories.DatabaseHelper
import com.kalok.coroutineituneslist.repositories.DatabaseHelperImpl
import com.kalok.coroutineituneslist.repositories.RetrofitApiDataRepository
import com.kalok.coroutineituneslist.ui.bookmarks.BookmarksViewModel
import com.kalok.coroutineituneslist.ui.home.HomeViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ApiDataRepository> { RetrofitApiDataRepository() }
    single<DatabaseHelper> { DatabaseHelperImpl(get()) }

    single { CompositeDisposable() }

    viewModel { BookmarksViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
}