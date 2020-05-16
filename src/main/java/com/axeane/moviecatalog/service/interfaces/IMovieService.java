package com.axeane.moviecatalog.service.interfaces;

import com.axeane.moviecatalog.service.dto.MovieDto;

import java.util.List;

public interface IMovieService {
    MovieDto saveMovie(MovieDto movieDto);

    MovieDto getMovieById(Long id);

    void deleteMovieById(Long id);

    MovieDto updateMovie(MovieDto movieDto);

    List<MovieDto> getAllMovies();

    List<MovieDto> getMoviesByGenreId(Long id);
}
