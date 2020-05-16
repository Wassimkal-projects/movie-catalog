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
                () -> new ResourceNotFoundException("Le Genre avec l'id "+ "id" +" n'existe pas")
        );
        genreRepository.delete(genreToDelete);
    }
}
