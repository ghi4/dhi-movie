package com.dhimas.dhiflix.core.utils

import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.core.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.core.data.source.remote.response.SeriesResponse

object DataMapper {

    fun mapDomainToEntity(input: Show) = ShowEntity(
        id = input.id,
        title = input.title,
        releaseDate = input.releaseDate,
        overview = input.overview,
        posterPath = input.posterPath,
        backdropPath = input.backdropPath,
        showType = input.showType,
        page = input.page,
        isFavorite = input.isFavorite,
        isSimilar = input.isSimilar,
        isSearch = input.isSearch
    )

    fun mapEntityToDomain(input: ShowEntity) = Show(
        id = input.id,
        title = input.title,
        releaseDate = input.releaseDate,
        overview = input.overview,
        posterPath = input.posterPath,
        backdropPath = input.backdropPath,
        showType = input.showType,
        page = input.page,
        isFavorite = input.isFavorite,
        isSimilar = input.isSimilar,
        isSearch = input.isSearch
    )

    fun mapEntitiesToDomain(input: List<ShowEntity>): List<Show> =
        input.map {
            Show(
                id = it.id,
                title = it.title,
                releaseDate = it.releaseDate,
                overview = it.overview,
                posterPath = it.posterPath,
                backdropPath = it.backdropPath,
                showType = it.showType,
                page = it.page,
                isFavorite = it.isFavorite,
                isSimilar = it.isSimilar,
                isSearch = it.isSearch,
            )
        }

    fun mapMovieResponseToEntity(input: MovieResponse, page: Int? = 0, isFavorite: Int? = 0, isSimilar: Int? = 0, isSearch: Int? = 0) = ShowEntity(
        id = input.movie_id,
        title = input.title,
        releaseDate = input.releaseDate,
        overview = input.overview,
        posterPath = input.posterPath,
        backdropPath = input.backdropPath,
        showType = Const.MOVIE_TYPE,
        page = page,
        isFavorite = isFavorite,
        isSimilar = isSimilar,
        isSearch = isSearch
    )

    fun mapSeriesResponseToEntity(input: SeriesResponse, page: Int? = 0, isFavorite: Int? = 0, isSimilar: Int? = 0, isSearch: Int? = 0) = ShowEntity(
        id = input.series_id,
        title = input.name,
        releaseDate = input.releaseDate,
        overview = input.overview,
        posterPath = input.posterPath,
        backdropPath = input.backdropPath,
        showType = Const.SERIES_TYPE,
        page = page,
        isFavorite = isFavorite,
        isSimilar = isSimilar,
        isSearch = isSearch
    )
}