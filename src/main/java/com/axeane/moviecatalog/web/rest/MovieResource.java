package com.axeane.moviecatalog.web.rest;

import com.axeane.moviecatalog.service.dto.MovieDto;
import com.axeane.moviecatalog.service.interfaces.IMovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieResource {

    private IMovieService movieService;

    public MovieResource(IMovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<MovieDto> saveMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto savedMovie = movieService.saveMovie(movieDto);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<MovieDto> updateMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto updatedMovie = movieService.updateMovie(movieDto);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return new ResponseEntity<>(movieService.getMovieById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<MovieDto> foundMovies = movieService.getAllMovies();
        if (foundMovies.isEmpty()) {
            return new ResponseEntity<>(foundMovies, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(foundMovies, HttpStatus.OK);
    }

    @GetMapping("/getMoviesByGenreId/{id}")
    public ResponseEntity<List<MovieDto>> getMoviesByGenreId(@PathVariable Long id) {
        List<MovieDto> foundMovies = movieService.getAllMovies();
        if (foundMovies.isEmpty()) {
            return new ResponseEntity<>(foundMovies, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(foundMovies, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMovieById(@PathVariable Long id) {
        movieService.deleteMovieById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
