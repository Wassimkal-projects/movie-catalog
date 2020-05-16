package com.axeane.moviecatalog.repository;

import com.axeane.moviecatalog.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
