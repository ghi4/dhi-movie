package com.dhimas.dhiflix.utils

import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.MovieEntity

object DummyData {

    fun generateDummyMovies(): ArrayList<MovieEntity>{
        val movies = ArrayList<MovieEntity>()

        movies.add(
            MovieEntity(
                "2067",
                "2020",
                "57",
                "A lowly utility worker is called to the future by a mysterious radio signal, he must leave his dying wife to embark on a journey that will force him to face his deepest fears in an attempt to change the fabric of reality and save humankind from its greatest environmental crisis yet.",
                R.drawable.poster_2067,
                R.drawable.backdrop_2067
            )
        )

        movies.add(
            MovieEntity(
                "Welcome to Sudden Death",
                "2020",
                "67",
                "Jesse Freeman is a former special forces officer and explosives expert now working a regular job as a security guard in a state-of-the-art basketball arena. Trouble erupts when a tech-savvy cadre of terrorists kidnap the team's owner and Jesse's daughter during opening night. Facing a ticking clock and impossible odds, it's up to Jesse to not only save them but also a full house of fans in this highly charged action thriller.",
                R.drawable.poster_welcome_to_sudden_death,
                R.drawable.backdrop_welcome_to_sudden_death
            )
        )

        movies.add(
            MovieEntity(
                "Hard Kill ",
                "2020",
                "47",
                "The work of billionaire tech CEO Donovan Chalmers is so valuable that he hires mercenaries to protect it, and a terrorist group kidnaps his daughter just to get it.",
                R.drawable.poster_hardkill,
                R.drawable.backdrop_hardkill
            )
        )

        movies.add(
            MovieEntity(
                "Peninsula",
                "2020",
                "71",
                "A soldier and his team battle hordes of post-apocalyptic zombies in the wastelands of the Korean Peninsula.",
                R.drawable.poster_peninsula,
                R.drawable.backdrop_peninsula
            )
        )

        movies.add(
            MovieEntity(
                "Unknown Origins",
                "2020",
                "62",
                "In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims by recreating the first appearance of several comic book superheroes. Cosme, a veteran police inspector who is about to retire, works on the case along with the tormented inspector David Valentín and his own son Jorge Elías, a nerdy young man who owns a comic book store.",
                R.drawable.poster_unknown_origins,
                R.drawable.backdrop_unknown_origins
            )
        )

        movies.add(
            MovieEntity(
                "Artemis Fowl",
                "2020",
                "58",
                "Artemis Fowl is a 12-year-old genius and descendant of a long line of criminal masterminds. He soon finds himself in an epic battle against a race of powerful underground fairies who may be behind his father's disappearance.",
                R.drawable.poster_artemis_fowl,
                R.drawable.backdrop_artemis_fowl
            )
        )

        movies.add(
            MovieEntity(
                "Hubie Halloween",
                "2020",
                "64",
                "Hubie Dubois who, despite his devotion to his hometown of Salem, Massachusetts (and its legendary Halloween celebration), is a figure of mockery for kids and adults alike. But this year, something really is going bump in the night, and it’s up to Hubie to save Halloween.",
                R.drawable.poster_hubie_halloween,
                R.drawable.backdrop_hubie_halloween
            )
        )

        movies.add(
            MovieEntity(
                "One Night in Bangkok",
                "2020",
                "72",
                "A hit man named Kai flies into Bangkok, gets a gun, and orders a cab. He offers a professional female driver big money to be his all-night driver. But when she realizes Kai is committing brutal murders at each stop, it's too late to walk away. Meanwhile, an offbeat police detective races to decode the string of slayings before more blood is spilled.",
                R.drawable.poster_one_night_in_bangkok,
                R.drawable.backdrop_one_night_in_bangkok
            )
        )

        movies.add(
            MovieEntity(
                "Battlefield 2025",
                "2020",
                "57",
                "Weekend campers, an escaped convict, young lovers and a police officer experience a night of terror when a hostile visitor from another world descends on a small Arizona town.",
                R.drawable.poster_battlefield_2025,
                R.drawable.backdrop_battlefield_2025
            )
        )

        movies.add(
            MovieEntity(
                "Sonic the Hedgehog",
                "2020",
                "75",
                "Based on the global blockbuster videogame franchise from Sega, Sonic the Hedgehog tells the story of the world’s speediest hedgehog as he embraces his new home on Earth. In this live-action adventure comedy, Sonic and his new best friend team up to defend the planet from the evil genius Dr. Robotnik and his plans for world domination.",
                R.drawable.poster_sonic_the_hegdog,
                R.drawable.backdrop_sonic_the_hegdog
            )
        )

//        movies.add(
//            MovieEntity(
//                "",
//                "2020",
//                "",
//                "",
//                ,
//
//            )
//        )
//
//        movies.add(
//            MovieEntity(
//                "",
//                "2020",
//                "",
//                "",
//                ,
//
//            )
//        )

        return movies
    }

    fun generateDummySeries(): ArrayList<MovieEntity>{
        val movies = ArrayList<MovieEntity>()

        movies.add(
                MovieEntity(
                        "Drakor",
                        "2020",
                        "57",
                        "A lowly utility worker is called to the future by a mysterious radio signal, he must leave his dying wife to embark on a journey that will force him to face his deepest fears in an attempt to change the fabric of reality and save humankind from its greatest environmental crisis yet.",
                        R.drawable.poster_peninsula,
                        R.drawable.backdrop_hardkill
                )
        )



        return movies
    }
}