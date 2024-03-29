package com.example.spring_22.dao.genre;

import com.example.spring_22.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> getByName(String genreName);

    Genre save(Genre genre);
}
