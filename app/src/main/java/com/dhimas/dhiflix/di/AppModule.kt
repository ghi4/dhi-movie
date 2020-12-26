package com.dhimas.dhiflix.di

import com.dhimas.dhiflix.core.domain.usecase.ShowInteractor
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase
import com.dhimas.dhiflix.ui.detail.DetailViewModel
import com.dhimas.dhiflix.ui.favorite.FavoriteViewModel
import com.dhimas.dhiflix.ui.movie.MovieViewModel
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.dhimas.dhiflix.ui.series.SeriesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<ShowUseCase> { ShowInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MovieViewModel(get()) }
    viewModel { SeriesViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}