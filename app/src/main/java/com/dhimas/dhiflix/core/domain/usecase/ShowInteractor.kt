package com.dhimas.dhiflix.core.domain.usecase

import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.data.ShowRepository
import com.dhimas.dhiflix.core.domain.repository.ShowDataSource
import kotlinx.coroutines.flow.Flow

class ShowInteractor(private val showRepository: ShowDataSource): ShowUseCase {
    override fun getMovieList(page: Int): Flow<Resource<List<Show>>> {
        return showRepository.getMovieList(page)
    }

    override fun getSeriesList(page: Int): Flow<Resource<List<Show>>> {
        return showRepository.getSeriesList(page)
    }

    override fun getMovieDetail(movie_id: String): Flow<Resource<Show>> {
        return showRepository.getMovieDetail(movie_id)
    }

    override fun getSeriesDetail(series_id: String): Flow<Resource<Show>> {
        return showRepository.getSeriesDetail(series_id)
    }

    override fun getFavoriteMovieList(): Flow<Resource<List<Show>>> {
        return showRepository.getFavoriteMovieList()
    }

    override fun getFavoriteSeriesList(): Flow<Resource<List<Show>>> {
        return showRepository.getFavoriteSeriesList()
    }

    override fun getSimilarMovieList(movie_id: String): Flow<Resource<List<Show>>> {
        return showRepository.getSimilarMovieList(movie_id)
    }

    override fun getSimilarSeriesList(series_id: String): Flow<Resource<List<Show>>> {
        return showRepository.getSimilarSeriesList(series_id)
    }

    override fun searchMovie(keyword: String): Flow<Resource<List<Show>>> {
        return showRepository.searchMovie(keyword)
    }

    override fun searchSeries(keyword: String): Flow<Resource<List<Show>>> {
        return showRepository.searchSeries(keyword)
    }

    override fun setFavorite(show: Show) {
        showRepository.setFavorite(show)
    }
}