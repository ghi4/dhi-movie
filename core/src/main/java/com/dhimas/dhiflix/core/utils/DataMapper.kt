package com.dhimas.dhiflix.core.utils

import com.dhimas.dhiflix.core.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.core.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.core.data.source.remote.response.SeriesResponse
import com.dhimas.dhiflix.core.domain.model.Show

object DataMapper {

    fun mapDomainToEntity(input: Show) = ShowEntity(
        id = input.id,
        title = input.title,
        releaseDate = input.releaseDate,
        overview = input.overview,
        posterPath = input.posterPath,
        backdropPath = input.backdropPath,
        showType = input.showType,
        isFavorite = input.isFavorite,
        isSimilar = input.isSimilar,
        isSearch = input.isSearch
    )

    fun mapEntityToDomain(input: ShowEntity?) = Show(
        id = input?.id ?: "Unknown",
        title = input?.title ?: "Unknown",
        releaseDate = input?.releaseDate ?: "Unknown",
        overview = input?.overview ?: "Unknown",
        posterPath = input?.posterPath ?: "Unknown",
        backdropPath = input?.backdropPath ?: "Unknown",
        showType = input?.showType ?: 0,
        isFavorite = input?.isFavorite ?: 0,
        isSimilar = input?.isSimilar ?: 0,
        isSearch = input?.isSearch ?: 0
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
                isFavorite = it.isFavorite,
                isSimilar = it.isSimilar,
                isSearch = it.isSearch,
            )
        }

    fun mapMovieResponseToEntity(
        input: MovieResponse?,
        isFavorite: Int? = 0,
        isSimilar: Int? = 0,
        isSearch: Int? = 0
    ) = ShowEntity(
        id = input?.movie_id ?: "Unknown",
        title = input?.title ?: "Unknown",
        releaseDate = input?.releaseDate ?: "Unknown",
        overview = input?.overview ?: "Unknown",
        posterPath = input?.posterPath ?: "Unknown",
        backdropPath = input?.backdropPath ?: "Unknown",
        showType = Const.MOVIE_TYPE,
        isFavorite = isFavorite,
        isSimilar = isSimilar,
        isSearch = isSearch
    )

    fun mapSeriesResponseToEntity(
        input: SeriesResponse?,
        isFavorite: Int? = 0,
        isSimilar: Int? = 0,
        isSearch: Int? = 0
    ) = ShowEntity(
        id = input?.series_id ?: "Unknown",
        title = input?.name ?: "Unknown",
        releaseDate = input?.releaseDate ?: "Unknown",
        overview = input?.overview ?: "Unknown",
        posterPath = input?.posterPath ?: "Unknown",
        backdropPath = input?.backdropPath ?: "Unknown",
        showType = Const.SERIES_TYPE,
        isFavorite = isFavorite,
        isSimilar = isSimilar,
        isSearch = isSearch
    )
}