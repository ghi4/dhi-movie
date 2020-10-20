package com.dhimas.dhiflix.ui.movie

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class MovieViewModelTest : Spek({
    describe("A MovieViewModel") {
        val movieViewModel by memoized { MovieViewModel() }

        it("Should not null or empty") {
            assertNotEquals(true, movieViewModel.getMovies().isNullOrEmpty())
        }

        it("Should have 10 item") {
            assertEquals(12, movieViewModel.getMovies().size)
        }

        describe("Movie Entity") {
            val movieEntity by memoized { movieViewModel.getMovies()[0] }

            it("Title should not null or empty") {
                assertNotEquals(true, movieEntity.title.isNullOrEmpty())
            }

            it("ReleaseYear should not null or empty") {
                assertNotEquals(true, movieEntity.releaseYear.isNullOrEmpty())
            }

            it("Overview should not null or empty") {
                assertNotEquals(true, movieEntity.overview.isNullOrEmpty())
            }

            it("PosterPath should not null") {
                assertNotEquals(null, movieEntity.posterPath)
            }

            it("BackdropPath should not null") {
                assertNotEquals(null, movieEntity.backdropPath)
            }
        }
    }
})