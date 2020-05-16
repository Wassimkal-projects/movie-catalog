# Movies catalog
* Le but de ce projet est de créer une relation de base de données Many to many entre la table Movie et la table Genre

## Création du projet: 
* Pour générer le projet, on a utilisé spring initializr, comme alternative on peut installer Spring boot CLI: 

![Image](https://github.com/axeaneProjects/github-wassim/blob/master/many-to-many/src/main/resources/initializr.JPG)  

### SGBD: PostgreSQL
* Puisque nous utilisons PostgreSQL comme base de données, nous devons ajouter les dépendances et configurer l'URL, le nom d'utilisateur et le mot de passe de la base de données afin que Spring puisse établir une connexion avec la base de données au démarrage,
cette configuration se fait au niveau du fichier properties.yml
```xml
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
		</dependency>
```
### Properties.yml:
```yaml
server:
  port: 8070

  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

  datasource:
    url: jdbc:postgresql://localhost:5432/movie-catalog
    username: wk-prjects
    password: admin
 ``` 
* Pour le suivi, la gestion et l'application des changements de schéma de base de données indépendamment de l'SGBD on utilise liquibase
 ```xml
		<dependency>
			<groupId>org.liquibase</groupId>
			<artifactId>liquibase-core</artifactId>
		</dependency>
 ```
* **db.changelog: changelog-master.xml :**
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd">

    <include file="/db/changelog/changes/initial-schema-1.xml"/>
</databaseChangeLog>

 ```
* On peut définir toutes les modifications dans le fichier master, mais il peut être difficile à gérer après le grand nombre de modifications  
**db.changelog.changes/initial-schema-1.xml :**  
Contient les informations concernant la création, la modification et l'insertion des données dans une table
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd">

    <changeSet id="001" author="wassim">

        <createSequence incrementBy="1"
                        schemaName="public"
                        sequenceName="movie_seq"
                        startValue="1"/>

        <createSequence incrementBy="1"
                        schemaName="public"
                        sequenceName="genre_seq"
                        startValue="1"/>

        <createTable tableName="movie">
            <column name="id" type="bigint">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="title" type="varchar"/>
            <column name="producer" type="varchar"/>
            <column name="release_date" type="date"/>
            <column name="rating" type="numeric"/>
            <column name="created_by" type="varchar(50)">
            </column>
            <column name="created_date" type="timestamp">
            </column>
            <column name="last_modified_by" type="varchar(50)"/>
            <column name="last_modified_date" type="timestamp"/>
        </createTable>

        <createTable tableName="genre">
            <column name="id" type="bigint">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="code" type="varchar"/>
            <column name="description" type="varchar"/>
            <column name="created_by" type="varchar(50)">
            </column>
            <column name="created_date" type="timestamp">
            </column>
            <column name="last_modified_by" type="varchar(50)"/>
            <column name="last_modified_date" type="timestamp"/>
        </createTable>

        <createTable tableName="genre_movies">
            <column name="genre_id" type="bigint"/>
            <column name="movie_id" type="bigint"/>
        </createTable>

    </changeSet>
</databaseChangeLog>

 ```

* Ensuite, il ajouter le path du fichier master dans ale fichier application.yml:  
 ```yaml
spring:
  liquibase:
    change-log: classpath:/db/changelog/changelog-master.xml
 ```

## Entités:
On utilise Spring Data JPA pour stocker et récupérer des données de la base de données  

 ```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
 ```
### Movie.java

 ```java
package com.axeane.moviecatalog.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "movie")
@EntityListeners(AuditingEntityListener.class)
public class Movie extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_SEQ")
    @SequenceGenerator(sequenceName = "movie_seq", allocationSize = 1, name = "MOVIE_SEQ")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "producer")
    private String producer;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "rating")
    private Float rating;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "genre_movies",
            joinColumns = {@JoinColumn(name = "movie_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id", referencedColumnName = "id")})
    private Set<Genre> genres = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", producer='" + producer + '\'' +
                ", releaseDate=" + releaseDate +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(getId(), movie.getId()) &&
                Objects.equals(getTitle(), movie.getTitle()) &&
                Objects.equals(getProducer(), movie.getProducer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getProducer());
    }
}

  ```

### Genre.java
```java
package com.axeane.moviecatalog.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "genre")
@EntityListeners(AuditingEntityListener.class)
public class Genre extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENRE_SEQ")
    @SequenceGenerator(sequenceName = "genre_seq", allocationSize = 1, name = "GENRE_SEQ")
    private Long id;

    @NotNull(message = "Le code ne doit pas être null")
    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Movie> movies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return Objects.equals(getId(), genre.getId()) &&
                Objects.equals(getCode(), genre.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode());
    }
}

```

## DTO: Data Transfert Objects
Le DTO est un patron de conception dans l'architecture logicielle qu'on va utiliser dans ce tutoriel. Le dto permet de:  
1/ Changer la façon de gérer la persistance des données sans toucher  le modèle "métier"  
2/ Cacher des données 
3/ Transporter des données entre les différentes couches distantes

### MovieDto.java
```java
package com.axeane.moviecatalog.service.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class MovieDto {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String producer;

    private LocalDate releaseDate;

    private Float rating;

    private Set<GenreDto> genres = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Set<GenreDto> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDto> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", producer='" + producer + '\'' +
                ", releaseDate=" + releaseDate +
                ", rating=" + rating +
                ", genres=" + genres +
                '}';
    }
}

```

### GenreDto.java
```java
package com.axeane.moviecatalog.service.dto;

import javax.validation.constraints.NotNull;

public class GenreDto {

    private Long id;

    @NotNull(message = "Le code ne doit pas être null")
    private String code;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GenreDto{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}


```

## MapStruct
Pour le mapping entre les DTO et entités, on a utilisé MapStruct: C'est un générateur de code qui simplifie l'implémentation des bean Java 
```xml
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-jdk8</artifactId>
            <version>1.1.0.Final</version>
        </dependency>
```

```xml
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>1.3.0.Beta1</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
```

### MovieMapper.java
```java
package com.axeane.moviecatalog.mapper;

import com.axeane.moviecatalog.domain.Movie;
import com.axeane.moviecatalog.service.dto.MovieDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {GenreMapper.class})
public interface MovieMapper {

    MovieDto movieToMovieDto(Movie movie);

    Movie movieDtoToMovie(MovieDto movieDto);

    List<MovieDto> toMovieDtoList(List<Movie> movies);
}

````

### GenreMapper.java
```java
package com.axeane.moviecatalog.mapper;

import com.axeane.moviecatalog.domain.Genre;
import com.axeane.moviecatalog.service.dto.GenreDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface GenreMapper {

    GenreDto genreToGenreDto(Genre genre);

    Genre genreDtoGenre(GenreDto genreDto);

    List<GenreDto> toGenreDtoList(List<Genre> genres);

}

```
## Repository
Ensuite, nous allons définir les repository pour accéder aux données de la base de données:

### MovieRepository.java
```java
package com.axeane.moviecatalog.repository;

import com.axeane.moviecatalog.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}

```

### GenreRepository.java
```java
package com.axeane.moviecatalog.repository;

import com.axeane.moviecatalog.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
```

## Services
## MovieServiceImpl.java
```java
package com.axeane.moviecatalog.service.implementation;

import com.axeane.moviecatalog.domain.Genre;
import com.axeane.moviecatalog.domain.Movie;
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

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.genreRepository = genreRepository;
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
            throw new BadRequestAlertException("L'id ne doit pas être null pour l'update");
        }

        movieRepository.findById(movieDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Ce film n'exite pas")
        );
        checkIfGenresExists(movieDto.getGenres());
        return movieMapper.movieToMovieDto(movieRepository.save(movieMapper.movieDtoToMovie(movieDto)));
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
        Genre genre = genreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ce genre n'existe pas")
        );
        return movieRepository.findAll()
                .stream()
                .filter(movie -> movie.getGenres().contains(genre))
                .map(movieMapper::movieToMovieDto)
                .collect(Collectors.toList());
    }
}
```
## GenreServiceImpl.java
```java
package com.axeane.moviecatalog.service.implementation;

import com.axeane.moviecatalog.domain.Genre;
import com.axeane.moviecatalog.mapper.GenreMapper;
import com.axeane.moviecatalog.repository.GenreRepository;
import com.axeane.moviecatalog.service.dto.GenreDto;
import com.axeane.moviecatalog.service.interfaces.IGenreService;
import com.axeane.moviecatalog.web.rest.errors.BadRequestAlertException;
import com.axeane.moviecatalog.web.rest.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenreServiceImpl implements IGenreService {

    private GenreRepository genreRepository;
    private GenreMapper genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    public GenreDto saveGenre(GenreDto genreDto) {
        if(genreDto.getId() != null){
            throw new BadRequestAlertException("L'id doit être null");
        }
        return genreMapper.genreToGenreDto(genreRepository.save(genreMapper.genreDtoGenre(genreDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ce Genre n'existe pas")
        );
        return genreMapper.genreToGenreDto(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::genreToGenreDto)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDto updateGenre(GenreDto genreDto) {
        if (genreDto.getId() == null) {
            throw new BadRequestAlertException("L'id ne doit pas être null pour l'update");
        }
        Genre genreToUpdate = genreRepository.findById(genreDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Ce Genre n'existe pas")
        );
        genreToUpdate.setCode(genreDto.getCode());
        genreToUpdate.setDescription(genreDto.getDescription());
        return genreMapper.genreToGenreDto(genreRepository.save(genreToUpdate));
    }

    @Override
    public void deleteGenreById(Long id) {
        Genre genreToDelete = genreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ce Genre n'existe pas")
        );
        genreRepository.delete(genreToDelete);
    }
}
```
## Resources
### MovieResource.java
```java
package com.axeane.moviecatalog.web.rest;

import com.axeane.moviecatalog.service.dto.MovieDto;
import com.axeane.moviecatalog.service.interfaces.IMovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/movie")
public class MovieResource {

    private IMovieService movieService;

    public MovieResource(IMovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/saveMovie")
    public ResponseEntity saveMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto savedMovie = movieService.saveMovie(movieDto);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    @PutMapping("/updateMovie")
    public ResponseEntity updateMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto updatedMovie = movieService.updateMovie(movieDto);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @GetMapping("/getMovieById/{id}")
    public ResponseEntity getMovieById(@PathVariable Long id) {
        return new ResponseEntity<>(movieService.getMovieById(id), HttpStatus.OK);
    }

    @GetMapping("/getAllMovies")
    public ResponseEntity getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @GetMapping("/getMoviesByGenreId/{id}")
    public ResponseEntity getMoviesByGenreId(@PathVariable Long id) {
        return new ResponseEntity<>(movieService.getMoviesByGenreId(id), HttpStatus.OK);
    }

    @DeleteMapping("/deleteMovieById/{id}")
    public ResponseEntity deleteMovieById(@PathVariable Long id) {
        movieService.deleteMovieById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}

```
### GenreResource.java
```java
package com.axeane.moviecatalog.web.rest;

import com.axeane.moviecatalog.service.dto.GenreDto;
import com.axeane.moviecatalog.service.interfaces.IGenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/genre")
public class GenreResource {

    private final IGenreService genreService;

    public GenreResource(IGenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping("/saveGenre")
    public ResponseEntity saveGenre(@Valid @RequestBody GenreDto genreDto) {
        GenreDto savedGenre = genreService.saveGenre(genreDto);
        return new ResponseEntity<>(savedGenre, HttpStatus.CREATED);
    }

    @GetMapping("/getGenreById/{id}")
    public ResponseEntity getGenreById(@PathVariable Long id) {
        GenreDto foundGenre = genreService.getGenreById(id);
        return new ResponseEntity<>(foundGenre, HttpStatus.OK);
    }

    @GetMapping("/getAllGenres")
    public ResponseEntity getAllGenres() {
        return new ResponseEntity<>(genreService.getAllGenres(), HttpStatus.OK);
    }

    @PutMapping("/updateGenre")
    public ResponseEntity updateGenre(@Valid @RequestBody GenreDto genreDto) {
        return new ResponseEntity<>(genreService.updateGenre(genreDto), HttpStatus.OK);
    }

    @DeleteMapping("/deleteGenreById/{id}")
    public ResponseEntity deleteGenreById(@PathVariable Long id) {
        genreService.deleteGenreById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}

```

## Exemple de Test avec Postman:
### Save genre
![Image](https://github.com/axeaneProjects/github-wassim/blob/master/many-to-many/src/main/resources/saveGenreJPG.JPG)  
### Save movie
![Image](https://github.com/axeaneProjects/github-wassim/blob/master/many-to-many/src/main/resources/saveMovie.JPG)  

