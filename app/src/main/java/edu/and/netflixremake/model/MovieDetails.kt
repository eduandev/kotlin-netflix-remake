package edu.and.netflixremake.model

data class MovieDetails(

    val movie: Movie,
    val similars: List<Movie>
)
