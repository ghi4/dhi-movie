package com.dhimas.dhiflix.ui.detail

import com.dhimas.dhiflix.utils.DummyData
import org.junit.jupiter.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class DetailViewModelTest: Spek({
    describe("A DetailViewModel"){
        val detailViewModel by memoized{DetailViewModel()}

        describe("Get movie entity by title"){
            val dummyMovie = DummyData.generateDummyMovies()
            val movieEntity by memoized{detailViewModel.getShowEntityByTitle(dummyMovie[5].title!!)}

            it("Should have same title"){
                assertEquals(dummyMovie[5].title, movieEntity.title)
            }

            it("Should have same releaseYear"){
                assertEquals(dummyMovie[5].releaseYear, movieEntity.releaseYear)
            }

            it("Should have same overview"){
                assertEquals(dummyMovie[5].overview, movieEntity.overview)
            }

            it("Should have same posterPath"){
                assertEquals(dummyMovie[5].posterPath, movieEntity.posterPath)
            }

            it("Should have same backdropPath"){
                assertEquals(dummyMovie[5].backdropPath, movieEntity.backdropPath)
            }
        }

        describe("List movie shouldn't have same value with input"){
            val dummyMovie = DummyData.generateDummyMovies()
            val movieList by memoized{detailViewModel.getMovieButExclude(dummyMovie[5])}

            it("Shouldn't null or empty"){
                assertNotEquals(true, movieList.isNullOrEmpty())
            }

            it("Should have ${dummyMovie.size - 1} array size"){
                assertEquals(dummyMovie.size - 1, movieList.size)
            }

            it("Should not have same value with input movie"){
                assertNotEquals(true, movieList.contains(dummyMovie[5]))
            }
        }

        describe("List series shouldn't have same value with input"){
            val dummySeries = DummyData.generateDummySeries()
            val seriesList by memoized{detailViewModel.getSeriesButExclude(dummySeries[5])}

            it("Shouldn't null or empty"){
                assertNotEquals(true, seriesList.isNullOrEmpty())
            }

            it("Should have ${dummySeries.size - 1} array size"){
                assertEquals(dummySeries.size - 1, seriesList.size)
            }

            it("Should not have same value with input movie"){
                assertNotEquals(true, seriesList.contains(dummySeries[5]))
            }
        }


    }
})