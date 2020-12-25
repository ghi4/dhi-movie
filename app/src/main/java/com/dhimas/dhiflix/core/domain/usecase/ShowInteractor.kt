package com.dhimas.dhiflix.core.domain.usecase

import androidx.lifecycle.LiveData
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.data.ShowRepository

class ShowInteractor(private val showRepository: ShowRepository): ShowUseCase {
    override fun getMovieList(page: Int): LiveData<Resource<List<Show>>> {
        return showRepository.getMovieList(page)
    }

    override fun getSeriesList(page: Int): LiveData<Resource<List<Show>>> {
        return showRepository.getSeriesList(page)
    }

    override fun getMovieDetail(movie_id: String): LiveData<Resource<Show>> {
        return showRepository.getMovieDetail(movie_id)
    }

    override fun getSeriesDetail(series_id: String): LiveData<Resource<Show>> {
        return showRepository.getSeriesDetail(series_id)
    }

    override fun getFavoriteMovieList(): LiveData<Resource<List<Show>>> {
        return showRepository.getFavoriteMovieList()
    }

    override fun getFavoriteSeriesList(): LiveData<Resource<List<Show>>> {
        return showRepository.getFavoriteSeriesList()
    }

    override fun getSimilarMovieList(movie_id: String): LiveData<Resource<List<Show>>> {
        return showRepository.getSimilarMovieList(movie_id)
    }

    override fun getSimilarSeriesList(series_id: String): LiveData<Resource<List<Show>>> {
        return showRepository.getSimilarSeriesList(series_id)
    }

    override fun searchMovie(keyword: String): LiveData<Resource<List<Show>>> {
        return showRepository.searchMovie(keyword)
    }

    override fun searchSeries(keyword: String): LiveData<Resource<List<Show>>> {
        return showRepository.searchSeries(keyword)
    }

    override fun setFavorite(show: Show) {
        showRepository.setFavorite(show)
    }
}