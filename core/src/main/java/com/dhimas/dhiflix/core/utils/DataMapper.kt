package com.dhimas.dhiflix.core.utils

import com.dhimas.dhiflix.core.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.core.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.core.data.source.remote.response.SeriesResponse
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.presenter.model.ShowsDetailModel
import com.dhimas.dhiflix.core.presenter.model.ShowsModel
import com.dhimas.dhiflix.core.presenter.model.ShowsPosterModel

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
        isSearch = input?.isSearch ?: 0
    )

    fun mapMovieResponseToEntity(
        input: MovieResponse?,
        isFavorite: Int? = 0,
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
        isSearch = isSearch
    )

    fun mapSeriesResponseToEntity(
        input: SeriesResponse?,
        isFavorite: Int? = 0,
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
        isSearch = isSearch
    )

    fun mapMovieResponseToDomain(
        input: MovieResponse?,
        isFavorite: Int? = 0,
        isSearch: Int? = 0
    ) = Show(
        id = input?.movie_id ?: "Unknown",
        title = input?.title ?: "Unknown",
        releaseDate = input?.releaseDate ?: "Unknown",
        overview = input?.overview ?: "Unknown",
        posterPath = input?.posterPath ?: "Unknown",
        backdropPath = input?.backdropPath ?: "Unknown",
        showType = Const.MOVIE_TYPE,
        isFavorite = isFavorite ?: 0,
        isSearch = isSearch ?: 0
    )

    fun mapSeriesResponseToDomain(
        input: SeriesResponse?,
        isFavorite: Int? = 0,
        isSearch: Int? = 0
    ) = Show(
        id = input?.series_id ?: "Unknown",
        title = input?.name ?: "Unknown",
        releaseDate = input?.releaseDate ?: "Unknown",
        overview = input?.overview ?: "Unknown",
        posterPath = input?.posterPath ?: "Unknown",
        backdropPath = input?.backdropPath ?: "Unknown",
        showType = Const.SERIES_TYPE,
        isFavorite = isFavorite ?: 0,
        isSearch = isSearch ?: 0
    )

    fun mapDomainToShows(show: Show): ShowsModel = ShowsModel(
        id = show.id,
        title = show.title,
        releaseDate = show.releaseDate,
        posterPath = show.posterPath,
        backdropPath = show.backdropPath,
        showType = show.showType
    )

    fun mapDomainToShowsDetail(show: Show): ShowsDetailModel = ShowsDetailModel(
        title = show.title,
        releaseDate = show.releaseDate,
        overview = show.overview,
        posterPath = show.posterPath,
        backdropPath = show.backdropPath,
        isFavorite = show.isFavorite
    )

    fun mapDomainToShowsPoster(show: Show): ShowsPosterModel = ShowsPosterModel(
        id = show.id,
        posterPath = show.posterPath,
        showType = show.showType
    )


    fun mapListEntityToDomain(input: List<ShowEntity>): List<Show> =
        input.map {
            mapEntityToDomain(it)
        }

    fun mapListDomainToArrayShowsModel(input: List<Show>): ArrayList<ShowsModel> {
        val result = ArrayList<ShowsModel>()

        input.map {
            result.add(mapDomainToShows(it))
        }

        return result
    }
}