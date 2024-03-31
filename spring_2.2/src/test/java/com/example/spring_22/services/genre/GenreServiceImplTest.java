package com.example.spring_22.services.genre;

import com.example.spring_22.dao.genre.GenreRepository;
import com.example.spring_22.domain.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с жанрами должен:")
@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl sut;

    @Test
    @DisplayName("Сохранять несуществующего автора")
    void shouldSaveNotExistingAuthor() {
        //given
        var genre = mock(Genre.class);
        when(genreRepository.getByName(genre.getGenreName())).thenReturn(Optional.empty());
        when(genreRepository.save(genre)).thenReturn(genre);

        //when
        var result = sut.saveGenreIfNotExists(genre);

        //then
        assertEquals(genre, result);
    }

    @Test
    @DisplayName("Возвращать существующего автора")
    void shouldReturnExistingAuthor() {
        //given
        var genre = mock(Genre.class);
        when(genreRepository.getByName(genre.getGenreName())).thenReturn(Optional.of(genre));

        //when
        var result = sut.saveGenreIfNotExists(genre);

        //then
        assertEquals(genre, result);
    }
}