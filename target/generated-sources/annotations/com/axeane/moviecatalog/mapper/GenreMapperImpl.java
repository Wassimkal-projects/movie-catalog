package com.axeane.moviecatalog.mapper;

import com.axeane.moviecatalog.domain.Genre;
import com.axeane.moviecatalog.service.dto.GenreDto;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-16T23:45:41+0100",
    comments = "version: 1.3.0.Beta1, compiler: javac, environment: Java 1.8.0_202 (Oracle Corporation)"
)
@Component
public class GenreMapperImpl implements GenreMapper {

    @Override
    public GenreDto genreToGenreDto(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        GenreDto genreDto = new GenreDto();

        genreDto.setId( genre.getId() );
        genreDto.setCode( genre.getCode() );
        genreDto.setDescription( genre.getDescription() );

        return genreDto;
    }

    @Override
    public Genre genreDtoGenre(GenreDto genreDto) {
        if ( genreDto == null ) {
            return null;
        }

        Genre genre = new Genre();

        genre.setId( genreDto.getId() );
        genre.setCode( genreDto.getCode() );
        genre.setDescription( genreDto.getDescription() );

        return genre;
    }

    @Override
    public List<GenreDto> toGenreDtoList(List<Genre> genres) {
        if ( genres == null ) {
            return null;
        }

        List<GenreDto> list = new ArrayList<GenreDto>( genres.size() );
        for ( Genre genre : genres ) {
            list.add( genreToGenreDto( genre ) );
        }

        return list;
    }

    @Override
    public Set<Genre> toGenreList(Set<GenreDto> genres) {
        if ( genres == null ) {
            return null;
        }

        Set<Genre> set = new HashSet<Genre>( Math.max( (int) ( genres.size() / .75f ) + 1, 16 ) );
        for ( GenreDto genreDto : genres ) {
            set.add( genreDtoGenre( genreDto ) );
        }

        return set;
    }
}
