package com.axeane.moviecatalog.service.implementation;

import com.axeane.moviecatalog.domain.Movie;
import com.axeane.moviecatalog.mapper.GenreMapper;
import com.axeane.moviecatalog.mapper.MovieMapper;
import com.axeane.moviecatalog.repository.GenreRepository;
import com.axeane.moviecatalog.repository.MovieRepository;
import com.axeane.moviecatalog.service.dto.GenreDto;
import com.axeane.moviecatalog.service.dto.MovieDto;
import com.axeane.moviecatalog.service.interfaces.IMovieService;
import com.axeane.moviecatalog.web.rest.errors.BadRequestAlertException;
import com.axeane.moviecatalog.web.rest.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieServiceImpl implements IMovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper, GenreRepository genreRepository, GenreMapper genreMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    public MovieDto saveMovie(MovieDto movieDto) {
        if (movieDto.getId() != null) {
            throw new BadRequestAlertException("L'id doit être null");
        }
        checkIfGenresExists(movieDto.getGenres());
        return movieMapper.movieToMovieDto(movieRepository.save(movieMapper.movieDtoToMovie(movieDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ce film n'existe pas")
        );
        return movieMapper.movieToMovieDto(movie);
    }

    @Override
    public void deleteMovieById(Long id) {
        MovieDto movie = getMovieById(id);
        movieRepository.deleteById(movie.getId());
    }

    @Override
    public MovieDto updateMovie(MovieDto movieDto) {
        if (movieDto.getId() == null) {
            throw new BadRequestAlertException("L'id ne doit pas être null pour la mise à jour du film");
        }

        Movie movieToUpadate = movieRepository.findById(movieDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Ce film n'exite pas")
        );
        checkIfGenresExists(movieDto.getGenres());
        movieToUpadate.setTitle(movieDto.getTitle());
        movieToUpadate.setProducer(movieDto.getProducer());
        movieToUpadate.setReleaseDate(movieDto.getReleaseDate());
        movieToUpadate.setRating(movieDto.getRating());
        movieToUpadate.setGenres(genreMapper.toGenreList(movieDto.getGenres()));
        return movieMapper.movieToMovieDto(movieRepository.save(movieToUpadate));
    }

    private void checkIfGenresExists(Set<GenreDto> genres) {
        if (genres != null) {
            genres.forEach(genreDto -> {
                if (genreDto.getId() == null) {
                    throw new BadRequestAlertException("L'id ne doit pas être null");
                }

                if (!genreRepository.findById(genreDto.getId()).isPresent()) {
                    throw new ResourceNotFoundException("Le genre avec l'id " + genreDto.getId() + " n'existe pas");
                }
            });
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movieMapper::movieToMovieDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> getMoviesByGenreId(Long id) {
        return movieMapper.toMovieDtoList(movieRepository.getMoviesByGenreId(id));
    }
}
