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
    scope(named(Const.VIEW_MODEL)) {
        viewModel { MovieViewModel(get()) }
        viewModel { SeriesViewModel(get()) }
        viewModel { DetailViewModel(get()) }
    }

    //Shared viewModel
    //Outside the scope, because unknown problem causing not shared with other
    //Already see stack overflow and github community but no luck
    viewModel { SearchViewModel(get()) }
}