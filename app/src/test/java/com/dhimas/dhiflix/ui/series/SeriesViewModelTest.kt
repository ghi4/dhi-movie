package com.dhimas.dhiflix.ui.series

import org.junit.jupiter.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class SeriesViewModelTest: Spek({
    describe("A SeriesViewModel"){
        val seriesViewModel by memoized{SeriesViewModel()}

        it("Should not null or empty"){
            assertNotEquals(true, seriesViewModel.getSeries().isNullOrEmpty())
        }

        it("Should have 12 item"){
            assertEquals(12, seriesViewModel.getSeries().size)
        }

        describe("Series Entity"){
            val seriesEntity by memoized{seriesViewModel.getSeries()[0]}

            it("Title should not null or empty"){
                assertNotEquals(true, seriesEntity.title.isNullOrEmpty())
            }

            it("ReleaseYear should not null or empty"){
                assertNotEquals(true, seriesEntity.releaseYear.isNullOrEmpty())
            }

            it("Overview should not null or empty"){
                assertNotEquals(true, seriesEntity.overview.isNullOrEmpty())
            }

            it("PosterPath should not null"){
                assertNotEquals(null, seriesEntity.posterPath)
            }

            it("BackdropPath should not null"){
                assertNotEquals(null, seriesEntity.backdropPath)
            }
        }
    }
})