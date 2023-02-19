package com.kalok.simpleituneslist

import com.kalok.simpleituneslist.repositories.ApiDataRepository
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import com.kalok.simpleituneslist.repositories.DatabaseHelperImpl
import com.kalok.simpleituneslist.repositories.RetrofitApiDataRepository
import com.kalok.simpleituneslist.ui.bookmarks.BookmarksViewModel
import com.kalok.simpleituneslist.ui.home.HomeViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ApiDataRepository> { RetrofitApiDataRepository() }
    single<DatabaseHelper> { DatabaseHelperImpl(get()) }
    single { CompositeDisposable() }

    viewModel { BookmarksViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
}