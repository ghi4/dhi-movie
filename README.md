# dhi-movie
![Platform](https://img.shields.io/badge/platform-Android-brightgreen)
[![ghi4](https://circleci.com/gh/ghi4/dhi-movie.svg?style=shield)](https://circleci.com/gh/ghi4/dhi-movie)


This project is about a movie catalog that I done on my final submission from [Dicoding](https://www.dicoding.com/) in class [Menjadi Android Developer Expert](https://www.dicoding.com/academies/165) which is the last class from [Android Developer Learning Path](https://www.dicoding.com/learningpaths/7). The data used in this app is from [TheMovieDB](https://developers.themoviedb.org/3). I'm so happy because I received 5 stars on this submission. I hope this project can be useful for you as a learning reference.
<p align="center">
  <img src="screenshot/dicoding_made_mystar.png"
       width="600"/>
</p>

## Features
- MVVM + Clean Architecture
- Modularization
  - Module [:core](https://github.com/ghi4/dhi-movie/tree/master/core) as library module
  - Module [:favorite](https://github.com/ghi4/dhi-movie/tree/master/favorite) as dynamic-feature module
- Koin for dependency injection
- Kotlin Coroutines for handling data flow
- Shimmer for loading animation
- Leak Canary for memory leak detection

## Security
- SQLCipher for local database encryption
- Obfuscation
- Certificate Pinning

## Screenshots
<h3 align="center"> Movie List and Series List </h3>
<p align="center">
  <img src="screenshot/movieList.jpg"
       width="210"/>
  <img src="screenshot/seriesList.jpg"
       width="210"/>
</p>

<h3 align="center"> Search Page </h3>
<p align="center">
  <img src="screenshot/search_first.jpg"
       width="210"/>
  <img src="screenshot/search_movie_thor.jpg"
       width="210"/>
  <img src="screenshot/search_empty.jpg"
       width="210"/>
</p>

<h3 align="center"> Favorite Page </h3>
<p align="center">
  <img src="screenshot/favorite_empty.jpg"
       width="210"/>
  <img src="screenshot/favorite_fill.jpg"
       width="210"/>
</p>

<h3 align="center"> Detail Page </h3>
<p align="center">
  <img src="screenshot/detailPage.jpg"
       width="210"/>
</p>
