package com.axeane.moviecatalog.service.interfaces;

import com.axeane.moviecatalog.service.dto.GenreDto;

import java.util.List;

public interface IGenreService {
    GenreDto saveGenre(GenreDto genreDto);

    GenreDto getGenreById(Long id);

    List<GenreDto> getAllGenres();

    GenreDto updateGenre(GenreDto genreDto);

    void deleteGenreById(Long id);
}
