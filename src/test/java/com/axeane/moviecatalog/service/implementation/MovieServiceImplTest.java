package com.axeane.moviecatalog.service.implementation;

import com.axeane.moviecatalog.MovieCatalogApplication;
import com.axeane.moviecatalog.domain.Movie;
import com.axeane.moviecatalog.mapper.GenreMapper;
import com.axeane.moviecatalog.mapper.MovieMapper;
import com.axeane.moviecatalog.repository.GenreRepository;
import com.axeane.moviecatalog.repository.MovieRepository;
import com.axeane.moviecatalog.service.dto.GenreDto;
import com.axeane.moviecatalog.service.dto.MovieDto;
import com.axeane.moviecatalog.service.interfaces.IGenreService;
import com.axeane.moviecatalog.service.interfaces.IMovieService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieCatalogApplication.class)
public class MovieServiceImplTest {

    private static final String DEFAULT_TITLE = "DEFAULT_TITLE";
    private static final String DEFAULT_PRODUCER = "DEFAULT_DESCRIPTION";
    private static final Float DEFAULT_RATING = 4F;
    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.now();


    private static final String DEFAULT_CODE = "DEFAULT_CODE";
    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private IMovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private IGenreService genreService;

    private MovieDto movieDto;
    private GenreDto genreDto;

    private MovieDto createMovieEntity() {
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle(DEFAULT_TITLE);
        movieDto.setProducer(DEFAULT_PRODUCER);
        movieDto.setReleaseDate(DEFAULT_RELEASE_DATE);
        movieDto.setRating(DEFAULT_RATING);
        return movieDto;
    }

    private GenreDto createGenreEntity() {
        GenreDto genre = new GenreDto();
        genre.setCode(DEFAULT_CODE);
        genre.setDescription(DEFAULT_DESCRIPTION);
        return genre;
    }

    @Before
    public void initTest() {
        movieService = new MovieServiceImpl(movieRepository, movieMapper, genreRepository, genreMapper);
    }

    @Test
    @Transactional
    public void saveMovie() {
        movieDto = createMovieEntity();
        long dbSizeBeforeTest = movieRepository.count();
        MovieDto movie = movieService.saveMovie(movieDto);
        Movie savedMovie = movieRepository.findById(movie.getId()).orElse(null);
        assertThat(movieRepository.count()).isEqualTo(dbSizeBeforeTest + 1);
        assertThat(savedMovie).isNotNull();
        assertThat(savedMovie.getId()).isNotNull();
        assertThat(savedMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(savedMovie.getProducer()).isEqualTo(DEFAULT_PRODUCER);
        assertThat(savedMovie.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(savedMovie.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void getMovieById() {
        movieDto = createMovieEntity();
        long dbSizeBeforeTest = movieRepository.count();
        MovieDto savedMovie = movieService.saveMovie(movieDto);
        MovieDto foundMovie = movieService.getMovieById(savedMovie.getId());

        assertThat(movieRepository.count()).isEqualTo(dbSizeBeforeTest + 1);
        assertThat(foundMovie).isNotNull();
        assertThat(foundMovie.getId()).isNotNull();
        assertThat(foundMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(foundMovie.getProducer()).isEqualTo(DEFAULT_PRODUCER);
        assertThat(foundMovie.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(foundMovie.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void deleteMovieById() {
        movieDto = createMovieEntity();
        MovieDto savedMovie = movieService.saveMovie(movieDto);

        assertThat(savedMovie).isNotNull();
        assertThat(savedMovie.getId()).isNotNull();
        long dbSizeBeforeDelete = movieRepository.count();
        movieService.deleteMovieById(savedMovie.getId());
        assertThat(movieRepository.count()).isEqualTo(dbSizeBeforeDelete - 1);
        Optional<Movie> deletedMovie = movieRepository.findById(savedMovie.getId());
        assertThat(deletedMovie).isNotPresent();
    }

    @Test
    @Transactional
    public void updateMovie() {
        movieDto = createMovieEntity();
        MovieDto movieToUpdate = movieService.saveMovie(movieDto);
        movieToUpdate.setTitle("NEW TITLE");
        movieToUpdate.setProducer("NEW PRODUCER");

        MovieDto updateMovie = movieService.updateMovie(movieToUpdate);
        assertThat(updateMovie).isNotNull();
        assertThat(updateMovie.getId()).isNotNull();
        assertThat(updateMovie.getTitle()).isEqualTo("NEW TITLE");
        assertThat(updateMovie.getProducer()).isEqualTo("NEW PRODUCER");
    }

    @Test
    @Transactional
    public void getAllMovies() {
        movieDto = createMovieEntity();
        movieService.saveMovie(movieDto);
        List<MovieDto> foundMovies = movieService.getAllMovies();

        assertThat(foundMovies.size()).isEqualTo(1);
        assertThat(foundMovies.get(foundMovies.size() - 1).getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(foundMovies.get(foundMovies.size() - 1).getProducer()).isEqualTo(DEFAULT_PRODUCER);
        assertThat(foundMovies.get(foundMovies.size() - 1).getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(foundMovies.get(foundMovies.size() - 1).getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getMoviesByGenreId() {
        movieDto = createMovieEntity();
        genreDto = createGenreEntity();
        GenreDto savedGenre = genreService.saveGenre(genreDto);
        movieDto.getGenres().add(savedGenre);
        movieService.saveMovie(movieDto);

        List<MovieDto> foundMovies = movieService.getMoviesByGenreId(savedGenre.getId());
        assertThat(foundMovies.size()).isEqualTo(1);
        assertThat(foundMovies.get(foundMovies.size() - 1).getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(foundMovies.get(foundMovies.size() - 1).getProducer()).isEqualTo(DEFAULT_PRODUCER);
        assertThat(foundMovies.get(foundMovies.size() - 1).getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(foundMovies.get(foundMovies.size() - 1).getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
    }
}
