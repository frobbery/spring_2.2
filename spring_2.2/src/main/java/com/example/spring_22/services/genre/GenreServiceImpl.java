package com.example.spring_22.services.genre;


import com.example.spring_22.dao.genre.GenreRepository;
import com.example.spring_22.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public Genre saveGenreIfNotExists(Genre genre) {
        return genreRepository.getByName(genre.getGenreName()).orElse(genreRepository.save(genre));
    }
}
