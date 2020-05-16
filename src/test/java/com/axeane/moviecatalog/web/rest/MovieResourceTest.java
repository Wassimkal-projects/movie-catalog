package com.axeane.moviecatalog.web.rest;

import com.axeane.moviecatalog.MovieCatalogApplication;
import com.axeane.moviecatalog.mapper.GenreMapper;
import com.axeane.moviecatalog.mapper.MovieMapper;
import com.axeane.moviecatalog.repository.GenreRepository;
import com.axeane.moviecatalog.repository.MovieRepository;
import com.axeane.moviecatalog.service.dto.GenreDto;
import com.axeane.moviecatalog.service.dto.MovieDto;
import com.axeane.moviecatalog.service.interfaces.IMovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieCatalogApplication.class)
public class MovieResourceTest {
    private static final String DEFAULT_TITLE = "DEFAULT_TITLE";
    private static final String DEFAULT_PRODUCER = "DEFAULT_DESCRIPTION";
    private static final Float DEFAULT_RATING = 4F;
    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.now();

    @Autowired
    ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private IMovieService movieService;

    private MovieDto movieDto;

    private MovieDto createEntity() {
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle(DEFAULT_TITLE);
        movieDto.setProducer(DEFAULT_PRODUCER);
        movieDto.setReleaseDate(DEFAULT_RELEASE_DATE);
        movieDto.setRating(DEFAULT_RATING);
        return movieDto;
    }

    @Before
    public void setup() {
        MovieResource movieResourceMock = new MovieResource(movieService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(movieResourceMock)
                .build();
    }


    @Test
    @Transactional
    public void saveMovie() throws Exception {
        movieDto = createEntity();
        mockMvc.perform(post("/movie")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void updateMovie() throws Exception {
        movieDto = createEntity();
        MovieDto movieToUpdate = movieMapper.movieToMovieDto(movieRepository.save(movieMapper.movieDtoToMovie(movieDto)));
        mockMvc.perform(put("/movie")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieToUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getMovieById() throws Exception {
        movieDto = createEntity();
        MovieDto savedMovie = movieMapper.movieToMovieDto(movieRepository.save(movieMapper.movieDtoToMovie(movieDto)));
        mockMvc.perform(get("/movie/{id}", savedMovie.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$.producer").value(DEFAULT_PRODUCER))
                .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getAllMovies() throws Exception {
        movieDto = createEntity();
        movieMapper.movieToMovieDto(movieRepository.save(movieMapper.movieDtoToMovie(movieDto)));
        mockMvc.perform(get("/movie")
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$[0].producer").value(DEFAULT_PRODUCER))
                .andExpect(jsonPath("$[0].rating").value(DEFAULT_RATING))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getMoviesByGenreId() throws Exception {
        GenreDto genre = new GenreDto();
        genre.setCode("DEFAULT_CODE");
        genre.setDescription("DEFAULT_DESCRIPTION");
        GenreDto savedGenre = genreMapper.genreToGenreDto(genreRepository.save(genreMapper.genreDtoGenre(genre)));

        movieDto =  createEntity();
        movieDto.getGenres().add(savedGenre);
        movieMapper.movieToMovieDto(movieRepository.save(movieMapper.movieDtoToMovie(movieDto)));

        mockMvc.perform(get("/movie/getMoviesByGenreId/{id}", savedGenre.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$[0].producer").value(DEFAULT_PRODUCER))
                .andExpect(jsonPath("$[0].rating").value(DEFAULT_RATING))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void deleteMovieById() throws Exception {
        movieDto = createEntity();
        MovieDto savedMovie = movieMapper.movieToMovieDto(movieRepository.save(movieMapper.movieDtoToMovie(movieDto)));
        mockMvc.perform(
                delete("/movie/{id}", savedMovie.getId()))
                .andExpect(status().isOk());
    }
}
