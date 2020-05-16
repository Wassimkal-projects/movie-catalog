package com.axeane.moviecatalog.web.rest;

import com.axeane.moviecatalog.MovieCatalogApplication;
import com.axeane.moviecatalog.mapper.GenreMapper;
import com.axeane.moviecatalog.repository.GenreRepository;
import com.axeane.moviecatalog.service.dto.GenreDto;
import com.axeane.moviecatalog.service.interfaces.IGenreService;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieCatalogApplication.class)
public class GenreResourceTest {

    private static final String DEFAULT_CODE = "DEFAULT_CODE";
    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    GenreMapper genreMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private IGenreService genreService;

    private MockMvc mockMvc;

    private GenreDto genreDto;

    private GenreDto createEntity() {
        GenreDto genre = new GenreDto();
        genre.setCode(DEFAULT_CODE);
        genre.setDescription(DEFAULT_DESCRIPTION);
        return genre;
    }

    @Before
    public void initTests() {
        GenreResource genreResourceMock = new GenreResource(genreService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(genreResourceMock)
                .build();
    }

    @Test
    @Transactional
    public void saveGenre() throws Exception {
        genreDto = createEntity();
        mockMvc.perform(post("/genre")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void getGenreById() throws Exception {
        genreDto = createEntity();
        GenreDto savedGenre = genreMapper.genreToGenreDto(genreRepository.save(genreMapper.genreDtoGenre(genreDto)));
        mockMvc.perform(get("/genre/{id}", savedGenre.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getAllGenres() throws Exception {
        genreDto = createEntity();
        genreMapper.genreToGenreDto(genreRepository.save(genreMapper.genreDtoGenre(genreDto)));
        mockMvc.perform(get("/genre")
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].code").value(DEFAULT_CODE))
                .andExpect(jsonPath("$[0].description").value(DEFAULT_DESCRIPTION))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void updateGenre() throws Exception {
        genreDto = createEntity();
        GenreDto genreToUpdate = genreMapper.genreToGenreDto(genreRepository.save(genreMapper.genreDtoGenre(genreDto)));
        mockMvc.perform(put("/genre")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genreToUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void deleteGenreById() throws Exception {
        genreDto = createEntity();
        GenreDto genreToUpdate = genreMapper.genreToGenreDto(genreRepository.save(genreMapper.genreDtoGenre(genreDto)));
        mockMvc.perform(
                delete("/genre/{id}", genreToUpdate.getId()))
                .andExpect(status().isOk());
    }
}
