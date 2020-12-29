package com.dhimas.dhiflix.di

import com.dhimas.dhiflix.core.domain.usecase.ShowInteractor
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase
import com.dhimas.dhiflix.core.utils.Const
import com.dhimas.dhiflix.ui.detail.DetailViewModel
import com.dhimas.dhiflix.ui.movie.MovieViewModel
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.dhimas.dhiflix.ui.series.SeriesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    factory<ShowUseCase> { ShowInteractor(get()) }
}

@ExperimentalCoroutinesApi
@FlowPreview
val viewModelModule = module {
    scope(named(Const.VIEWMODEL)) {
        viewModel { MovieViewModel(get()) }
        viewModel { SeriesViewModel(get()) }
        viewModel { DetailViewModel(get()) }
    }
    viewModel { SearchViewModel(get()) }
}