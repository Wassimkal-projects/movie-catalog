package com.axeane.moviecatalog.web.rest;

import com.axeane.moviecatalog.service.dto.GenreDto;
import com.axeane.moviecatalog.service.interfaces.IGenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/genre")
public class GenreResource {

    private final IGenreService genreService;

    public GenreResource(IGenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public ResponseEntity<GenreDto> saveGenre(@Valid @RequestBody GenreDto genreDto) {
        GenreDto savedGenre = genreService.saveGenre(genreDto);
        return new ResponseEntity<>(savedGenre, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable Long id) {
        GenreDto foundGenre = genreService.getGenreById(id);
        return new ResponseEntity<>(foundGenre, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        List<GenreDto> foundGenres = genreService.getAllGenres();
        if(foundGenres.isEmpty()){
            return new ResponseEntity<>(foundGenres,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(genreService.getAllGenres(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<GenreDto> updateGenre(@Valid @RequestBody GenreDto genreDto) {
        return new ResponseEntity<>(genreService.updateGenre(genreDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteGenreById(@PathVariable Long id) {
        genreService.deleteGenreById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
