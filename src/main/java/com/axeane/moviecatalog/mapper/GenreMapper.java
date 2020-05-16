package com.axeane.moviecatalog.mapper;

import com.axeane.moviecatalog.domain.Genre;
import com.axeane.moviecatalog.service.dto.GenreDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface GenreMapper {

    GenreDto genreToGenreDto(Genre genre);

    Genre genreDtoGenre(GenreDto genreDto);

    List<GenreDto> toGenreDtoList(List<Genre> genres);

    Set<Genre> toGenreList(Set<GenreDto> genres);

}
