package com.axeane.moviecatalog.repository;

import com.axeane.moviecatalog.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = "select distinct m.* " +
            "from movie as m, genre_movies as gm " +
            "where gm.genre_id = :genreId and m.id = gm.movie_id", nativeQuery = true)
    List<Movie> getMoviesByGenreId(@Param("genreId") Long genreId);
}
