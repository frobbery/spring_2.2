package com.example.spring_22.dao.book;

import com.example.spring_22.config.YamlPropertySourceFactory;
import com.example.spring_22.domain.Author;
import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Dao для работы с книгами должно:")
@DataJpaTest
@Import(BookRepositoryJpa.class)
@TestPropertySource(value = "/application-test.yml", factory = YamlPropertySourceFactory.class)
@Sql(value = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa sut;

    @Test
    @DisplayName("Сохранять книгу")
    void shouldSaveBook() {
        //given
        var expectedBook = Book.builder()
                .id(4L)
                .name("New")
                .build();

        //when
        var actualBook = sut.save(expectedBook);

        //then
        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("Находить книгу по id")
    void shouldFindBookById() {
        //given
        var expectedBook = Book.builder()
                .id(1L)
                .name("Regular adventure novel")
                .author(Author.builder()
                        .id(1L)
                        .fullName("Pushkin")
                        .build())
                .genres(List.of(Genre.builder()
                        .id(1L)
                        .genreName("Adventure")
                        .build()))
                .build();

        //when
        var actualBook = sut.findById(expectedBook.getId());

        //then
        assertThat(actualBook)
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.comments")
                .isEqualTo(Optional.of(expectedBook));
    }

    @Test
    @DisplayName("Находить все книги")
    void shouldFindAllBooks() {
        //when
        var allBooks = sut.findAll();

        //then
        assertEquals(3, allBooks.size());
    }

    @Test
    @DisplayName("Обновлять название книги по id")
    void shouldUpdateBookNameById() {
        //given
        var expectedBook = Book.builder()
                .id(1L)
                .name("New")
                .build();

        //when
        sut.updateNameById(expectedBook.getId(), expectedBook.getName());

        //then
        assertThat(sut.findById(expectedBook.getId()))
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.author", "value.genres", "value.comments")
                .isEqualTo(Optional.of(expectedBook));
    }

    @Test
    @DisplayName("Удалять книгу по id")
    void shouldDeleteBookById() {
        //given
        var bookId = 3L;

        //when
        sut.deleteById(bookId);

        //then
        assertThat(sut.findById(bookId))
                .isEmpty();
    }

    @Test
    @DisplayName("Добавлять жанр к книге")
    void shouldAddGenreToBook() {
        //given
        var newGenre = Genre.builder()
                .id(2L)
                .genreName("Romance")
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name("Regular adventure novel")
                .author(Author.builder()
                        .id(1L)
                        .fullName("Pushkin")
                        .build())
                .genres(List.of(Genre.builder()
                        .id(1L)
                        .genreName("Adventure")
                        .build(),
                        newGenre))
                .build();

        //when
        sut.addGenreToBook(expectedBook.getId(), newGenre);

        //then
        assertThat(sut.findById(expectedBook.getId()))
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.comments")
                .isEqualTo(Optional.of(expectedBook));
    }

    @Test
    @DisplayName("Обновлять автора книги")
    void shouldUpdateBookAuthor() {
        //given
        var newAuthor = Author.builder()
                .id(2L)
                .fullName("Rubina")
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name("Regular adventure novel")
                .author(newAuthor)
                .genres(List.of(Genre.builder()
                        .id(1L)
                        .genreName("Adventure")
                        .build()))
                .build();

        //when
        sut.updateAuthor(expectedBook.getId(), newAuthor);

        //then
        assertThat(sut.findById(expectedBook.getId()))
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.comments")
                .isEqualTo(Optional.of(expectedBook));
    }

    @Test
    @DisplayName("Удалять жанры книги")
    void shouldDeleteGenresFromBook() {
        //given
        var expectedBook = Book.builder()
                .id(1L)
                .name("Regular adventure novel")
                .author(Author.builder()
                        .id(1L)
                        .fullName("Pushkin")
                        .build())
                .genres(List.of())
                .build();

        //when
        sut.deleteBookGenreLinks(expectedBook.getId());

        //then
        assertThat(sut.findById(expectedBook.getId()))
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.comments")
                .isEqualTo(Optional.of(expectedBook));
    }

    @Test
    @DisplayName("Удалять жанр книги")
    void shouldDeleteGenreFromBook() {
        //given
        var genreToBeDeleted = Genre.builder()
                .id(1L)
                .genreName("Adventure")
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name("Regular adventure novel")
                .author(Author.builder()
                        .id(1L)
                        .fullName("Pushkin")
                        .build())
                .genres(List.of())
                .build();

        //when
        sut.deleteGenreFromBook(expectedBook.getId(), genreToBeDeleted);

        //then
        assertThat(sut.findById(expectedBook.getId()))
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.comments")
                .isEqualTo(Optional.of(expectedBook));
    }
}