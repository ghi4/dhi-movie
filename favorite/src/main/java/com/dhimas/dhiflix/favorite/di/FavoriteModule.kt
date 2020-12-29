package com.dhimas.dhiflix.favorite.di

import com.dhimas.dhiflix.core.utils.Const
import com.dhimas.dhiflix.favorite.favorite.FavoriteViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val favoriteModule = module {
    scope(named(Const.VIEWMODEL)) {
        viewModel { FavoriteViewModel(get()) }
    }
}
