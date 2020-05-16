package com.axeane.moviecatalog.mapper;

import com.axeane.moviecatalog.domain.Movie;
import com.axeane.moviecatalog.service.dto.MovieDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {GenreMapper.class})
public interface MovieMapper {

    MovieDto movieToMovieDto(Movie movie);

    Movie movieDtoToMovie(MovieDto movieDto);

    List<MovieDto> toMovieDtoList(List<Movie> movies);
}
