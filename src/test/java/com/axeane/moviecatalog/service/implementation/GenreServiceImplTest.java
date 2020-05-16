package com.axeane.moviecatalog.service.implementation;

import com.axeane.moviecatalog.MovieCatalogApplication;
import com.axeane.moviecatalog.domain.Genre;
import com.axeane.moviecatalog.mapper.GenreMapper;
import com.axeane.moviecatalog.repository.GenreRepository;
import com.axeane.moviecatalog.service.dto.GenreDto;
import com.axeane.moviecatalog.service.interfaces.IGenreService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieCatalogApplication.class)
public class GenreServiceImplTest {

    private static final String DEFAULT_CODE = "DEFAULT_CODE";
    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private IGenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreMapper genreMapper;

    private GenreDto genreDto;

    private GenreDto createEntity() {
        GenreDto genre = new GenreDto();
        genre.setCode(DEFAULT_CODE);
        genre.setDescription(DEFAULT_DESCRIPTION);
        return genre;
    }

    @Before
    public void initTest() {
        genreService = new GenreServiceImpl(genreRepository, genreMapper);
    }

    @Test
    @Transactional
    public void saveGenre() {
        genreDto = createEntity();
        long dbSizeBeforeTest = genreRepository.count();
        GenreDto savedGenre = genreService.saveGenre(genreDto);
        assertThat(genreRepository.count()).isEqualTo(dbSizeBeforeTest + 1);
        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getId()).isNotNull();
        assertThat(savedGenre.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(savedGenre.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getGenreById() {
        genreDto = createEntity();
        long dbSizeBeforeTest = genreRepository.count();
        GenreDto savedGenre = genreService.saveGenre(genreDto);
        GenreDto foundGenre = genreService.getGenreById(savedGenre.getId());
        assertThat(genreRepository.count()).isEqualTo(dbSizeBeforeTest + 1);
        assertThat(foundGenre).isNotNull();
        assertThat(foundGenre.getId()).isNotNull();
        assertThat(foundGenre.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(foundGenre.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllGenres() {
        genreDto = createEntity();
        long dbSizeBeforeTest = genreRepository.count();
        genreService.saveGenre(genreDto);
        List<GenreDto> foundGenres = genreService.getAllGenres();
        assertThat(foundGenres.size()).isEqualTo(dbSizeBeforeTest + 1);
        assertThat(foundGenres.get(foundGenres.size() - 1).getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(foundGenres.get(foundGenres.size() - 1).getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateGenre() {
        genreDto = createEntity();
        GenreDto genreToUpdate = genreService.saveGenre(genreDto);
        genreToUpdate.setCode("NEW CODE");
        genreToUpdate.setDescription("NEW DESCRIPTION");

        GenreDto updatedGenre = genreService.updateGenre(genreToUpdate);
        assertThat(updatedGenre).isNotNull();
        assertThat(updatedGenre.getId()).isNotNull();
        assertThat(updatedGenre.getCode()).isEqualTo("NEW CODE");
        assertThat(updatedGenre.getDescription()).isEqualTo("NEW DESCRIPTION");
    }

    @Test
    @Transactional
    public void deleteGenreById() {
        genreDto = createEntity();
        GenreDto savedGenre = genreService.saveGenre(genreDto);
        long dbSizeBeforDelete = genreRepository.count();
        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getId()).isNotNull();
        genreService.deleteGenreById(savedGenre.getId());
        Optional<Genre> deletedGenre = genreRepository.findById(savedGenre.getId());
        assertThat(genreRepository.count()).isEqualTo(dbSizeBeforDelete - 1);
        assertThat(deletedGenre).isNotPresent();
    }
}
