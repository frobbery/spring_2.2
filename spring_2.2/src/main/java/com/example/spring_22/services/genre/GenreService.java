package com.example.spring_22.services.genre;

import com.example.spring_22.domain.Genre;

public interface GenreService {

    Genre saveGenreIfNotExists(Genre genre);
}
