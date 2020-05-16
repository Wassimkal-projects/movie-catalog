package com.axeane.moviecatalog.mapper;

import com.axeane.moviecatalog.domain.Genre;
import com.axeane.moviecatalog.domain.Movie;
import com.axeane.moviecatalog.service.dto.GenreDto;
import com.axeane.moviecatalog.service.dto.MovieDto;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-16T23:45:42+0100",
    comments = "version: 1.3.0.Beta1, compiler: javac, environment: Java 1.8.0_202 (Oracle Corporation)"
)
@Component
public class MovieMapperImpl implements MovieMapper {

    @Autowired
    private GenreMapper genreMapper;

    @Override
    public MovieDto movieToMovieDto(Movie movie) {
        if ( movie == null ) {
            return null;
        }

        MovieDto movieDto = new MovieDto();

        movieDto.setId( movie.getId() );
        movieDto.setTitle( movie.getTitle() );
        movieDto.setProducer( movie.getProducer() );
        movieDto.setReleaseDate( movie.getReleaseDate() );
        movieDto.setRating( movie.getRating() );
        movieDto.setGenres( genreSetToGenreDtoSet( movie.getGenres() ) );

        return movieDto;
    }

    @Override
    public Movie movieDtoToMovie(MovieDto movieDto) {
        if ( movieDto == null ) {
            return null;
        }

        Movie movie = new Movie();

        movie.setId( movieDto.getId() );
        movie.setTitle( movieDto.getTitle() );
        movie.setProducer( movieDto.getProducer() );
        movie.setReleaseDate( movieDto.getReleaseDate() );
        movie.setRating( movieDto.getRating() );
        movie.setGenres( genreMapper.toGenreList( movieDto.getGenres() ) );

        return movie;
    }

    @Override
    public List<MovieDto> toMovieDtoList(List<Movie> movies) {
        if ( movies == null ) {
            return null;
        }

        List<MovieDto> list = new ArrayList<MovieDto>( movies.size() );
        for ( Movie movie : movies ) {
            list.add( movieToMovieDto( movie ) );
        }

        return list;
    }

    protected Set<GenreDto> genreSetToGenreDtoSet(Set<Genre> set) {
        if ( set == null ) {
            return null;
        }

        Set<GenreDto> set1 = new HashSet<GenreDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Genre genre : set ) {
            set1.add( genreMapper.genreToGenreDto( genre ) );
        }

        return set1;
    }
}
